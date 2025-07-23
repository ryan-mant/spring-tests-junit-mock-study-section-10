package br.com.erudio.controllers;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.erudio.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.model.Person;
import br.com.erudio.services.PersonServices;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonServices service;

    private Person person;

    @BeforeEach
    void setup(){
        person = new Person("Lucas", "Pereira", "lucas@email.com.br", "Rua 1", "male");
    }


    @Test
    @DisplayName("JUnit test for Given Person Object when Create Person then Return Saved Person")
    void testGivenPersonObject_WhenCreatePerson_thenReturnSavedPerson() throws Exception {

        // Given / Arrange
        given(service.create(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // When / Act
        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)));

        // Then / Assert
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }
    @Test
    @DisplayName("JUnit test for Given List Of Persons When FindAll Persons then Return Persons List")
    void testGivenListOfPersons_WhenFindAllPersons_thenReturnPersonsList() throws Exception {

        // Given / Arrange

        List<Person> personList = new ArrayList<>();
        personList.add(person);
        personList.add(new Person("Maria", "Helena", "maria@email.com.br", "Rua 2", "female"));

        given(service.findAll()).willReturn(personList);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person"));

        // Then / Assert
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(personList.size())));
    }

    @Test
    @DisplayName("JUnit test for Given PersonId When FindById then Return Person Object")
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() throws Exception {

        // Given / Arrange
        Long personId = 1L;
        given(service.findById(personId))
                .willReturn(person);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        // Then / Assert
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }
    @Test
    @DisplayName("JUnit test for Given Update Person When Update then Return Updated Person Object")
    void testGivenUpdatePerson_WhenUpdate_thenReturnUpdatedPersonObject() throws Exception {

        // Given / Arrange
        Long personId = 1L;
        given(service.findById(personId)).willReturn(person);
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        // When / Act
        Person updatedPerson = new Person(
                "Maria",
                "Helena",
                "maria@email.com.br",
                "Rua 2",
                "female");

        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for Given Unexistent Person When Update then Return Not Found")
    void testGivenUnexistentPerson_WhenUpdate_thenReturnNotFound() throws Exception {

        // Given / Arrange
        Long personId = 1L;
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(1));

        // When / Act
        Person updatedPerson = new Person(
                "Maria",
                "Helena",
                "maria@email.com.br",
                "Rua 2",
                "female");

        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }
}