package ru.job4j.cars.repository.engine;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.Engine;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Constantine on 23.03.2024
 */
@SpringBootTest
class EngineRepositoryImplTest {

    @Autowired
    private EngineRepository engineRepository;

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    @BeforeEach
    void clearTable() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE engine RESTART IDENTITY");
            session.getTransaction().commit();
        }
    }

    @Disabled
    @Test
    void whenSaveEngineThenGetTheSame() {
        var engine = Engine.builder()
                .name("Inline-4 Turbo 1.5L")
                .build();
        var createdEngine = engineRepository.save(engine);
        assertThat(engineRepository.findById(1)).usingRecursiveComparison().isEqualTo(engine);
    }
}