package com.github.yongchristophertang.demo.examples;

import com.github.yongchristophertang.demo.DemoApplication;
import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.engine.web.WebTemplate;
import com.github.yongchristophertang.engine.web.WebTemplateBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.yongchristophertang.engine.web.request.TestRequestBuilders.api;
import static com.github.yongchristophertang.engine.web.request.TestRequestBuilders.get;
import static com.github.yongchristophertang.engine.web.response.HttpResultHandlers.print;
import static com.github.yongchristophertang.engine.web.response.HttpResultMatchers.jsonPath;
import static com.github.yongchristophertang.engine.web.response.HttpResultMatchers.status;
import static com.github.yongchristophertang.engine.web.response.HttpResultTransformers.json;

/**
 * Test case to demonstrate generating HTTP request.
 *
 * @author Yong Tang
 * @since 0.1
 */
public class Demo1HTTPRequestGenerator extends DemoApplication {
    private static final Logger LOGGER = LogManager.getLogger();
    private WebTemplate webTemplate = WebTemplateBuilder.defaultConfig().build();

    @BeforeClass
    public void setUpWebService() throws InterruptedException, ExecutionException, TimeoutException {
        runDemoService();
    }

    @AfterClass
    public void shutDownWebService() {
        stopDemoService();
    }

    @Test
    public void testProxyHttpRequestGenerationAndResponseAssertion() throws Exception {
        webTemplate.perform(api(PersonServiceApi.class).createPerson(new Person() {{
            setName("John Wooden");
            setAddress("Apt.310 - 180 Lees Ave., Ottawa, ON, Canada");
            setPhone(Arrays.asList("613-983-0330", "613-983-6677"));
        }})).andDo(print());

        webTemplate.perform(get("http://localhost:8080/person/{id}").path("id", String.valueOf(2))).andDo(print())
            .andExpect(jsonPath("$.name", "John Wooden")).andExpect(jsonPath("$.id", 2))
            .andExpect(jsonPath("$.phone", Arrays.asList("613-983-0330", "613-983-6677")))
            .andExpect(jsonPath("$.phone[0]", "613-983-0330"))
            .andExpect(jsonPath("$.phone").arraySize(2))
            .andExpect(jsonPath("$.address").exists());
    }

    @Test
    public void testDifferentHttpStatus() throws Exception {
        webTemplate.perform(api(PersonServiceApi.class).readPerson("Alexander Lee")).andDo(print()).andExpect(status().is(404));
    }

    @Test
    public void testDeserializationOfHttpResponse() throws Exception {
        Person person = webTemplate.perform(api(PersonServiceApi.class).readPerson(1L))
                .andDo(print()).andTransform(json().object(Person.class));
        LOGGER.info("This is the deserialized Java Bean: {}", person);

        List<String> phones = webTemplate.perform(api(PersonServiceApi.class).readPerson("Will O'Corner"))
                .andDo(print()).andTransform(json("$[0].phone").list(String.class));
        LOGGER.info("This is the deserialized Java List: {}", phones);
    }
}
