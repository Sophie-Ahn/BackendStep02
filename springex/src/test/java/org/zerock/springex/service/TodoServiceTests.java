package org.zerock.springex.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.springex.domain.TodoVo;
import org.zerock.springex.dto.PageRequestDto;
import org.zerock.springex.dto.PageResponseDto;
import org.zerock.springex.dto.TodoDto;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class TodoServiceTests {
    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister() {
        TodoDto todoDto = TodoDto.builder()
                .title("Test..............")
                .dueDate(LocalDate.now())
                .writer("user")
                .build();

        todoService.register(todoDto);
    }

    @Test
    public void testPaging(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(10).build();

        PageResponseDto<TodoDto> pageResponseDto = todoService.getList(pageRequestDto);

        log.info(pageResponseDto);

        pageResponseDto.getDtoList().stream().forEach(todoDto -> log.info(todoDto));
    }

}
