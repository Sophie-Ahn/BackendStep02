package org.zerock.jdbcex.service;

import org.modelmapper.ModelMapper;
import org.zerock.jdbcex.dao.TodoDao;
import org.zerock.jdbcex.domain.TodoVo;
import org.zerock.jdbcex.dto.TodoDto;
import org.zerock.jdbcex.util.MapperUtil;

public enum TodoService {
    INSTANCE;

    private TodoDao dao;
    private ModelMapper modelMapper;

    TodoService() {
        dao = new TodoDao();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public void register(TodoDto todoDto) throws Exception {
        TodoVo todoVo = modelMapper.map(todoDto, TodoVo.class);

        System.out.println("todoVo: " + todoVo);

        dao.insert(todoVo); // int를 반환하므로 이를 이용해서 예외처리도 가능
    }
}
