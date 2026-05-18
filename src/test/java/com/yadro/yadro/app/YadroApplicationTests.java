package com.yadro.yadro.app;

import com.yadro.yadro.app.model.Person;
import com.yadro.yadro.app.repository.MainRepository;
import com.yadro.yadro.app.service.MainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class YadroApplicationTests {

    @Autowired
    private MainService mainService;

    @Autowired
    private MainRepository mainRepository;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void cleanDb() {
        mainRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(mainService).isNotNull();
    }

    @Test
    void testLoadAndSavePersonsFromApi() {
        Person[] mockPersons = {
                createPerson(null, "Male", "John", "Doe", "+123456789", "john@example.com", "New York", "New York, 5th Ave, д. 10"),
                createPerson(null, "Female", "Jane", "Smith", "+987654321", "jane@example.com", "Los Angeles", "Los Angeles, Sunset Blvd, д. 20")
        };
        ResponseEntity<Person[]> responseEntity = new ResponseEntity<>(mockPersons, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Person[].class)))
                .thenReturn(responseEntity);

        mainService.loadAndSavePersons(2);

        assertThat(mainRepository.count()).isEqualTo(2);
        Person saved = mainRepository.findById(1L);
        assertThat(saved.getFirstName()).isEqualTo("John");
    }

    @Test
    void testPagination() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            people.add(createPerson(null, "Gender", "Name" + i, "Surname", "phone", "email", "City" + i, "Address" + i));
        }
        mainRepository.saveAll(people);

        Page<Person> firstPage = mainService.getPersonsPage(0, 10);
        assertThat(firstPage.getTotalElements()).isEqualTo(25);
        assertThat(firstPage.getContent()).hasSize(10);
        assertThat(firstPage.getTotalPages()).isEqualTo(3);
    }

    @Test
    void testFindPersonById() {
        Person saved = createPerson(null, "F", "Anna", "Ivanova", "555", "anna@ex.com", "Moscow", "Moscow, Tverskaya, д. 1");
        mainRepository.saveAll(List.of(saved));

        Person found = mainService.getPersonById(1L);
        assertThat(found.getFirstName()).isEqualTo("Anna");
    }

    @Test
    void testRandomPerson() {
        Person p1 = createPerson(null, "M", "Alex", "A", "111", "a@a.com", "City1", "Address1");
        Person p2 = createPerson(null, "F", "Bella", "B", "222", "b@b.com", "City2", "Address2");
        mainRepository.saveAll(List.of(p1, p2));

        Person random = mainService.getRandomPerson();
        assertThat(random).isNotNull();
        assertThat(random.getFirstName()).isIn("Alex", "Bella");
    }

    @Test
    void isEmptyWhenNoData() {
        assertThat(mainService.isEmpty()).isTrue();
        Person testPerson = createPerson(null, "M", "Test", "Test", "0", "t@t.com", "T", "T address");
        mainRepository.saveAll(List.of(testPerson));
        assertThat(mainService.isEmpty()).isFalse();
    }

    // Вспомогательный метод для создания Person с city и address
    private Person createPerson(Long id, String gender, String firstName, String lastName,
                                String phone, String email, String city, String address) {
        Person p = new Person();
        p.setId(id);
        p.setGender(gender);
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setPhone(phone);
        p.setEmail(email);
        p.setCity(city);
        p.setAddress(address);
        return p;
    }
}