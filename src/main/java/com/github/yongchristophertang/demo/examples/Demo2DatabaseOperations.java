package com.github.yongchristophertang.demo.examples;

import com.github.yongchristophertang.database.annotations.SqlDB;
import com.github.yongchristophertang.database.testng.TestNGDBInjectionModuleFactory;
import com.github.yongchristophertang.demo.DemoApplication;
import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.engine.web.WebTemplate;
import com.github.yongchristophertang.engine.web.WebTemplateBuilder;
import com.google.inject.Inject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.yongchristophertang.engine.web.request.TestRequestBuilders.api;
import static com.github.yongchristophertang.engine.web.response.HttpResultHandlers.print;
import static com.github.yongchristophertang.engine.web.response.HttpResultMatchers.jsonPath;

/**
 * Test cases to demonstrate database operations
 *
 * @author Yong Tang
 * @since 0.1
 */
@Guice(moduleFactory = TestNGDBInjectionModuleFactory.class)
@SqlDB(url="jdbc:mysql://localhost:3306/persondb", userName = "demo", password = "demopwd")
public class Demo2DatabaseOperations extends DemoApplication {
    private JdbcTemplate jdbcTemplate;
    private WebTemplate webTemplate = WebTemplateBuilder.customConfig().alwaysDo(print()).build();

    @Inject
    private DataSource dataSource;

    @BeforeClass
    public void setUpDataBaseAndService() throws InterruptedException, ExecutionException, TimeoutException {
        runDemoService();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @AfterClass
    public void shutDownService() {
        stopDemoService();
    }

    @Test
    public void testJdbcOperations() throws Exception {
        webTemplate.perform(api(PersonServiceApi.class)
            .createPerson(
                new Person("Benjamin Harper", "334 Bay view st., Ottawa, ON, Canada", Arrays.asList("613-560-7777"))));
        jdbcTemplate.execute("UPDATE person SET name='Jason Elsa' WHERE id=2");
        webTemplate.perform(api(PersonServiceApi.class).readPerson(2L)).andExpect(jsonPath("$.name", "Jason Elsa"));
    }
}
