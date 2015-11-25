package com.github.yongchristophertang.demo.controller;

import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.demo.persist.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private static final Function<Long, Person> lookupPerson =
        id -> persistPersons.parallelStream().filter(p -> p.getId() == id).findFirst()
            .orElseThrow(() -> new PersonNotFoundException(id));
    /**
     * Internal deterministic person id
     */
    static AtomicInteger id = new AtomicInteger();

    @Autowired
    private PersonRepository personRepository;

    /**
     * GET a person
     *
     * @param id the person's id
     * @return the person
     */
    @RequestMapping("/person/{id}")
    public Person getPerson(@PathVariable("id") long id) {
//        return lookupPerson.apply(id);
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    /**
     * Store a person
     *
     * @param person person got from request body
     * @return person's id
     */
    @RequestMapping(value = "/persons", method = RequestMethod.POST, consumes = "application/json")
    public Person storePerson(@RequestBody Person person) {
//        person.setId(id.incrementAndGet());
//        persistPersons.add(person);
        return personRepository.save(person);
//        return person.getId();
    }

    @RequestMapping("/person")
    public List<Person> getPersonByName(@RequestParam(value = "name",required = true) String name) {
        return personRepository.findByName(name);
    }

    /**
     * Delete a person
     *
     * @param id person's id
     * @return the deleted person
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE)
    public Person deletePerson(@PathVariable("id") long id) {
//        Person person = lookupPerson.apply(id);
//        persistPersons.remove(person);
        return personRepository.findAndDeleteById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    /**
     * Update a person
     *
     * @param id           person's id
     * @param personUpdate updated name
     * @return the updated person
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.PUT)
    public Person updatePerson(@PathVariable("id") long id, @RequestBody(required = false) Person personUpdate) {
//        Person person = lookupPerson.apply(id);
//        if (personUpdate != null) {
//            personUpdate.setId(person.getId());
//            persistPersons.remove(person);
//            persistPersons.add(personUpdate);
//        }
        personUpdate.setId(id);
        return personRepository.save(personUpdate);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class PersonNotFoundException extends RuntimeException {

        public PersonNotFoundException(long id) {
            super("could not find person '" + id + "'.");
        }
    }
}
