package ru.job4j.cars.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.job4j.cars.dto.PostFilter;
import ru.job4j.cars.model.Post;

/**
 * Данный класс производит сверку того,
 * что пользователь заполнил все требуемые
 * поля для поиска наших {@link Post}.
 *
 * Если поля не заполнены или заполнены
 * неверно, то результат записывается в
 * {@link BindingResult} и на его основе
 * принимается решение о том, как производить
 * поиск наших {@link Post}.
 *
 * Должны быть провалидированы ВСЕ поля
 * {@link PostFilter} иначе будет падать
 * 500 ошибка!
 *
 * @author Constantine on 05.06.2024
 */
@Component
public class SearchValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PostFilter.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PostFilter filter = (PostFilter) target;
        if (!filter.isFileExists()) {
            errors.rejectValue("fileExists", "only fileExists is false");
        }
        if (filter.getCar() == null) {
            errors.rejectValue("car", "car is null");
        }
        if (filter.getCreatedDaysBefore() <= 0) {
            errors.rejectValue("createdDaysBefore", "createdDaysBefore is less than 1");
        }
        if (filter.getLowestPrice() <= 0) {
            errors.rejectValue("lowestPrice", "lowestPrice is less than 1");
        }
        if (filter.getHighestPrice() <= 0) {
            errors.rejectValue("highestPrice", "highestPrice is less than 1");
        }
        if (!filter.isSold()) {
            errors.rejectValue("sold", "sold is false");
        }
    }
}
