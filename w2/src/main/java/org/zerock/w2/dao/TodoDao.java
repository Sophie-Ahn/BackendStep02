package org.zerock.w2.dao;

import lombok.Cleanup;
import org.zerock.w2.domain.TodoVo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoDao {

    public String getTime(){
        String now = null;


        // try-with-Resources
        // try()안에서 생성한 객체는 try이 종료될 때, 자동으로 close가 호출되므로
        // finally구문을 사용하지 않아도 된다. 코드를 간결하게 하려는 목적
        try(Connection connection = ConnectionUtil.INSTANCE.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select now()");
            ResultSet resultSet = preparedStatement.executeQuery();
        ){
            resultSet.next();
            now = resultSet.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return now;
    }

    public String getTime2() throws Exception{
        String now = null;

        /*
        * lombok에서 제공해주는 @cleanup을 사용하면
        * 메서드가 종료될 때 자동으로 close()가 된다.
        * */
        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        now = resultSet.getString(1);

        return now;
    }

    public void insert(TodoVo vo) throws Exception{
        String sql = "insert into tbl_todo(title, dueDate, finished) values(?, ?, ?)";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());

        preparedStatement.executeUpdate();
    }

    public List<TodoVo> selectAll() throws Exception {
        String sql = "select * from tbl_todo";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        List<TodoVo> list = new ArrayList<>();

        while (resultSet.next()){
            TodoVo vo = TodoVo.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    .dueDate(resultSet.getDate("duedate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();

            list.add(vo);
        }

        return list;
    }

    public TodoVo selectOne(Long tno) throws Exception {
        String sql = "select * from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        TodoVo vo = TodoVo.builder()
                .tno(resultSet.getLong("tno"))
                .title(resultSet.getString("title"))
                .dueDate(resultSet.getDate("duedate").toLocalDate())
                .finished(resultSet.getBoolean("finished"))
                .build();

        return vo;
    }

    public void deleteOne(Long tno) throws Exception {
        String sql = "delete from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);
        preparedStatement.executeUpdate();
    }

    public void updateOne(TodoVo todoVo) throws Exception {
        String sql = "update tbl_todo set title = ?, dueDate=?, finished=? where tno=?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, todoVo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(todoVo.getDueDate()));
        preparedStatement.setBoolean(3, todoVo.isFinished());
        preparedStatement.setLong(4, todoVo.getTno());

        preparedStatement.executeUpdate();
    }
}
