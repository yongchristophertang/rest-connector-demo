package com.github.yongchristophertang.demo.controller;

import com.github.yongchristophertang.demo.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Web service controller
 *
 * @author Yong Tang
 * @since 0.1
 */
@RestController
public class DemoController {
    /**
     * Mimic a persistence of persons
     */
    static final Set<Person> persistPersons = new CopyOnWriteArraySet<>();
    private static final Function<Integer, Person> lookupPerson =
        id -> persistPersons.parallelStream().filter(p -> p.getId() == id).findFirst()
            .orElseThrow(() -> new PersonNotFoundException(id));
    /**
     * Internal deterministic person id
     */
    static AtomicInteger id = new AtomicInteger();

    /**
     * GET a person
     *
     * @param id the person's id
     * @return the person
     */
    @RequestMapping("/person/{id}")
    public Person getPerson(@PathVariable("id") int id) {
        return lookupPerson.apply(id);
    }

    /**
     * Store a person
     *
     * @param person person got from request body
     * @return person's id
     */
    @RequestMapping(value = "/persons", method = RequestMethod.POST, consumes = "application/json")
    public int storePerson(@RequestBody Person person) {
        person.setId(id.incrementAndGet());
        persistPersons.add(person);
        return person.getId();
    }

    /**
     * Delete a person
     *
     * @param id person's id
     * @return the deleted person
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE)
    public Person deletePerson(@PathVariable("id") int id) {
        Person person = lookupPerson.apply(id);
        persistPersons.remove(person);
        return person;
    }

    /**
     * Update a person
     *
     * @param id           person's id
     * @param personUpdate updated name
     * @return the updated person
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.PUT)
    public Person updatePerson(@PathVariable("id") int id, @RequestBody(required = false) Person personUpdate) {
        Person person = lookupPerson.apply(id);
        if (personUpdate != null) {
            person.setName(personUpdate.getName());
        }
        return person;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class PersonNotFoundException extends RuntimeException {

        public PersonNotFoundException(int id) {
            super("could not find person '" + id + "'.");
        }
    }
}
