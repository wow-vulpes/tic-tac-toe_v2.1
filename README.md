# Крестики-Нолики (Spring Boot, JWT)

В рамках проекта реализована серверная часть веб-приложения «Крестики-Нолики».

### Статус проекта
* На данный момент проект находится в разработке, пока добавлена только JWT-аутентификация, исправляю баги

### Основной функционал:

* Реализована JWT-аутентификация
* Добавлена поддержка ролей
* Настроена безопасность через Spring Security
* Подключена PostgreSQL через Spring Data JPA/Hibernate

### Игровая логика:

* Создание и хранение игр
* История завершённых игр
* Таблица лидеров

---

### Технологии:

* Java 18
* Gradle (Kotlin DSL)
* Spring Boot
* Spring Security
* Spring Data JPA / Hibernate
* PostgreSQL
* REST API
* JWT Auth

---

### Быстрый запуск:

```bash
./gradlew bootRun
```

Или:

```bash
./gradlew build
java -jar build/libs/*.jar
```
