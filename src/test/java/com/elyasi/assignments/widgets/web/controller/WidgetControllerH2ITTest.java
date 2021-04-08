package com.elyasi.assignments.widgets.web.controller;

import com.elyasi.assignments.widgets.constant.GlobalConstant;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * {@link https://spring.io/guides/gs/testing-web/}
 */


//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) (integration test)
// use with TestRestTemplate
// run whole the application with the server

// Another useful approach is to "not start the server at all but to test
// only the layer below that", where Spring handles the incoming HTTP request
// and hands it off to your controller. That way, almost of the full stack is used,
// and your code will be called in exactly the same way as if it were processing
// a real HTTP request but without the cost of starting the server.
// To do that, use Spring’s MockMvc and ask for that to be injected for you
// by using the @AutoConfigureMockMvc annotation on the test case.
// In this test, the full Spring application context is started but without the server
@SpringBootTest // (integration test)
@AutoConfigureMockMvc
// with MockMvc


// @WebMvcTest (unit test)
// We can narrow the tests to "only the web layer" by using @WebMvcTest
// with MockMvc
// in this way, Spring Boot instantiates only the web layer rather than the whole context.
// We use @MockBean to create and inject a mock for the GreetingService
@ActiveProfiles({GlobalConstant.PROFILE_H2, GlobalConstant.PROFILE_DEV})
class WidgetControllerH2ITTest extends WidgetControllerITest {

}