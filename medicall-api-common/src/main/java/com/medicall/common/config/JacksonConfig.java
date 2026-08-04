package com.medicall.common.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/**
 * 클라이언트와의 날짜/시간 표현을 고정한다.
 * 기본 설정은 나노초가 붙어("2026-08-11T21:27:35.114753") 클라이언트 파싱이 흔들린다.
 */
@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer dateTimeFormatCustomizer() {
        return builder -> {
            builder.serializers(
                    new LocalDateTimeSerializer(DATE_TIME),
                    new LocalDateSerializer(DATE),
                    new LocalTimeSerializer(TIME)
            );
            builder.deserializers(
                    new LocalDateTimeDeserializer(DATE_TIME),
                    new LocalDateDeserializer(DATE),
                    new LocalTimeDeserializer(TIME)
            );
        };
    }
}
