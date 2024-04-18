package org.zerock.springex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.springex.domain.TodoVo;
import org.zerock.springex.dto.PageRequestDto;
import org.zerock.springex.dto.PageResponseDto;
import org.zerock.springex.dto.TodoDto;
import org.zerock.springex.mapper.TodoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    /*
    * lombok이 제공하는 @RequiredArgsConstructor에 의해
    * 생성자가 생성되고 Spring container에서
    * final로 된 타입과 일치하는 bean을 찾아서 주입한다.
    * */
    private final TodoMapper todoMapper;
    private final ModelMapper modelMapper;

    @Override
    public void register(TodoDto todoDto) {
        log.info(modelMapper);

        // dto -> vo
        TodoVo todoVo = modelMapper.map(todoDto, TodoVo.class);

        log.info(todoVo);

        // Dao에 해당하는 todoMapper를 사용하여 DBMS와 통신한다.
        todoMapper.insert(todoVo);
    }

    // 사용하지 않는다.
//    @Override
//    public List<TodoDto> getAll() {
//        List<TodoDto> dtoList = todoMapper.selectAll().stream()
//                .map(vo -> modelMapper.map(vo, TodoDto.class))
//                .collect(Collectors.toList());
//
//        return dtoList;
//    }

    @Override
    public TodoDto getOne(Long tno) {
        TodoVo todoVo = todoMapper.selectOne(tno);

        TodoDto todoDto = modelMapper.map(todoVo, TodoDto.class);

        return todoDto;
    }

    @Override
    public void remove(Long tno) {
        todoMapper.delete(tno);
    }

    @Override
    public void modify(TodoDto todoDto) {
        TodoVo todoVo = modelMapper.map(todoDto, TodoVo.class);

        todoMapper.update(todoVo);
    }

    @Override
    public PageResponseDto<TodoDto> getList(PageRequestDto pageRequestDto) {
        List<TodoVo> voList = todoMapper.selectList(pageRequestDto);
        List<TodoDto> dtoList = voList.stream()
                .map(vo -> modelMapper.map(vo, TodoDto.class))
                .collect(Collectors.toList());

        int total = todoMapper.getCount(pageRequestDto);

        PageResponseDto<TodoDto> pageResponseDto = PageResponseDto.<TodoDto>withAll()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDto(pageRequestDto)
                .build();

        return pageResponseDto;
    }
}
