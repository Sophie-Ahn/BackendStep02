package org.zerock.springex.service;

import org.zerock.springex.dto.TodoDto;

import java.util.List;

public interface TodoService {
    void register(TodoDto todoDto);

    List<TodoDto> getAll();

    TodoDto getOne(Long tno);

    void remove(Long tno);

    void modify(TodoDto todoDto);

}
