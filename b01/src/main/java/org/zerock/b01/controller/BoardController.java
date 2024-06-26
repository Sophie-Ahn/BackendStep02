package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.BoardDto;
import org.zerock.b01.dto.BoardListAllDto;
import org.zerock.b01.dto.PageRequestDto;
import org.zerock.b01.dto.PageResponseDto;
import org.zerock.b01.service.BoardService;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

/*
* resources/satic : 프론트엔드 파일(No thymleaf html, css, js)
* resources/templates : controller에 대응되는 view 파일들이 존재하는 곳
* */

@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    @Value("${org.zerock.upload.path}") // import 시에 springframework으로 시작하는 value
    private String uploadPath;

    private final BoardService boardService;

//    @GetMapping("/list")
//    public void list(PageRequestDto pageRequestDto, Model model){
//        PageResponseDto<BoardDto> responseDto = boardService.list(pageRequestDto);
//        log.info(responseDto);
//        model.addAttribute("responseDto", responseDto);
//
//        // templates/board/list에 pageRequest와 responseDto가 request에 담겨서 전달된다.
//    }

    @GetMapping("/list")
    public void list(PageRequestDto pageRequestDto, Model model){
//        PageResponseDto<BoardDto> responseDto = boardService.list(pageRequestDto);

//        PageResponseDto<BoardListReplyCountDto> responseDto = boardService.listWithReplyCount(pageRequestDto);

        PageResponseDto<BoardListAllDto> responseDto = boardService.listWithAll(pageRequestDto);

        log.info(responseDto);

        model.addAttribute("responseDto", responseDto);
    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDto boardDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("board POST register......");

        if(bindingResult.hasErrors()){
            log.info("has errors..........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        log.info(boardDto);

        Long bno = boardService.register(boardDto);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDto pageRequestDto, Model model){
        BoardDto boardDto = boardService.readOne(bno);

        log.info(boardDto);

        model.addAttribute("dto", boardDto);

        /*
        * /board/read 요청과 /board/modify요청은 모두 bno에 해당하는 1개 row의 정보를 dto로 받는다.
        * 다만, 이동하는 페이지가 templates/board/read.html이고 templates/board/modify.html로 달라진다.
        * read.html은 readonly 속성으로 읽기만 가능하고, modify.html은 수정이 가능한다.
        * */
    }

    @PostMapping("/modify")
    public String modify(PageRequestDto pageRequestDto, @Valid BoardDto boardDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("board modify post........." + boardDto);

        if(bindingResult.hasErrors()){
            log.info("has errors.........");

            String link = pageRequestDto.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addFlashAttribute("bno", boardDto.getBno());

            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDto);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDto.getBno());

        return "redirect:/board/read";
    }

//    @PostMapping("/remove")
//    public String remove(Long bno, RedirectAttributes redirectAttributes){
//        log.info("remove post...." + bno);
//
//        boardService.remove(bno);
//
//        redirectAttributes.addFlashAttribute("result" + "removed");
//
//        return "redirect:/board/list";
//    }
    @PostMapping("/remove")
    public String remove(BoardDto boardDto, RedirectAttributes redirectAttributes){
        Long bno = boardDto.getBno();
        log.info("remove post...." + bno);

        boardService.remove(bno);

        // 게시뭉리 데이터베이스상에서 삭제되었다면 첨부파일 삭제
        log.info(boardDto.getFileNames());
        List<String> fileNames = boardDto.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFile(fileNames);
        }

        redirectAttributes.addFlashAttribute("result" + "removed");

        return "redirect:/board/list";
    }

    public void removeFile(List<String> files){
        for(String fileName : files){
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();

            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                // 썸네일이 존재한다면
                if(contentType.startsWith("image")){
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                    thumbnailFile.delete();
                }
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }

}
