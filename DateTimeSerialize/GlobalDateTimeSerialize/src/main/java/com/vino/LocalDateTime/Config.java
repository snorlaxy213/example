package com.vino.LocalDateTime;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class Config {

    // 时间格式化
    private static final DateTimeFormatter INPUTFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static final DateTimeFormatter OUTPUTFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);

//    //基于 @DateTimeFormat配置 Step-1
//    @Bean
//    public Converter<String, LocalDateTime> localDateConverter() {
//        return new Converter<String, LocalDateTime>() {
//            @Override
//            public LocalDateTime convert(String source) {
//                return LocalDateTime.parse(source, FORMATTER);
//            }
//        };
//    }

    //基于 @JsonFormat配置 Step-1
    @Bean
    public Formatter<LocalDateTime> localDateFormatter() {
        return new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(String text, Locale locale) {
                return LocalDateTime.parse(text, INPUTFORMAT);
            }

            @Override
            public String print(LocalDateTime object, Locale locale) {
                return object.format(INPUTFORMAT);
            }
        };
    }

    // Step-2
    //完成Step-1和Step-2后，LocalDateTime无论是通过@RequestParam传输的普通参数，还是@RequestBody的对象，都可以接受到
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                // 反序列化
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(INPUTFORMAT))
                // 序列化
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(OUTPUTFORMAT));
    }
}
