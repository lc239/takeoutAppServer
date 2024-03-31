package com.lc.takeoutApp.config;

import com.lc.takeoutApp.converter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories //开启spring data的Repository
@Configuration
public class R2dbcConfiguration {

    @Autowired
    AddressReadingConverter addressReadingConverter;

    @Autowired
    AddressWritingConverter addressWritingConverter;

    @Autowired
    AddressArrReadingConverter addressArrReadingConverter;

    @Bean
    public R2dbcCustomConversions conversions(){
        //添加转换器
        return R2dbcCustomConversions.of(
                MySqlDialect.INSTANCE,
                addressReadingConverter,
                addressWritingConverter,
                addressArrReadingConverter,
                new CategoryArrReadingConverter(),
                new OrderedMenuArrReadingConverter(),
                new ListConverter(),
                new JsonArrWritingConverter<>()
        );
    }
}
