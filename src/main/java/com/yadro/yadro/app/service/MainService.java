package com.yadro.yadro.app.service;

import com.yadro.yadro.app.model.Person;
import com.yadro.yadro.app.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final MainRepository repository;
    private final RestTemplate restTemplate;
    private static final String API_URL = "https://api.randomdatatools.ru/";

    public void loadAndSavePersons(int desiredCount) {
        int batchSize = 100;
        int loaded = 0;
        int batchNumber = 0;
        
        log.info("Начинаем загрузку {} человек из API", desiredCount);
        
        while (loaded < desiredCount) {
            int count = Math.min(batchSize, desiredCount - loaded);
            String url = API_URL + "?count=" + count;
            batchNumber++;
            
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                headers.set("User-Agent", "SpringBootApp/1.0");
                HttpEntity<String> entity = new HttpEntity<>(headers);
                
                ResponseEntity<Person[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Person[].class);
                
                Person[] personsArray = response.getBody();
                
                if (personsArray != null && personsArray.length > 0) {
                    for (Person p : personsArray) {
                        p.prepareAddress();
                    }
                    repository.saveAll(Arrays.asList(personsArray));
                    loaded += personsArray.length;
                    log.info("Пакет {}: загружено {} человек (всего загружено {}/{})",
                             batchNumber, personsArray.length, loaded, desiredCount);
                } else {
                    log.warn("Пакет {}: API не вернул данных", batchNumber);
                    break;
                }
                
                if (loaded < desiredCount) {
                    Thread.sleep(500);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Загрузка прервана", e);
                break;
            } catch (Exception e) {
                log.error("Ошибка при загрузке пакета {}: {}", batchNumber, e.getMessage(), e);
                break;
            }
        }
        
        log.info("Загрузка завершена. Итого сохранено {} человек из {}", loaded, desiredCount);
    }

    public Page<Person> getPersonsPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public Person getPersonById(Long id) {
        return repository.findById(id);
    }

    public Person getRandomPerson() {
        return repository.findRandom();
    }

    public void saveAll(List<Person> people) {
        repository.saveAll(people);
    }

    public boolean isEmpty() {
        return repository.count() == 0;
    }
}