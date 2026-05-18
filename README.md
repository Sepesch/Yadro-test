# Приложение для загрузки и просмотра данных о людях

## Описание

Веб-приложение на **Spring Boot** загружает случайные данные из публичного API [RandomDataTools](https://randomdatatools.ru/), сохраняет их в базу данных **H2** и предоставляет веб‑интерфейс для просмотра, пагинации, а также отдельные страницы для конкретного или случайного человека.

## Функциональность

- Загрузка произвольного количества людей из API (пакетами по 100, с задержкой 500 мс)
- Хранение всех данных в реляционной БД
- Главная страница со списком людей (пагинация, ссылки на карточку)
- Форма для загрузки дополнительных записей
- Страница конкретного человека: `/{id}`
- Страница случайного человека: `/random` (обновляется при каждом запросе)
- Обработка ошибок (404, 400, 500) с кастомными страницами
- Юнит-тесты с моками внешнего API
- Запуск в **Docker** через `docker-compose`

## Стек технологий и обоснование выбора

| Технология               | Зачем                                                                 |
|--------------------------|-----------------------------------------------------------------------|
| **Spring Boot 3.4**      | Быстрая разработка, встроенный контейнер сервлетов, удобное тестирование |
| **Thymeleaf**            | Интеграция со Spring MVC, естественные HTML-шаблоны                   |
| **H2 (in‑memory)**       | Демо-БД без установки, поддержка SQL, автономность                    |
| **Spring JDBC**          | Лёгкий доступ к БД, полный контроль над SQL, без ORM                  |
| **RestTemplate**         | Синхронный HTTP-клиент, легко мокается в тестах                       |
| **Gradle**               | Гибкая сборка, многостадийный Dockerfile                              |
| **JUnit + Mockito**      | Встроенный мокинг внешних вызовов                                     |
| **Docker + compose**     | Портативность, единая команда запуска                                 |

## Запуск приложения

### 1. Локальный запуск (без Docker)

**Требования:** Java 21, Gradle (или `./gradlew`)

```bash
git clone <your-repo-url>
cd yadro-app
./gradlew clean bootJar
java -jar build/libs/yadro-0.0.1-SNAPSHOT.jar
```
После запуска откройте [http://localhost:8080](http://localhost:8080)

 При первом старте автоматически загружается 1000 человек (класс `DataLoader`).

### 2. Запуск в Docker (рекомендовано)

Убедитесь, что установлены **Docker** и **Docker Compose**.

```bash
docker-compose up --build
```
Или вручную:

```bash
docker build -t yadro-app .
docker run -p 8080:8080 yadro-app
```
Приложение будет доступно на `http://localhost:8080`.

## Как пользоваться

|Страница|Действие|
|---|---|
|`/` или `/homepage`|Таблица со списком людей, пагинация, кнопка «Загрузить ещё», ссылка на случайного человека|
|`/load` (POST)|Загружает указанное количество новых людей (параметр `count`)|
|`/{id}`|Карточка человека с полной информацией (включая полный адрес)|
|`/random`|Случайный человек – при обновлении страницы данные меняются|
|`/count`|Возвращает общее количество записей в базе (текст/plain)|
## Тестирование

Запуск всех тестов:

```bash
./gradlew test
```
Тесты (`YadroApplicationTests`) покрывают:
- Загрузку и сохранение (мок `RestTemplate`)
- Пагинацию
- Поиск по ID
- Получение случайного человека
- Проверку `isEmpty()`

## Структура проекта (ключевые файлы)

```text

src/main/java/com/yadro/yadro/app/
├── config/MainConfig.java          # Настройка RestTemplate
├── controller/MainController.java  # Обработка веб-запросов
├── exception/...                   # Кастомные исключения + глобальный хендлер
├── model/Person.java               # DTO, подготовка поля address
├── repository/MainRepository.java  # JdbcTemplate, SQL-запросы
├── service/MainService.java        # Логика загрузки из API, пагинация
├── service/DataLoader.java         # Загрузка 1000 записей при старте
└── YadroApplication.java
src/main/resources/
├── static/css/style.css            # Стили
├── templates/
│   ├── index.html                  # Главная страница с таблицей
│   ├── person.html                 # Карточка человека
│   └── error.html                  # Страница ошибок
├── application.properties          # Настройки БД, Thymeleaf
└── schema.sql                      # Создание таблицы persons
Dockerfile                           # Многостадийная сборка
compose.yaml                         # Описание сервиса для Docker Compose
build.gradle                         # Зависимости и плагины
```
## Ссылки
- [RandomDataTools API](https://randomdatatools.ru/developers/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Docker Compose](https://docs.docker.com/compose/)
