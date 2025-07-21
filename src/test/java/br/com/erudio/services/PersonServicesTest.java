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


    @DisplayName("JUnit test for Given Person Object when Save Person Should Return Person Object")
    @Test
    void testGivenPersonObject_whenSavePerson_ShouldReturnPersonObject() {

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

    @DisplayName("JUnit test for Given Persons List when FindAllPersons Then Return Persons List")
    @Test
    void testGivenPersonsList_whenFindAllPersons_ThenReturnPersonsList() {

        // Given / Arrange
        Person person1 = new Person("Mateus", "Rodriss", "mateus@email.com.br", "Rua 2", "male");

        given(repository.findAll()).willReturn(List.of(person0, person1));

        // When / Act
        List<Person> personList = service.findAll();

        // Then / Assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }
}