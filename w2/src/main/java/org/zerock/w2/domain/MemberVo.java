package org.zerock.w2.domain;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVo {
    private String mid;
    private String mpw;
    private String mname;
}
