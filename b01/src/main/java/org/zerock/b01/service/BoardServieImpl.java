package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServieImpl implements BoardService{

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDto boardDto) {
//        Board board = modelMapper.map(boardDto, Board.class);
//        long bno = boardRepository.save(board).getBno();
//
//        return bno;

        Board board = dtoToEntity(boardDto);
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDto readOne(Long bno) {

        // board_image까지 조인 처리되는 findByWithImages()를 이용
//        Optional<Board> result = boardRepository.findById(bno);
        Optional<Board> result = boardRepository.findByIdWithImages(bno);

        Board board = result.orElseThrow();

//        BoardDto boardDto = modelMapper.map(board, BoardDto.class);
        BoardDto boardDto = entityToDto(board);

        return boardDto;
    }

    @Override
    public void modify(BoardDto boardDto) {
        Optional<Board> result = boardRepository.findById(boardDto.getBno());

        Board board = result.orElseThrow();

        board.change(boardDto.getTitle(), boardDto.getContent());

        // 첨부파일의 처리
        board.clearImages();

        if(boardDto.getFileNames() != null){
            for(String fileName : boardDto.getFileNames()){
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDto<BoardDto> list(PageRequestDto pageRequestDto) {

        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDto> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDto.class)).collect(Collectors.toList());

        return PageResponseDto.<BoardDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDto<BoardListReplyCountDto> listWithReplyCount(PageRequestDto pageRequestDto) {
        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable("bno");

        Page<BoardListReplyCountDto> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDto.<BoardListReplyCountDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDto<BoardListAllDto> listWithAll(PageRequestDto pageRequestDto) {
        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable("bno");

        Page<BoardListAllDto> result = boardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDto.<BoardListAllDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

}
