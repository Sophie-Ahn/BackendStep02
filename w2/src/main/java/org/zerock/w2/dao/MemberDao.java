package org.zerock.w2.dao;

import lombok.Cleanup;
import org.zerock.w2.domain.MemberVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDao {
    public MemberVo getWithPassword(String mid, String mpw) throws Exception {
        String sql = "SELECT mid, mpw, mname from tbl_member WHERE mid =? AND mpw=?";

        MemberVo memberVo = null;

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, mid);
        preparedStatement.setString(2, mpw);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        memberVo = MemberVo.builder()
                .mid(resultSet.getString(1))
                .mpw(resultSet.getString(2))
                .mname(resultSet.getString(3))
                .build();

        return memberVo;
    }
}
