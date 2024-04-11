package org.zerock.dao;

import com.sun.tools.javac.comp.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.jdbcex.dao.TodoDao;
import org.zerock.jdbcex.domain.TodoVo;

import java.time.LocalDate;
import java.util.List;

public class TodoDaoTests {
    private TodoDao todoDao;

    // @Test를 하기 전에 미리 호출되어 객체를 준비하는 용도로 사용하는 어노테이션
    @BeforeEach
    public void ready(){
        todoDao = new TodoDao();
    }

    @Test
    public void testTime(){
        System.out.println(todoDao.getTime());
    }

    @Test
    public void testTime2() throws Exception{
        System.out.println(todoDao.getTime2());
    }

    @Test
    public void testInsert() throws Exception{
        TodoVo todoVo = TodoVo.builder()
                .title("Sample title...")
                .dueDate(LocalDate.of(2021, 12, 31))
                .build();

        todoDao.insert(todoVo);
    }

    @Test
    public void testList() throws Exception{
        List<TodoVo> list = todoDao.selectAll();
        list.forEach(vo -> System.out.println(vo));
    }

    @Test
    public void testSelectOne() throws Exception{
        Long tno = 1L; // 반드시 존재하는 번호 사용
        TodoVo vo = todoDao.selectOne(tno);

        System.out.println(vo);
    }

    @Test
    public void testUpdateOne() throws Exception{
        TodoVo todoVo = TodoVo.builder()
                .tno(1L)
                .title("Sample title...zzz")
                .dueDate(LocalDate.of(2025, 12, 31))
                .finished(true)
                .build();

        todoDao.updateOne(todoVo);
    }
}
