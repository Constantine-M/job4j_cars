package ru.job4j.cars.predicate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.job4j.cars.dto.PostFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Данный класс описывает своеобразный
 * билдер предикатов - здесь будут собраны
 * все предикаты для нашего фильтра
 * {@link PostFilter}.
 *
 * Данную заготовку оставил до момента
 * пока не разберусь как сюда добавить
 * примитивы.
 *
 * @author Constantine on 04.06.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {

    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicate builder() {
        return new QPredicate();
    }

    /**
     * Метод добавляет предикаты.
     *
     * В этом же методе мы делаем простую
     * проверку на null. Таким образом, если
     * поле пустое, то значение в этом поле
     * не будет добавлено в предикат и не
     * будет учитываться при поиске.
     */
    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    /**
     * Данный метод собирает все предикаты
     * в один предикат через AND (логическое "И").
     */
    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicates);
    }
}
