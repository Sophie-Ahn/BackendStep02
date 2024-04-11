package org.zerock.jdbcex.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder // 빌더 패턴 사용. 초기화시 필드초기화처럼 가독성 증가 효과
//@Data // (getter, setter, toString, equals, hashcode 모두 오버라이딩)
@ToString // 필드 정보 보기
@NoArgsConstructor // 매개변수 없는 생성자
@AllArgsConstructor // 모든 필드를 초기화할 수 있는 생성자 ModelMapper로 Dto <-> Vo 변환
public class TodoDto {
    private Long tno;
    private String title;
    private LocalDate dueDate;
    private boolean finished;

}
