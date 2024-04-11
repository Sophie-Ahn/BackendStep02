package org.zerock.w2.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.w2.dao.TodoDao;
import org.zerock.w2.domain.TodoVo;
import org.zerock.w2.dto.TodoDto;
import org.zerock.w2.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
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

//        System.out.println("todoVo: " + todoVo);
        log.info(todoVo);

        dao.insert(todoVo); // int를 반환하므로 이를 이용해서 예외처리도 가능
    }

    public List<TodoDto> listAll() throws Exception {
        List<TodoVo> voList = dao.selectAll();

        log.info("voList -------------------------");
        log.info(voList);

        List<TodoDto> dtoList = voList.stream()
                .map(vo -> modelMapper.map(vo, TodoDto.class))
                .collect(Collectors.toList());

        return dtoList;
    }

    public TodoDto get(Long tno) throws Exception {
        log.info("tno: " + tno);
        TodoVo todoVo = dao.selectOne(tno);
        TodoDto todoDto = modelMapper.map(todoVo, TodoDto.class);

        return todoDto;
    }

    public void remove(Long tno) throws Exception {
        log.info("tno: " + tno);
        dao.deleteOne(tno);
    }

    public void modify(TodoDto todoDto) throws Exception {
        log.info("todoDto: " + todoDto);
        TodoVo todoVo = modelMapper.map(todoDto, TodoVo.class);

        dao.updateOne(todoVo);
    }
}
