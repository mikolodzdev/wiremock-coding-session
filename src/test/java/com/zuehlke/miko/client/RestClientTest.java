package com.zuehlke.miko.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.zuehlke.miko.wiremockcd.infra.impl.RestClient;
import com.zuehlke.miko.wiremockcd.infra.impl.dto.PostTO;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class RestClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(new WireMockConfiguration().extensions(new ResponseTemplateTransformer(false)));

    private static final RestClient REST_CLIENT = new RestClient("http://localhost:8080");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void getPost() throws Exception {
        stubFor(get(urlEqualTo("/posts/10")).willReturn(
                aResponse()
                        .withBody(OBJECT_MAPPER.writeValueAsString(new PostTO(10, 10, "Ipsum", "Lorem")))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON)

                        //TODO Task 1.
                        /**
                         * Respond for /posts/10 HTPP GET request with retrieval timestamp.
                         * <li>
                         *     <ul>Amend the test method RestClientTest.getPost(). Header name: date</ul>
                         * </li>
                         */
                )
        );

        PostTO p = REST_CLIENT.getPost(10);
        assertEquals(10, p.getId().intValue());

        verify(getRequestedFor(urlMatching("/posts/[0-9]+"))
                .withHeader("Required-Header", matching("Important value!"))
        );
    }

    @Test
    public void createPost() throws Exception {
        // TODO Task 2.
        /**
         * Create the unit test for RestClient#createPost(Integer, String, String)
         * <ul>
         *     <li>Upstream service returns id of the post, so the WireMock should return this as well,</li>
         *     <li>Verify if new PostTO.id(which is assigned by upstream service) is correctly deserialized and is present,</li>
         *     <li>Upstream services should answer with HTTP Code 201 CREATED when post created.</li>
         *     <li>If HTTP Code different than 201 is returned by upstream service Exception is thrown.</li>
         * </ul>
         */
    }

    @Test
    public void postPostWithAuthorizationHeader() throws Exception {
        // TODO Task 3.
        /**
         * Verify whether the /posts HTTP POST is sent with Authorization Header like “bearer <RANDOM_TOKEN>”
         */
    }

    @Test(expected = ProcessingException.class)
    @Ignore
    public void callGetWithTimeout() {
        // TODO Task 4.
        /**
         * Write a JUnit test for RestClient which checks
         * if the upstream service timeout is correctly handled. Expected behaviour is ProcessingException after 2 seconds.
         */
    }

    @Test
    public void createPostJSESSIONID() throws Exception {
        // TODO Task 5.
        /**
         * Rewrite value of request header «JSESSIONID» into response header of the same name.
         */
    }
}
