package ru.job4j.cars;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

public class UserUsage {

    public static void main(String[] args) {
        String ls = System.lineSeparator();
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var userRepository = new UserRepository(sessionFactory);
            var user = new User();
            user.setLogin("admin");
            user.setPassword("admin");
            userRepository.create(user);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("FIND ALL ORDER BY ID - FIND ALL ORDER BY ID - FIND ALL ORDER BY ID - FIND ALL ORDER BY ID");
            System.out.println("---------------------------------------------------------------------------------------------");
            userRepository.findAllOrderById()
                    .forEach(System.out::println);
            System.out.println("FIND ALL ORDER BY ID - FIND ALL ORDER BY ID - FIND ALL ORDER BY ID - FIND ALL ORDER BY ID" + ls);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("FIND BY LIKE LOGIN - FIND BY LIKE LOGIN - FIND BY LIKE LOGIN - FIND BY LIKE LOGIN");
            System.out.println("---------------------------------------------------------------------------------------------");
            userRepository.findByLikeLogin("e")
                    .forEach(System.out::println);
            System.out.println("FIND BY LIKE LOGIN - FIND BY LIKE LOGIN - FIND BY LIKE LOGIN - FIND BY LIKE LOGIN" + ls);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("FIND BY ID USER - FIND BY ID USER - FIND BY ID USER - FIND BY ID USER - FIND BY ID USER");
            System.out.println("---------------------------------------------------------------------------------------------");
            userRepository.findById(user.getId())
                    .ifPresent(System.out::println);
            System.out.println("FIND BY ID USER - FIND BY ID USER - FIND BY ID USER - FIND BY ID USER - FIND BY ID USER" + ls);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("FIND BY LOGIN - FIND BY LOGIN - FIND BY LOGIN - FIND BY LOGIN - FIND BY LOGIN");
            System.out.println("---------------------------------------------------------------------------------------------");
            userRepository.findByLogin("admin")
                    .ifPresent(System.out::println);
            System.out.println("FIND BY LOGIN - FIND BY LOGIN - FIND BY LOGIN - FIND BY LOGIN - FIND BY LOGIN" + ls);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER");
            System.out.println("---------------------------------------------------------------------------------------------");
            user.setPassword("password");
            userRepository.update(user);
            userRepository.findById(user.getId())
                    .ifPresent(System.out::println);
            System.out.println("UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER - UPDATE USER" + ls);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("DELETE USER - DELETE USER - DELETE USER - DELETE USER - DELETE USER - DELETE USER");
            System.out.println("---------------------------------------------------------------------------------------------");
            userRepository.delete(user.getId());
            System.out.println("DELETE USER - DELETE USER - DELETE USER - DELETE USER - DELETE USER - DELETE USER" + ls);
            userRepository.findAllOrderById()
                    .forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
