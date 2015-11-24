package com.github.yongchristophertang.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Person model
 *
 * @author Yong Tang
 * @since 0.1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    private String name;
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }

        Person person = (Person) o;

        if (id != person.id) {
            return false;
        }
        return !(name != null ? !name.equals(person.name) : person.name != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
            "name='" + name + '\'' +
            ", id='" + id + '\'' +
            '}';
    }
}
