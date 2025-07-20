package br.com.erudio.repositories;

import br.com.erudio.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @DisplayName("Given Person Object when Save then Return Saved Person")
    @Test
    void testGivenPersonObject_whenSave_thenReturnSavedPerson() {

    	// Given / Arrange

        Person person0 = new Person("Lucas", "Pereira", "lucas@email.com.br", "Rua 1", "male");

    	// When / Act

        Person savedPerson = repository.save(person0);


    	// Then / Assert

        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }
    @DisplayName("Given Person Object when FindById then Return Person Object")
    @Test
    void testGivenPersonObject_whenFindById_thenReturnPersonObject() {

        // Given / Arrange

        Person person0 = new Person("Lucas", "Pereira", "lucas@email.com.br", "Rua 1", "male");

        repository.save(person0);


        // When / Act

        Person savedPerson = repository.findById(person0.getId()).get();

        // Then / Assert

        assertNotNull(savedPerson);
        assertEquals(person0.getId(), savedPerson.getId());
    }
    @DisplayName("Given Person Object when FindByEmail then Return Person Object")
    @Test
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {

        // Given / Arrange

        Person person0 = new Person("Lucas", "Pereira", "lucas@email.com.br", "Rua 1", "male");

        repository.save(person0);


        // When / Act

        Person savedPerson = repository.findByEmail(person0.getEmail()).get();

        // Then / Assert

        assertNotNull(savedPerson);
        assertEquals(person0.getEmail(), savedPerson.getEmail());
    }

    @DisplayName("Given Person List when FindAll then Return Person List")
    @Test
    void testGivenPersonList_whenFindAll_thenReturnPersonList() {

        // Given / Arrange

        Person person0 = new Person("Lucas", "Pereira", "lucas@email.com.br", "Rua 1", "male");
        Person person1 = new Person("Mateus", "Rodriss", "mateus@email.com.br", "Rua 2", "male");
        Person person2 = new Person("Helena", "Souza", "helena@email.com.br", "Rua 3", "female");

        repository.save(person0);
        repository.save(person1);
        repository.save(person2);

        // When / Act

        List<Person> persons = repository.findAll();

        // Then / Assert

        assertNotNull(persons);
        assertEquals(3, persons.size());
    }
}