package org.zerock.w1.todo;

import org.zerock.w1.todo.dto.TodoDto;
import org.zerock.w1.todo.sevice.TodoService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="todoListController", urlPatterns = "/todo/list")
public class TodoListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("/todo/list");

        // getList객체를 INSTANCE통해 얻어옴
        List<TodoDto> dtoList = TodoService.INSTANCE.getList();
        // 꺼낸 것을 req로 저장하고 list.jsp에서 list라는 이름으로 꺼낼 수 있게 해놓음
        req.setAttribute("list", dtoList);

        System.out.println("DB로부터 목록을 꺼내어 list.jsp에 전달");

//        RequestDispatcher rd = req.getRequestDispatcher("todoList.jsp");
//        rd.forward(req, resp);
        req.getRequestDispatcher("/WEB-INF/todo/list.jsp").forward(req, resp);
    }
}
