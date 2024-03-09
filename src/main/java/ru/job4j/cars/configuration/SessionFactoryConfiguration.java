package ru.job4j.cars.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Данный класс описывает настройки
 * {@link org.hibernate.SessionFactory}
 * для нашего приложения.
 *
 * В приложении будет использоваться один
 * {@link org.hibernate.SessionFactory}.
 *
 * @author Constantine on 05.03.2024
 */
@Configuration
public class SessionFactoryConfiguration {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
}
