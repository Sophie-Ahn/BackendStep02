package org.zerock.springex.mapper;

import org.zerock.springex.domain.TodoVo;
import org.zerock.springex.dto.PageRequestDto;
import sun.jvm.hotspot.debugger.Page;

import java.util.List;

public interface TodoMapper {
    String getTime();

    void insert(TodoVo todoVo);

    List<TodoVo> selectAll();

    TodoVo selectOne(Long tno);

    void delete(Long tno);

    void update(TodoVo todoVo);

    List<TodoVo> selectList(PageRequestDto pageRequestDto);

    int getCount(PageRequestDto pageRequestDto);

}
