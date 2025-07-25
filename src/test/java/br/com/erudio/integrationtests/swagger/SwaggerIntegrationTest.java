package br.com.erudio.integrationtests.swagger;

import br.com.erudio.config.TestConfig;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {
	@DisplayName("JUnit test for Should Display SwaggerUI Page")
	@Test
	void testShouldDisplaySwaggerUIPage() {
		var content = given().basePath("/swagger-ui/index.html")
				.port(TestConfig.SERVER_PORT)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract().body().asString();

		assertTrue(content.contains("Swagger UI"));
	}

}
