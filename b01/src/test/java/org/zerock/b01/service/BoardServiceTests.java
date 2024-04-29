package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        log.info(boardService.getClass().getName());

        BoardDto boardDto = BoardDto.builder()
                .title("Sample Title......")
                .content("Sample Content.........")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDto);

        log.info("bno: " + bno);
    }

    @Test
    public void testModify(){

        // 변경에 필요한 데이터만
        BoardDto boardDto = BoardDto.builder()
                .bno(101L)
                .title("Updated........101")
                .content("Updated content........101")
                .build();

        // 첨부파일을 하나 추가
        boardDto.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));

        boardService.modify(boardDto);
    }

    @Test
    public void testList(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDto<BoardDto> responseDto = boardService.list(pageRequestDto);

        log.info(responseDto);
    }

    @Test
    public void testRegisterWithImages(){
        log.info(boardService.getClass().getName());

        BoardDto boardDto = BoardDto.builder()
                .title("File........Sample Title")
                .content("Sample Content..........")
                .writer("user00")
                .build();

        boardDto.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_bbb.jpg"
                ));

        Long bno = boardService.register(boardDto);

        log.info("bno: " + bno);
    }

    @Test
    public void testReadAll(){
        Long bno = 101L;

        BoardDto boardDto = boardService.readOne(bno);

        log.info(boardDto);

        for(String fileName : boardDto.getFileNames()){
            log.info(fileName);
        }
    }

    @Test
    public void testRemoveAll(){
        Long bno = 2L;
        boardService.remove(bno);
    }

    @Test
    public void testListWithAll(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDto<BoardListAllDto> responseDto = boardService.listWithAll(pageRequestDto);

        List<BoardListAllDto> dtoList = responseDto.getDtoList();

        dtoList.forEach(boardListAllDto -> {
            log.info(boardListAllDto.getBno()+":" + boardListAllDto.getTitle());

            if(boardListAllDto.getBoardImages() != null){
                for(BoardImageDto boardImage : boardListAllDto.getBoardImages()){
                    log.info(boardImage);
                }
            }

            log.info("------------------------");
        });
    }

}
