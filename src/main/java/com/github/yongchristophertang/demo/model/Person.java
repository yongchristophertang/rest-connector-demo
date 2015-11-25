package com.github.yongchristophertang.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;

/**
 * Person model
 *
 * @author Yong Tang
 * @since 0.1
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    @ElementCollection(targetClass = String.class)
    private List<String> phone;

    protected Person() {
    }

    public Person(String name, String address, List<String> phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;

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

        if (name != null ? !name.equals(person.name) : person.name != null) {
            return false;
        }
        if (id != null ? !id.equals(person.id) : person.id != null) {
            return false;
        }
        if (address != null ? !address.equals(person.address) : person.address != null) {
            return false;
        }
        return !(phone != null ? !phone.equals(person.phone) : person.phone != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Person{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", address='" + address + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
