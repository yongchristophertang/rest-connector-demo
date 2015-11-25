package com.github.yongchristophertang.demo.controller;

import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.demo.persist.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web service controller using mysql database repository
 *
 * @author Yong Tang
 * @since 0.1
 */
@RestController
@RequestMapping("/demo")
public class DemoController {


    private final PersonRepository personRepository;

    @Autowired
    public DemoController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * GET a person
     *
     * @param id the person's id
     * @return the person
     */
    @RequestMapping("/person/{id}")
    public Person getPerson(@PathVariable("id") long id) {
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
        return personRepository.save(person);
    }

    /**
     * Get persons by name.
     *
     * @param name the person's name
     * @return list of person with the request name
     */
    @RequestMapping("/person")
    public List<Person> getPersonByName(@RequestParam(value = "name",required = true) String name) {
        List<Person> persons = personRepository.findByName(name);
        if (persons.size() == 0) {
            throw new PersonNotFoundException(name);
        }
        return persons;
    }

    /**
     * Delete a person
     *
     * @param id person's id
     * @return the deleted person
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE)
    public Person deletePerson(@PathVariable("id") long id) {
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
        personUpdate.setId(id);
        return personRepository.save(personUpdate);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class PersonNotFoundException extends RuntimeException {

        public PersonNotFoundException(long id) {
            super("could not find person with id '" + id + "'.");
        }

        public PersonNotFoundException(String name) {
            super("could not find person with name '" + name + "'.");
        }
    }

}
