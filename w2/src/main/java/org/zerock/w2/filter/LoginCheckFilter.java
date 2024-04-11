package org.zerock.w2.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
        if(session.getAttribute("loginInfo") == null){
            resp.sendRedirect("/login");
            return;
        }

        // 다음 필터 또는 서블릿으로 전달
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
