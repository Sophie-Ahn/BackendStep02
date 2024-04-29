package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDto boardDto); // 등록: 여러가지 데이터를 입력해야해서 dto를 넣음

    BoardDto readOne(Long bno); // 읽기: 번호만 있으면 그 값에 대해 읽을 수 있기 때문에 bno

    void modify(BoardDto boardDto); // 수정: 여러 데이터를 수정해야해서 dto 사용

    void remove(Long bno); // 삭제: 특정 번호에 대해서 전체적으로 다 삭제하기 때문에 bno

    PageResponseDto<BoardDto> list(PageRequestDto pageRequestDto);

    //댓글의 숫자까지 처리
    PageResponseDto<BoardListReplyCountDto> listWithReplyCount(PageRequestDto pageRequestDto);

    // 게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDto<BoardListAllDto> listWithAll(PageRequestDto pageRequestDto);

    default Board dtoToEntity(BoardDto boardDto){
        Board board = Board.builder()
                .bno(boardDto.getBno())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(boardDto.getWriter())
                .build();

        if(boardDto.getFileNames() != null){
            boardDto.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }

        return board;
    }

    default BoardDto entityToDto(Board board){

        BoardDto boardDto = BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid()+"_" +boardImage.getFileName()).collect(Collectors.toList());

        boardDto.setFileNames(fileNames);

        return boardDto;
    }
}
