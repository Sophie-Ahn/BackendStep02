package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.ReplyDto;
import org.zerock.b01.repository.ReplyRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDto replyDto) {
        Reply reply = modelMapper.map(replyDto, Reply.class);

        Long rno = replyRepository.save(reply).getRno();

        return rno;
    }
}
