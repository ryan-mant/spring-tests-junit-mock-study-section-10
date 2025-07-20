package br.com.erudio.services;

import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}