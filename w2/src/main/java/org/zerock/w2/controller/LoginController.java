package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDto;
import org.zerock.w2.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = "/login")
@Log4j2
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login get...............");

        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login post...............");

        String mid = req.getParameter("mid");
        String mpw = req.getParameter("mpw");

//        String str = mid+mpw;
//
//        HttpSession session = req.getSession();
//
//        // 임시로 id+pw의 문자열을 loginInfo로 sesstion에 저장한다.
//        session.setAttribute("loginInfo", str);
//
//        resp.sendRedirect("/todo/list");

        try{
            MemberDto memberDto = MemberService.INSTANCE.login(mid, mpw);
            HttpSession session = req.getSession();
            session.setAttribute("loginInfo", memberDto);
            resp.sendRedirect("/todo/list");
        } catch (Exception e){
            resp.sendRedirect("/login?result=error");
        }
    }
}
