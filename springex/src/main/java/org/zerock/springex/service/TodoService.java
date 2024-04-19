package org.zerock.springex.service;

import org.zerock.springex.dto.PageRequestDto;
import org.zerock.springex.dto.PageResponseDto;
import org.zerock.springex.dto.TodoDto;

import java.util.List;

public interface TodoService {
    void register(TodoDto todoDto);

    // 테이블의 데이터를 모두 가져와라
//    List<TodoDto> getAll();

    // 해당 페이지, 해당 사이즈만큼만 가져와라
    PageResponseDto<TodoDto> getList(PageRequestDto pageRequestDto);

    TodoDto getOne(Long tno);

    void remove(Long tno);

    void modify(TodoDto todoDto);

}
