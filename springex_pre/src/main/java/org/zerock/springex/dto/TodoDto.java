package org.zerock.springex.dto;

import lombok.*;

import java.time.LocalDate;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {
    private long tno;
    private String title;
    private LocalDate dueDate;
    private boolean finished;
    private String wrtier; // 새로 추가됨
}
