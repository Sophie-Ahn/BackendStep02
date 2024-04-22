package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardDto;
import org.zerock.b01.repository.BoardRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServieImpl implements BoardService{

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDto boardDto) {
        Board board = modelMapper.map(boardDto, Board.class);
        long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDto readOne(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        BoardDto boardDto = modelMapper.map(board, BoardDto.class);

        return boardDto;
    }

    @Override
    public void modify(BoardDto boardDto) {
        Optional<Board> result = boardRepository.findById(boardDto.getBno());

        Board board = result.orElseThrow();

        board.change(boardDto.getTitle(), boardDto.getContent());

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }
}
