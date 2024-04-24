package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.PageRequestDto;
import org.zerock.b01.dto.PageResponseDto;
import org.zerock.b01.dto.ReplyDto;
import org.zerock.b01.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public ReplyDto read(Long rno) {

        Optional<Reply> replyOptional = replyRepository.findById(rno);

        Reply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDto.class);
    }

    @Override
    public void modify(ReplyDto replyDto) {
        Optional<Reply> replyOptional = replyRepository.findById(replyDto.getRno());

        Reply reply = replyOptional.orElseThrow();

        reply.changeText(replyDto.getReplyText()); // 댓글의 내용만 수정 가능

        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDto<ReplyDto> getListOfBoard(Long bno, PageRequestDto pageRequestDto) {

        Pageable pageable = PageRequest.of(pageRequestDto.getPage() <= 0 ? 0 :
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("rno").ascending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        List<ReplyDto> dtoList = result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDto.class))
                .collect(Collectors.toList());

        return PageResponseDto.<ReplyDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
