package com.zuehlke.miko.wiremockcd.infra.impl;

import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.zuehlke.miko.wiremockcd.infra.exception.PostNotCreatedException;
import com.zuehlke.miko.wiremockcd.infra.impl.dto.PostTO;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class RestClient {

    private final WebTarget webTarget;

    public RestClient(String host) {
        ClientConfig configuration = new ClientConfig();
        configuration = configuration.property(ClientProperties.READ_TIMEOUT, 2000);
        Client client = ClientBuilder.newClient(configuration);
        webTarget = client.target((host == null ? "https://jsonplaceholder.typicode.com" : host) + "/posts");
    }


    public List<PostTO> getPosts() {
        Response r = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header("Required-Header", "Important value!")
                .get();

        List<PostTO> output = r.readEntity(new GenericType<List<PostTO>>() {
        });
        return output;
    }

    public PostTO getPost(int postId) {
        Response r = webTarget
                .path("/" + postId)
                .request(MediaType.APPLICATION_JSON)
                .header("Required-Header", "Important value!")
                .get();

        PostTO output = r.readEntity(PostTO.class);
        System.err.println("'date' header value: " + r.getHeaderString("date"));

        return output;
    }

    public List<PostTO> getPostsOfUser(Integer userId) {
        Response r = webTarget
                .queryParam("userId", 1)
                .request(MediaType.APPLICATION_JSON)
                .header("Required-Header", "Important value!")
                .get();

        List<PostTO> output = r.readEntity(new GenericType<List<PostTO>>() {
        });
        return output;
    }

    /**
     * @throws PostNotCreatedException if post cannot be created because of technical problems.
     */
    public PostTO createPost(Integer userId, String title, String body) {

        Response r = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header("Req-Header", "Req-Header-value")
                .header("JSESSIONID", RandomStringUtils.randomAlphanumeric(12))
                .post(Entity.entity(new PostTO(userId, null, title, body), MediaType.APPLICATION_JSON));

        if (Response.Status.CREATED.getStatusCode() != r.getStatus()) {
            throw new PostNotCreatedException();
        }
        return r.readEntity(PostTO.class);
    }

    /**
     * Updates an existing post.
     * @return new version of the post under postId
     */
    public PostTO updatePost(Integer postId, String title, String body) {
        Response r = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header("Req-Header", "Req-Header-value")
                .put(Entity.entity(new PostTO(null, postId, title, body), MediaType.APPLICATION_JSON));

        return r.readEntity(PostTO.class);
    }
}
