package com.github.yongchristophertang.demo.examples;

import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.engine.java.LoggerProxyFactory;
import com.github.yongchristophertang.engine.java.handler.JsonAssertion;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test case to demonstrate logger proxy and assertion for Java client API
 *
 * @author Yong Tang
 * @since 0.1
 */
public class Demo3JavaApiLogProxy {
    @Test
    public void testJavaLoggerProxyAndAssertions() {
        ITest<Person> testClient = Person::new;

        ITest clientProxy = LoggerProxyFactory.newProxyFactory(testClient).buildProxy();
        testClient.demoAPI("name", "address", Arrays.asList("12345"));
        Person person = (Person) clientProxy.demoAPI("name proxy", "address proxy", Arrays.asList("54321"));
        new JsonAssertion(person.toString()).pathMatch("$.name", "name proxy").pathMatch("$.phone", CoreMatchers.hasItem("54321"));
    }

    public interface ITest<T> {
        T demoAPI(String name, String address, List<String> phone);
    }
}
