package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardListAllDto;
import org.zerock.b01.dto.BoardListReplyCountDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert(){
        // 1이상 ~ 100이하까지
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title.........")
                    .content("content....." + i)
                    .writer("user" + 1 % 10)
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    public void testSelect(){
        Long bno = 100L;

        // Board값이 혹시라도 정상적이지 않을까봐
        // Optional을 통해서 정상적으로 받을 수 있게 해줌
        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    public void testUpdate(){
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update....title 100", "update content 100");

        /*
        * save: pk가 테이블에 존재하지 않으면 => insert문
        *       pk가 기존 테이블에 존재하면 => update문
        */
        boardRepository.save(board);
    }

    @Test
    public void testDelete(){
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(10,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));
    }

//    @Test
//    public void testTime(){
//        String nowTime = boardRepository.getTime();
//        log.info("nowTime : " + nowTime);
//    }
//
//    @Test
//    public void testKeyword(){
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        Page<Board> result = boardRepository.findKeyword("1", pageable);
//        List<Board> todoList = result.getContent();
//        todoList.forEach(board -> log.info(board));
//    }

    @Test
    public void testSearch1(){
        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }

    @Test
    public void testSearchAll2(){
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info(result.getTotalElements());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testSearchReplyCount(){
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDto> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testInsertWithImages(){
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for(int i = 0; i < 3; i++){
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    public void testReadWithImages(){
        // 반드시 존재하는 bno로 확인
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info(("--------------------"));
        for(BoardImage boardImage : board.getImageSet()){
            log.info(boardImage);
        }
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages(){

        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        // 기존의 첨부파일들은 삭제
        board.clearImages();

        // 새로운 첨부파일들
        for(int i = 0; i < 2; i++){
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){
        Long bno = 1L;

        // Reply -> Board로 연결되어 있으므로
        // 직접 지워야 한다.
        replyRepository.deleteByBoard_Bno(bno);

        // Board <-> BoardImage 양방향 연결이고
        // 아래 설정에 의해서 board의 row를 지우면
        // 참조하는 자식 boardImage의 row도 삭제된다.
        //CascadeType.ALL
        //orphanRemoval = true
        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll(){
        for(int i = 1; i <= 100; i++){
            Board board = Board.builder()
                    .title("Title...." + i)
                    .content("Content.." + i)
                    .writer("writer" + i)
                    .build();

            for(int j = 1; j <= 100; j++){
                if(i % 5 == 0){
                    continue;
                }

                board.addImage(UUID.randomUUID().toString(), i+ "file" + j + ".jpg");
            }

            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

//        boardRepository.searchWithAll(null, null, pageable);

        Page<BoardListAllDto> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("-------------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDto -> log.info(boardListAllDto));
    }


}
