# MongoDB Task Planner - NBD

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-green)
![MongoDB](https://img.shields.io/badge/Database-MongoDB-forestgreen)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

Aplikacja webowa typu **"To-Do List"** zrealizowana w Java Spring Boot, w pełni konteneryzowana. Projekt wykorzystuje elastyczność bazy danych **NoSQL (MongoDB)** do przechowywania zadań oraz zapewnia pełne bezpieczeństwo danych użytkowników.


##  1. Architektura i Technologie

* **Backend:** Spring Boot 4.0.1 (Java 17)
* **Baza danych:** MongoDB (NoSQL)
* **Bezpieczeństwo:** Spring Security 6 (Autoryzacja, szyfrowanie BCrypt, ochrona CSRF)
* **Frontend:** Thymeleaf (Server-Side Rendering) + Bootstrap 5
* **Konteneryzacja:** Docker + Docker Compose

### Struktura projektu (MVC):
* **`model`**:
    * `Task`: Posiada elastyczne pole `Map<String, Object>` (**Schema-less**) oraz flagę stanu (`completed`).
    * `AppUser`: Model użytkownika z danymi do logowania.
* **`repository`**: Interfejsy `MongoRepository` do operacji CRUD.
* **`service`**:
    * Logika biznesowa i obsługa statusów zadań.
    * Implementacja `UserDetailsService` (łączenie Spring Security z bazą Mongo).
    * Zaawansowane agregacje (**Aggregation Framework**) do statystyk.
* **`controller`**: Obsługa żądań HTTP, sterowanie widokami i zabezpieczanie endpointów.

---

## 2. Uruchomienie Aplikacji

Projekt jest w pełni skonfigurowany pod środowisko **Docker**. Nie wymaga instalowania Javy ani MongoDB na lokalnym komputerze – wszystko uruchamia się w izolowanych kontenerach.

### Wymagania:
* Zainstalowany **Docker Desktop** (lub Docker Engine + Docker Compose).

### Instrukcja krok po kroku:

1.  **Pobierz projekt** i otwórz terminal w głównym katalogu.

2.  **Uruchom skrypt startowy** (zależnie od systemu):

    * **Linux:**
        ```bash
        chmod +x deploy.sh
        ./deploy.sh
        ```

    * **Windows:**
        ```batch
        deploy.bat
        ```

3.  **Dostęp:**
    * Adres w przeglądarce: [http://localhost:8080](http://localhost:8080)

## 3. Bezpieczeństwo i Logowanie

Aplikacja implementuje pełny system autoryzacji oparty na **Spring Security**:

* **Rejestracja:** Możliwość założenia konta (`/register`). Hasła są haszowane algorytmem **BCrypt** przed zapisem do bazy.
* **Izolacja danych:** Każdy użytkownik widzi i edytuje **tylko swoje** zadania. Próba dostępu do cudzego zadania (np. przez ręczną zmianę ID w URL) kończy się błędem autoryzacji (403 Forbidden).
* **Ochrona CSRF:** Wszystkie formularze (logowanie, dodawanie, edycja, wylogowanie) są zabezpieczone tokenami przed atakami.

## 4. Wykorzystanie NoSQL (Kryteria Oceny)

Projekt demonstruje kluczowe zalety MongoDB w porównaniu do relacyjnych baz SQL:

### 1. Elastyczny Schemat (Flexible Schema)
* W klasie `Task` wykorzystano pole:
    ```java
    private Map<String, Object> attributes;
    ```
* Pozwala to na zapisywanie w tej samej kolekcji zadań o **zupełnie różnych polach** (np. zadanie typu "Projekt" ma pole `deadline`, a inne zadanie go nie posiada).
* **Porównanie z SQL:** W bazie relacyjnej wymagałoby to wielu tabel z kosztownymi `JOIN`-ami lub jednej tabeli z wieloma kolumnami `NULL`.

### 2. Agregacje (Aggregation Pipeline)
* Logika statystyk (np. liczba zadań w projekcie, średni priorytet) jest realizowana **po stronie bazy danych**, co odciąża aplikację.
* Wykorzystano etapy potoku:
    * `$match` – filtrowanie po użytkowniku.
    * `$group` – grupowanie wyników po nazwie projektu.
    * `$project` – formatowanie wyniku.
* Lokalizacja w kodzie: `TaskService.java` -> metoda `getProjectStats()`.

### 3. Wydajność i Indeksy
* Nałożono indeksy na pola kluczowe (np. `category`, `owner`) przy użyciu adnotacji `@Indexed`, co znacznie przyspiesza filtrowanie przy dużej liczbie rekordów.

##  5. Funkcjonalności

*  **Zarządzanie Zadaniami:** Pełny CRUD (Dodawanie, Edycja, Usuwanie) z obsługą dynamicznych atrybutów.
*  **Archiwizacja:** System oznaczania zadań jako "Wykonane" z podziałem widoku na zakładki **"Do zrobienia"** i **"Archiwum"**.
*  **Zaawansowane Wyszukiwanie:** Wielokryterialne filtrowanie (*Status* AND *Kategoria* AND *Priorytet*) przy użyciu `MongoTemplate` i `Query Criteria`.
*  **Dashboard:** Wyświetlanie statystyk i średniego priorytetu zadań zalogowanego użytkownika.