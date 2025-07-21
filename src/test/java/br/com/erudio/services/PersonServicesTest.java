package br.com.erudio.services;

import br.com.erudio.exceptions.CreatePersonException;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonServices service;

    private Person person0;

    @BeforeEach
    void setup(){
        person0 = new Person("Lucas", "Pereira", "lucas@email.com.br", "Rua 1", "male");
    }


    @DisplayName("JUnit test for Given Person Object when Save Person then Return Person Object")
    @Test
    void testGivenPersonObject_whenSavePerson_thenReturnPersonObject() {

    	// Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person0)).willReturn(person0);

    	// When / Act
        Person savedPerson = service.create(person0);

    	// Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Lucas", savedPerson.getFirstName());
    }

    @DisplayName("JUnit test for Given Existing Email when Save Person then Throws Exception")
    @Test
    void testGivenExistingEmail_whenSavePerson_thenThrowsException() {

        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));

        // When / Act

        assertThrows(CreatePersonException.class, () -> service.create(person0));

        // Then / Assert

        verify(repository, never()).save(any(Person.class));
    }

    @DisplayName("JUnit test for Given Persons List when FindAllPersons then Return Persons List")
    @Test
    void testGivenPersonsList_whenFindAllPersons_thenReturnPersonsList() {

        // Given / Arrange
        Person person1 = new Person("Mateus", "Rodriss", "mateus@email.com.br", "Rua 2", "male");

        given(repository.findAll()).willReturn(List.of(person0, person1));

        // When / Act
        List<Person> personList = service.findAll();

        // Then / Assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @DisplayName("JUnit test for Given Empty Persons List when FindAllPersons then Return Empty Persons List")
    @Test
    void testGivenEmptyPersonsList_whenFindAllPersons_thenReturnEmptyPersonsList() {

        // Given / Arrange

        given(repository.findAll()).willReturn(Collections.emptyList());

        // When / Act
        List<Person> personList = service.findAll();

        // Then / Assert
        assertTrue(personList.isEmpty());
        assertEquals(0, personList.size());
    }

    @DisplayName("JUnit test for Given Person Id when FindById then Return Person Object")
    @Test
    void testGivenPersonId_whenFindById_thenReturnPersonObject() {

        // Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        // When / Act
        Person savedPerson = service.findById(1L);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Lucas", savedPerson.getFirstName());
    }

    @DisplayName("JUnit test for Given Person Object when Update Person then Return Updated Person Object")
    @Test
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {

        // Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        person0.setEmail("mudou@gmail.com");
        person0.setFirstName("Kauan");

        given(repository.save(person0)).willReturn(person0);
        // When / Act
        Person updatedPerson = service.update(person0);

        // Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("Kauan", updatedPerson.getFirstName());
        assertEquals("mudou@gmail.com", updatedPerson.getEmail());
    }

    @DisplayName("JUnit test for Given Person Id when Delete Person then Do Nothing")
    @Test
    void testGivenPersonId_whenDeletePerson_thenDoNothing() {

        // Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        willDoNothing().given(repository).delete(person0);

        // When / Act
        service.delete(person0.getId());

        // Then / Assert
        verify(repository, times(1)).delete(person0);
    }
}