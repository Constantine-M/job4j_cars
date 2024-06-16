package ru.job4j.cars.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.controller.PostController;
import ru.job4j.cars.model.File;
import ru.job4j.cars.service.file.SimpleFileService;

/**
 * Данный класс описывает конвертер,
 * который по сути ничего не делает,
 * т.е. внутри нет какой-то логики.
 *
 * Без этого конвертера спринг пытается
 * сконвертировать {@link MultipartFile} в
 * {@link File}, не находит подходящего
 * конвертера и кидает 500 ошибку.
 *
 * А таким образом мы, получается,
 * "обманываем" спринг. Во время
 * публикации поста спринг "преобразовывает"
 * типы и открывает доступ к дальнейшей
 * логике.
 *
 * Вся работа с файлами (их преобразование)
 * на самом деле происходит в
 * {@link PostController #convertFromMultipartFilesToDto}
 * и {@link SimpleFileService}.
 *
 * @author Constantine on 14.05.2024
 */
@Slf4j
public class MultipartFileToFileConverter implements Converter<MultipartFile, File> {

    @Override
    public File convert(MultipartFile multipartFile) {
        return new File();
    }
}
