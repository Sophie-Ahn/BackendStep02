package org.zerock.springex.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/*
* jsp에서 bootstrap의 pagination 컴포넌트를 사용하는데
* 그 때 이 dto를 받아서 pagenation을 구성한다.
* */
/*
* 현재 E에 TodoDto가 전달된다.
* 하지만 다른 업무/목적으로 얼마든지 많은 테이블과 dto가 생성될 수 있다.
* 이 때 dtoList를 제외한 나머지 값들은 어떤 page에서도 pagination에 꼭 필요한 데이터이다.
* dtoList만 할일 페이지 업무일 때는 TodoDto
*                   예를 들어 Project 업무일 때는 ProjectDto
*                   쇼핑 구매 업무일 때는 OrderDto 등의 list가 필요하므로
*                   Generic으로 만들면 1번 만들어 놓고 재사용이 가능하다
* */
@Getter
@ToString
public class PageResponseDto<E> {
    private int page; // 현재 페이지 정보
    private int size; // 현재 보여야 할 row의 크기
    private int total; // 테이블의 전체 row 크기

    private int start; // 시작 페이지 번호
    private int end; // 끝 페이지 번호

    private boolean prev; // 이전 페이지 존재하면 true, prev버튼이 보인다. false면 감춘다.
    private boolean next; // 다음 페이지 존재 여부

    private List<E> dtoList; // 테이블에 보여줘야 할 row데이터의 list

    @Builder(builderMethodName = "withAll")
    public PageResponseDto(PageRequestDto pageRequestDto, List<E> dtoList, int total){
        this.page = pageRequestDto.getPage();
        this.size = pageRequestDto.getSize();

        this.total = total;
        this.dtoList = dtoList;

        // 내 page번호에 따라 pagination의 끝번호를 구하고 -9를 해주면 시작번호를 구할 수 있다.
        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;

        // 9는 pagination범위 - 1
        this.start = this.end - 9;

        // pagination의 가장 끝 번호
        int last = (int)(Math.ceil((this.total/(double)size)));

        /*
        * 위에서 end계산은 10단위로 무조건 맞춘 것이기 때문에
        * 실제 마지막 페이지 번호와 확인해서
        * 마지막 페이지인 경우는 end > last보다 크게 되므로
        * end = last로 해줘야
        * 마지막 페이지인 경우 pagination이 알맞게 나오게 된다.
        * */
        /*
        *
        * */
        this.end = this.end > last ? last : this.end;

        // 1~10 pagination이 아니고, 그 이상이라는 의미이므로 이전 페이지가 존재
        this.prev = this.start > 1;

        // 현재 pagination이 보여주는 범위가 마지막이 아니므로 다음 페이지가 존재
        this.next = this.total > this.end * this.size;
    }
}
