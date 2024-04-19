package org.zerock.springex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;

/*
* url 요청이 들어왔을 때, 함께 넘어오는
* page, size값을 저장하기 위한 클래스
* */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {

    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1; // 페이지 번호

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    @Positive
    private int size = 10; // 페이지 당 보여줄 정보의 크기

    private String link;

    private String[] types;
    private String keyword;
    private boolean finished;
    private LocalDate from;
    private LocalDate to;

    // 해당 페이지 이전에 몇개를 건너띄어야 하는지 계산하는 메서드
    public int getSkip(){
        return (page-1) * 10;
    }

    /*
    * /todo/list에서 /todo/read, /todo/modify등으로 이동했다가
    * 다시 /todo/list로 돌아올 때 현재 내 페이지로 돌아오도록 하기 위해
    * 이 링크정보를 붙여서 url이동을 하는 용도로 사용한다.
    * */
    public String getLink(){
        StringBuilder builder = new StringBuilder();
        builder.append("page=" + this.page);
        builder.append("&size=" + this.size);

        if (finished){
            builder.append("&finisehd=on");
        }

        if (types != null && types.length > 0){
            for(int i = 0; i <types.length; i++){
                builder.append("&types=" + types[i]);
            }
        }

        if(keyword != null){
            try{
                builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(from != null){
            builder.append("&from=" + from.toString());
        }

        if(to != null) {
            builder.append("&to=" + to.toString());
        }

        return builder.toString();
    }

    public boolean checkType(String type){
        if(types == null || types.length == 0) {
            return false;
        }
        return Arrays.stream(types).anyMatch(type::equals);
    }
}
