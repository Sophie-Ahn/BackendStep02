package org.zerock.springex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.PageRequestDto;
import org.zerock.springex.dto.TodoDto;
import org.zerock.springex.service.TodoService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // /todo/list
    @RequestMapping("/list")
    public void list(@Valid PageRequestDto pageRequestDto, BindingResult bindingResult, Model model){

        log.info("todo list............");

        if(bindingResult.hasErrors()) {
            // 디폴트 값을 가지게 된다.(page=1, size=10)
            // 첫번째 페이지가 나오도록
            pageRequestDto = PageRequestDto.builder().build();
        }

        // pageRequestDto를 todoService.getList에 넘겨주면, PageResponseDto를 리턴한다.
        // 이 리턴된 값을 model -> request -> jsp에 전달
        // 이 전달된 responseDto를 jsp가 꺼내서 bootstrap의 pagination 컴포넌트를 구성
        model.addAttribute("responseDto", todoService.getList(pageRequestDto));

//        model.addAttribute("dtoList", todoService.getAll());

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
    */
    @PostMapping("/register")
    public String registerPost(@Valid TodoDto todoDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("post todo register...........");

        // todoDto의 제약조건이 오류가 발생했을 때
        if(bindingResult.hasErrors()){
            log.info("has errors....");
            redirectAttributes.addAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/todo/register";
        }

        log.info(todoDto);

        todoService.register(todoDto);

        return "redirect:/todo/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long tno, PageRequestDto pageRequestDto, Model model) {
        TodoDto todoDto = todoService.getOne(tno);
        log.info(todoDto);

        model.addAttribute("dto", todoDto);
    }

    @PostMapping("/remove")
    public String remove(Long tno,PageRequestDto pageRequestDto, RedirectAttributes redirectAttributes){
        log.info("-----------------remove---------------");
        log.info("tno: " + tno);

        todoService.remove(tno);

//        redirectAttributes.addAttribute("page", 1);
//        redirectAttributes.addAttribute("size", pageRequestDto.getSize());

        return "redirect:/todo/list?" + pageRequestDto.getLink();
    }

    @PostMapping("/modify")
    public String modify(PageRequestDto pageRequestDto, @Valid TodoDto todoDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            log.info("has errors....................");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("tno", todoDto.getTno());

            return "redirect:/todo/modify";
        }

        log.info(todoDto);
        todoService.modify(todoDto);

//        redirectAttributes.addAttribute("page", pageRequestDto.getPage());
//        redirectAttributes.addAttribute("size", pageRequestDto.getSize());
        redirectAttributes.addAttribute("tno", todoDto.getTno());

        return "redirect:/todo/read";
    }
}
