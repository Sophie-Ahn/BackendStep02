package org.zerock.w2.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Log4j2
public class W2AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("-------------init------------------------");
        log.info("-------------init------------------------");
        log.info("-------------init------------------------");

        ServletContext sevletContext = sce.getServletContext();

        sevletContext.setAttribute("appName", "W2");
    }

    // 프로그램이 종료될 때 호출이 된다.
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("-------------destroy------------------------");
        log.info("-------------destroy------------------------");
        log.info("-------------destroy------------------------");
    }
}
