# MongoDB Task Planner - Projekt Zaliczeniowy

Aplikacja webowa typu "To-Do List" zrealizowana w Java Spring Boot, wykorzystująca elastyczność bazy danych MongoDB do przechowywania zadań o zróżnicowanej strukturze.

## 1. Architektura i Technologie
* **Backend:** Spring Boot 4.0.1 (Java 17)
* **Baza danych:** MongoDB (NoSQL)
* **Frontend:** Thymeleaf (Server-Side Rendering) + Bootstrap 5
* **Build Tool:** Maven

### Struktura projektu (MVC):
* `model`: Klasa `Task` z elastycznym polem `Map<String, Object>` (Schema-less).
* `repository`: Interfejs `MongoRepository` do operacji CRUD.
* `service`: Logika biznesowa, zaawansowane filtrowanie (`Criteria API`) i agregacje (`Aggregation Framework`).
* `controller`: Obsługa żądań HTTP i sterowanie widokami.

## 2. Uruchomienie

### Wymagania:
* JDK 17 lub nowsze
* Docker (do uruchomienia bazy) LUB zainstalowany lokalnie MongoDB Server

### Instrukcja krok po kroku:

1.  **Uruchomienie bazy danych (Docker):**
    ```bash
    docker run -d -p 27017:27017 --name mongodb mongo:latest
    ```

2.  **Konfiguracja:**
    Upewnij się, że w `src/main/resources/application.properties` wpisane jest:
    `spring.data.mongodb.uri=mongodb://localhost:27017/taskplanner`

3.  **Uruchomienie aplikacji:**
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Dostęp:**
    Otwórz przeglądarkę pod adresem: `http://localhost:8080`

> **Nota:** Aplikacja posiada wbudowany **Data Seeder**. Przy każdym uruchomieniu baza jest czyszczona i wypełniana 4 przykładowymi rekordami, aby zademonstrować działanie statystyk i filtrowania.

## 3. Wykorzystanie NoSQL (Kryteria Oceny)

Projekt wykorzystuje kluczowe zalety MongoDB, których nie oferują standardowe bazy SQL:

1.  **Elastyczny Schemat (Flexible Schema):**
    * Wykorzystano pole `private Map<String, Object> flexibleAttributes`.
    * Pozwala to na zapisywanie zadań o zupełnie różnych polach w tej samej kolekcji (np. zadanie "IT" ma pole `server_id`, a zadanie "Biznes" ma pole `client`).
    * W SQL wymagałoby to wielu tabel (`JOIN`) lub kolumn z wartościami `NULL`.

2.  **Agregacje (Aggregation Pipeline):**
    * Logika statystyk (np. liczba zadań w projekcie) jest realizowana po stronie bazy danych, a nie w aplikacji Java.
    * Użyto operacji: `$group`, `$match`, `$project`.
    * Przykład kodu w `TaskService.java`: `getStatsByProject()`.

3.  **Indeksy:**
    * Nałożono indeks na pole `category` (`@Indexed`) w celu optymalizacji najczęstszego filtrowania.

## 4. Funkcjonalności
* **CRUD:** Pełne dodawanie, edycja (z mapowaniem dynamicznych pól) i usuwanie.
* **Filtrowanie:** Wielokryterialne wyszukiwanie (Kategoria AND Priorytet AND Projekt) przy użyciu `MongoTemplate` i `Query Criteria`.
* **Statystyki:** Dashboard pokazujący średni priorytet i obłożenie projektów.