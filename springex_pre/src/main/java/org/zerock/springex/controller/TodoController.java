package org.zerock.springex.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zerock.springex.dto.TodoDto;

@Controller
@Log4j2
@RequestMapping("/todo")
public class TodoController {

    // /todo/list
    @RequestMapping("/list")
    public void list(){
        log.info("todo list............");

        // /WEB-INF/views/list.jsp
    }

    // /todo/register
//    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @GetMapping("/register")
    public void register(){
        log.info("todo register...........");

        // /WEB-INF/views/register.jsp
    }

//    @PostMapping("/register")
//    public void registerPost() {
//        log.info("post todo register.....");
//    }

    /* 웹에서 보내오는 parameter들이
    * TodoDto 내부의 필드들의 이름과 매칭되면 todoDto객체 내부에 저장된다.
    * */
    @PostMapping("/register")
    public void registerPost(TodoDto todoDto){
        log.info("post todo register...........");
        log.info(todoDto);
    }

}
