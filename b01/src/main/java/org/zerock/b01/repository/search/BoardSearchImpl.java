package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardListReplyCountDto;

import java.util.List;

// 반드시 상속받은 인터페이스명 + Impl
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl(){
        super(Board.class);
    }

    // 우리는 BoardRepository 내부에서 호출할 메서드를
    // QueryDsl로 구현한다.
    @Override
    public Page<Board> search1(Pageable pageable) {
        // 우리가 작업하는 Querydsl 프로그램이 -> JPQL로 변환하기 위해 사용
        QBoard board = QBoard.board;

        /*
        * Querydsl을 통해서 1단계씩 sql문을 작성해나간다.
        * */

        // FROM board
        JPQLQuery<Board> query = from(board);

        // WHERE title LIKE '%1%'
        query.where(board.title.contains("1"));

        // paging
        // ORDER BY bno DESC LIMIT 1, 10;
        this.getQuerydsl().applyPagination(pageable, query);

        // SELECR * FROM board WHERE title LIKE '%1%';
        List<Board> list = query.fetch();

        // Primary key로 설정해서 전체를 확실하게 알 수 있음
        // SELECR COUNT(bno) FROM board WHERE title LIKE '%1%';
        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        // Querydsl -> JQPL 변환하기 위한 역할
        QBoard board = QBoard.board;

        // FROM board
        JPQLQuery<Board> query = from(board);

        if((types != null && types.length > 0) && keyword != null) {
            // 괄호의 역할 (
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            /*
            * title Like '%1%'
            * OR content LIKE '%1%'
            * OR writer LIKE '%1%'
            * */

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }

            // ) 괄호의 역할

            /*
            * WHERE (
                  title Like '%1%'
                  OR content LIKE '%1%'
                  OR writer LIKE '%1%'
            * )
            * */

            query.where(booleanBuilder);
        }

        // AND bno > 0L
        query.where(board.bno.gt(0L));

        // ORDER BY bno DESC LIMIT 1, 10;
        this.getQuerydsl().applyPagination(pageable, query);

        /* SELECT *
            FROM board
            WHERE (
                title LIKE '%1%'
                OR content '%1%'
                OR writer LIKE '%1%'
            )
            AND bno > 0L
            ORDER BY bno DESC LIMIT 1, 10;
         */
        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDto> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        // Querydsl -> JPQL로 변환
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        // 왼쪽 테이블인 board에 기준을 맞춰서 board는 다 출력해줘
        // board의 row(행)을 댓글이 존재하지 않아도 모두 출력해줘
        // FROM board LEFT OUTER JOIN reply ON reply.board_bno = board.bno

        // FROM board;
        JPQLQuery<Board> query = from(board);

        // LEFT OUTER JOIN reply ON reply.board_bno = board.bno
        query.leftJoin(reply).on(reply.board.eq(board));

        // GROUP BY board.bno, board.title, board.writer
        // GROUP BY board의 컬럼들
        query.groupBy(board);

        // OR가 AND보다 연산자 우선순위가 낮기 때문에 OR조건들은 ()괄호로 묶어준다.
        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type : types) {
                switch(type){
                    case "t":
                        // OR board.title LIKE '%:keyword%'
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        // OR board.content LIKE '%:keyword%'
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        // OR board.writer LIKE '%:keyword%'
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }

            /*
            * WHERE (
            * board.title LIKE '%:keyword%'
            * OR
            * board.content LIKE '%:keyword%'
            * OR
            * board.writer LIKE '%:keyword%'
            * )
            * */
            query.where(booleanBuilder); // )
        }

        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDto> dtoQuery = query.select(
                Projections.bean(BoardListReplyCountDto.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDto> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }


}
