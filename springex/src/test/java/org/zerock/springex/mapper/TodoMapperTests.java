package org.zerock.springex.mapper;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.springex.domain.TodoVo;
import org.zerock.springex.dto.PageRequestDto;
import org.zerock.springex.dto.TodoDto;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class TodoMapperTests {
    @Autowired(required = false)
    private TodoMapper todoMapper;

    @Test
    public void testGetTime(){
        log.info(todoMapper.getTime());
    }

    @Test
    public void testInsert(){
        TodoVo todoVo = TodoVo.builder()
                .title("스프링테스트")
                .dueDate(LocalDate.of(2022,10,10))
                .writer("user00")
                .build();

        todoMapper.insert(todoVo);
    }

    @Test
    public void testSelectAll(){
        List<TodoVo> voList = todoMapper.selectAll();

        voList.forEach(vo -> log.info(vo));
    }

    @Test
    public void testSelectOne() {
        TodoVo todoVo = todoMapper.selectOne(3L);

        log.info(todoVo);
    }

    @Test
    public void testSelectList(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(1)
                .size(10)
                .build();

        List<TodoVo> voList = todoMapper.selectList(pageRequestDto);

        voList.forEach(vo -> log.info(vo));
    }

    @Test
    public void testSelectSearch(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(1)
                .size(10)
                .types(new String[]{"t", "w"})
                .keyword("스프링")
//                .finished(true)
                .from(LocalDate.of(2021,12,01))
                .to(LocalDate.of(2022,12,31))
                .build();

        List<TodoVo> voList = todoMapper.selectList(pageRequestDto);

        voList.forEach(vo -> log.info(vo));

        log.info(todoMapper.getCount(pageRequestDto));
    }
}
