package org.zerock.b01.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        // StTRICT는 ReplyDto <-> Reply 변환이 안됨
        // MatchingStrategies 현상이 발생됨
        // LOOSE : 클래스랑 테이블이랑 컬럼값이랑 필드값을 비교할 때 루즈하게 함
        // STRICT : 클래스랑 테이블이랑 컬럼값이랑 필드값을 비교할 때 엄격하게 함

        return modelMapper;
    }
}