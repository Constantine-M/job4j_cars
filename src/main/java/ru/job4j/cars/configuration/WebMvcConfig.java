package ru.job4j.cars.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.job4j.cars.util.MultipartFileToFileConverter;

/**
 * Данный конфигурационный класс
 * позволяет зарегистрировать наш
 * "конвертер" {@link MultipartFileToFileConverter}
 * в Spring context.
 *
 * @author Constantine on 14.05.2024
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MultipartFileToFileConverter());
    }
}
