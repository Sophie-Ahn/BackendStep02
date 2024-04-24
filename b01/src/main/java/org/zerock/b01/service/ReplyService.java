package org.zerock.b01.service;

import org.zerock.b01.dto.PageRequestDto;
import org.zerock.b01.dto.PageResponseDto;
import org.zerock.b01.dto.ReplyDto;

public interface ReplyService {
    Long register(ReplyDto replyDto);

    ReplyDto read(Long rno);

    void modify(ReplyDto replyDto);

    void remove(Long rno);

    PageResponseDto<ReplyDto> getListOfBoard(Long bno, PageRequestDto pageRequestDto);
}
