package com.github.yongchristophertang.demo.examples;

import com.github.yongchristophertang.demo.DemoApplication;
import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.engine.web.WebTemplate;
import com.github.yongchristophertang.engine.web.WebTemplateBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.yongchristophertang.engine.web.request.TestRequestBuilders.api;
import static com.github.yongchristophertang.engine.web.request.TestRequestBuilders.get;
import static com.github.yongchristophertang.engine.web.response.HttpResultHandlers.print;
import static com.github.yongchristophertang.engine.web.response.HttpResultMatchers.jsonPath;

/**
 * Test case to demonstrate generating HTTP request.
 *
 * @author Yong Tang
 * @since 0.1
 */
public class Demo1HTTPRequestGenerator extends DemoApplication {
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
    public void testProxyHttpRequestGeneration() throws Exception {
        webTemplate.perform(api(PersonServiceApi.class).createPerson(new Person() {{
            setName("John Wooden");
            setAddress("Apt.310 - 180 Lees Ave., Ottawa, ON, Canada");
            setPhone(Arrays.asList("613-983-0330", "613-983-6677"));
        }})).andDo(print());
    }

    @Test(dependsOnMethods = "testProxyHttpRequestGeneration")
    public void testRawHttpRequestGenerationAndAssertion() throws Exception {
        webTemplate.perform(get("http://localhost:8080/person/{id}").path("id", String.valueOf(2))).andDo(print())
            .andExpect(jsonPath("$.name", "John Wooden")).andExpect(jsonPath("$.id", 2))
            .andExpect(jsonPath("$.phone", Arrays.asList("613-983-0330", "613-983-6677")))
            .andExpect(jsonPath("$.phone[0]", "613-983-0330"))
            .andExpect(jsonPath("$.phone").arraySize(2))
            .andExpect(jsonPath("$.address").exists());
    }
}
