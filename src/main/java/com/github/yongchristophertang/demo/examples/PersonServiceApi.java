package com.github.yongchristophertang.demo.examples;

import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.engine.web.annotations.*;
import com.github.yongchristophertang.engine.web.request.JsonStringConverter;
import com.github.yongchristophertang.engine.web.request.RequestBuilder;

/**
 * Demo service's API definition
 *
 * @author Yong Tang
 * @since 0.1
 */
@Host(value = "localhost", port = 8080)
@Path("/demo")
public interface PersonServiceApi {

    @POST
    @Path("/persons")
    @Produce("application/json;charset=utf-8")
    RequestBuilder createPerson(@BodyParam(converter = JsonStringConverter.class) Person person);

    @GET
    @Path("/person/{id}")
    RequestBuilder readPerson(@PathParam("id") Long id);

    @GET
    @Path("/person")
    RequestBuilder readPerson(@QueryParam("name") String name);

    @PUT
    @Path("/person/{id}")
    RequestBuilder updatePerson(@PathParam("id") Long id, @BodyParam String updateName);

    @DELETE
    @Path("person/{id}")
    RequestBuilder deletePerson(@PathParam("id") Long id);
}
