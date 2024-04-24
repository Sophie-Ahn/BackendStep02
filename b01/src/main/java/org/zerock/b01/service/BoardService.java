package org.zerock.b01.service;

import org.zerock.b01.dto.BoardDto;
import org.zerock.b01.dto.BoardListReplyCountDto;
import org.zerock.b01.dto.PageRequestDto;
import org.zerock.b01.dto.PageResponseDto;

public interface BoardService {
    Long register(BoardDto boardDto); // 등록: 여러가지 데이터를 입력해야해서 dto를 넣음

    BoardDto readOne(Long bno); // 읽기: 번호만 있으면 그 값에 대해 읽을 수 있기 때문에 bno

    void modify(BoardDto boardDto); // 수정: 여러 데이터를 수정해야해서 dto 사용

    void remove(Long bno); // 삭제: 특정 번호에 대해서 전체적으로 다 삭제하기 때문에 bno

    PageResponseDto<BoardDto> list(PageRequestDto pageRequestDto);

    //댓글의 숫자까지 처리
    PageResponseDto<BoardListReplyCountDto> listWithReplyCount(PageRequestDto pageRequestDto);
}
