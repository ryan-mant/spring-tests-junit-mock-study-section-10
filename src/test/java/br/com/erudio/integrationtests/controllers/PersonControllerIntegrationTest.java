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

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    static void setup() {
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

    @Order(3)
    @DisplayName("JUnit integration test for Given Person Id when FindById Should Return A Person Object")
    @Test
    void integrationTestGivenPersonId_when_FindById_ShouldReturnAPersonObject() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person foundPerson = mapper.readValue(content, Person.class);

        assertNotNull(foundPerson);
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        assertNotNull(foundPerson.getEmail());

        assertTrue(foundPerson.getId() > 0);
        assertEquals("Kauan", foundPerson.getFirstName());
        assertEquals("Pereira", foundPerson.getLastName());
        assertEquals("Rua 1", foundPerson.getAddress());
        assertEquals("male", foundPerson.getGender());
        assertEquals("kauan@email.com.br", foundPerson.getEmail());

    }

    @Order(4)
    @DisplayName("JUnit integration test for when FindAll Should Return A Person List")
    @Test
    void integrationTest_when_FindAll_ShouldReturnAPersonList() throws JsonProcessingException {

        Person person1 = new Person("Joana", "Marques", "joana@email.com.br", "Rua 2", "female");

        given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person1)
                .when()
                .post()
                .then()
                .statusCode(200);

        var content = given()
                .spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        List<Person> people = Arrays.stream(mapper.readValue(content, Person[].class)).toList();

        Person firstPerson = people.get(0);

        assertTrue(firstPerson.getId() > 0);
        assertEquals("Kauan", firstPerson.getFirstName());
        assertEquals("Pereira", firstPerson.getLastName());
        assertEquals("Rua 1", firstPerson.getAddress());
        assertEquals("male", firstPerson.getGender());
        assertEquals("kauan@email.com.br", firstPerson.getEmail());

        Person secondPerson = people.get(1);

        assertTrue(secondPerson.getId() > 0);
        assertEquals("Joana", secondPerson.getFirstName());
        assertEquals("Marques", secondPerson.getLastName());
        assertEquals("Rua 2", secondPerson.getAddress());
        assertEquals("female", secondPerson.getGender());
        assertEquals("joana@email.com.br", secondPerson.getEmail());

    }

    @Order(5)
    @DisplayName("JUnit integration test for Given Person Id when delete Should Return No content")
    @Test
    void integrationTestGivenPersonId_when_Delete_ShouldReturnNoContent() {
        given()
            .spec(specification)
            .pathParam("id", person.getId())
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }
}