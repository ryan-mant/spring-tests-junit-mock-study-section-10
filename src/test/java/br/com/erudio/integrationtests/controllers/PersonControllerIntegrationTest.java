package br.com.erudio.integrationtests.controllers;

import br.com.erudio.config.TestConfig;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    public static void setup(){
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        person = new Person("Lucas", "Pereira", "lucas@email.com.br", "Rua 1", "male");
    }

    @Order(1)
    @DisplayName("JUnit integration test for Given Person Object when Create One Person Should Return A Person Object")
    @Test
    void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person savedPerson = mapper.readValue(content, Person.class);

        person = savedPerson;

        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getId());
        assertNotNull(savedPerson.getFirstName());
        assertNotNull(savedPerson.getLastName());
        assertNotNull(savedPerson.getAddress());
        assertNotNull(savedPerson.getGender());
        assertNotNull(savedPerson.getEmail());

        assertTrue(savedPerson.getId() > 0);
        assertEquals("Lucas", savedPerson.getFirstName());
        assertEquals("Pereira", savedPerson.getLastName());
        assertEquals("Rua 1", savedPerson.getAddress());
        assertEquals("male", savedPerson.getGender());
        assertEquals("lucas@email.com.br", savedPerson.getEmail());

    }

    @Order(2)
    @DisplayName("JUnit integration test for Given Person Object when Update One Person Should Return A Updated Person Object")
    @Test
    void integrationTestGivenPersonObject_when_UpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws JsonProcessingException {

        person.setFirstName("Kauan");
        person.setEmail("kauan@email.com.br");
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person updatedPerson = mapper.readValue(content, Person.class);

        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());
        assertNotNull(updatedPerson.getEmail());

        assertTrue(updatedPerson.getId() > 0);
        assertEquals("Kauan", updatedPerson.getFirstName());
        assertEquals("Pereira", updatedPerson.getLastName());
        assertEquals("Rua 1", updatedPerson.getAddress());
        assertEquals("male", updatedPerson.getGender());
        assertEquals("kauan@email.com.br", updatedPerson.getEmail());

    }
}