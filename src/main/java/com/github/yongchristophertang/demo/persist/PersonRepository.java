package com.github.yongchristophertang.demo.persist;


import com.github.yongchristophertang.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Persistence repository for person objects
 *
 * @author Yong Tang
 * @since 0.1
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByName(String name);

    Optional<Person> findById(long id);

    Optional<Person> findAndDeleteById(long id);
}
