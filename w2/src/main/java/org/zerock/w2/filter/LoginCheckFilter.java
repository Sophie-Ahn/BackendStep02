package org.zerock.w2.filter;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDto;
import org.zerock.w2.service.MemberService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(urlPatterns = {"/todo/*", "", ""})
@Log4j2
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Login check filter.......");

        // 로그인 정보가 세션에 존재하는지 여부를 판단하는 기능을 여기에다 넣을 거임
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        HttpSession session = req.getSession();

        // 세션에 로그인 정보가 있으니 로그인이라고 판단하고 서블릿으로 전송
        if(session.getAttribute("loginInfo") != null){
            // 다음 필터 또는 서블릿으로 전달
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // session에 loginInfo가 없다면
        // 쿠키를 체크
        // 세션에 loginInfo는 없지만, 브라우저에서 remember-me를 보냈는지 찾는다.
        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        // 세션에도 없고 쿠키도 없다면 그냥 로그인으로
        // remember-me가 없으면, login하라고 /login으로 보낸다.
        if(cookie == null){
            resp.sendRedirect("/login");
            return;
        }

        // 쿠키가 존재하는 상황이라면
        log.info("cookie는 존재하는 상황");

        // uuid값
        // remember-me의 value를 추출
        String uuid = cookie.getValue();

        try {
            // 데이터베이스 확인
            // 브라우저의 remember-me 쿠키값과 Maria-DB의 uuid값이 일치하는 지
            MemberDto memberDto = MemberService.INSTANCE.getByUUID(uuid);

            log.info("쿠키의 값으로 조회한 사용자 정보: " + memberDto);

            // remember-me의 정보가 Maria-DB의 uuid와 일치하지 않는다.
            if(memberDto == null){
                throw new EOFException("Cookie value is not valid");
            }

            // 회원정보를 세션에 추가
            // 쿠키값과 DB값이 일치하면, loginInfo를 세션에 저장하고
            // 서블릿 전송
            session.setAttribute("loginInfo", memberDto);
            // 다음 필터 또는 서블릿으로 전달
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/login");
        }
    }

    private Cookie findCookie(Cookie[] cookies, String name){
        if(cookies == null || cookies.length == 0) {
            return null;
        }

        // Cookie객체를 얻는데, 추가로 Optional에서 제공하는 메서드를 사용할 수 있다.
        Optional<Cookie> result = Arrays.stream(cookies)
                .filter(ck -> ck.getName().equals(name))
                .findFirst();

        // result가 정상이면 Cookie객체를 리턴하고, 아니면 null값을 리턴한다.
        return result.isPresent()?result.get():null;
    }
}
