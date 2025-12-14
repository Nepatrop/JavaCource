# Система управления сотрудниками (Employee Registry System)

Веб-приложение на Spring Boot для управления сотрудниками, отделами и формирования отчетов компании.

## Выполненные требования курса

Проект соответствует всем 7 требованиям технического задания:

1. **Слоистая архитектура** - Entity, Repository, Service, Controller слои
2. **Spring Framework** - Spring Boot 3.5.6, Spring Data JPA, Spring Security
3. **Графический интерфейс** - Thymeleaf + Bootstrap 5, веб-страницы для всех операций
4. **Аутентификация с шифрованием** - BCrypt для паролей, вход по email
5. **База данных через Hibernate** - JPA сущности, Spring Data репозитории, H2/PostgreSQL
6. **Документирование кода** - JavaDoc комментарии во всех классах
7. **Стабильность** - обработка исключений, валидация данных

## Технологический стек

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 21
- **Database**: H2 (dev/test), PostgreSQL (prod)
- **ORM**: Hibernate / Spring Data JPA
- **Security**: Spring Security + BCrypt
- **Template Engine**: Thymeleaf
- **Frontend**: Bootstrap 5.1.3
- **Build Tool**: Maven
- **Logging**: SLF4J + Log4j2

## Структура проекта

```
src/
├── main/
│   ├── java/ru/danila/NauJava/
│   │   ├── entity/           # JPA сущности (Employee, Department, User, Role, Report)
│   │   ├── repository/       # Spring Data JPA репозитории
│   │   ├── service/          # Бизнес-логика (интерфейсы + реализации)
│   │   ├── rest/             # REST контроллеры API
│   │   ├── controller/       # Web контроллеры (Thymeleaf)
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── exception/        # Обработка исключений
│   │   ├── config/           # Конфигурация Spring
│   │   └── security/         # Spring Security компоненты
│   └── resources/
│       ├── templates/        # Thymeleaf HTML шаблоны
│       └── application.properties
└── test/                     # Unit и интеграционные тесты
```

## Запуск приложения

### Требования
- Java 21 или выше
- Maven 3.8+

### Запуск

```bash
# Перейти в директорию проекта
cd JavaCource

# Запустить приложение
.\mvnw.cmd spring-boot:run
```

Приложение запустится на `http://localhost:8080`

### Доступ из локальной сети

Приложение настроено на прослушивание всех сетевых интерфейсов (`server.address=0.0.0.0`).
Для доступа с других устройств используйте локальный IP-адрес компьютера: `http://192.168.x.x:8080`

## REST API

### Сотрудники (/api/employees)
| Метод  | URL                              | Описание                    |
|--------|----------------------------------|-----------------------------|
| GET    | /api/employees                   | Получить всех сотрудников   |
| GET    | /api/employees/{id}              | Получить сотрудника по ID   |
| POST   | /api/employees                   | Создать сотрудника          |
| PUT    | /api/employees/{id}              | Обновить сотрудника         |
| DELETE | /api/employees/{id}              | Удалить сотрудника          |
| GET    | /api/employees/department/{id}   | Сотрудники по отделу        |

### Отделы (/api/departments)
| Метод  | URL                              | Описание                    |
|--------|----------------------------------|-----------------------------|
| GET    | /api/departments                 | Все отделы                  |
| GET    | /api/departments/{id}            | Отдел по ID                 |
| POST   | /api/departments                 | Создать отдел               |
| PUT    | /api/departments/{id}            | Обновить отдел              |
| DELETE | /api/departments/{id}            | Удалить отдел               |

### Отчеты (/api/reports)
| Метод  | URL                              | Описание                    |
|--------|----------------------------------|-----------------------------|
| GET    | /api/reports                     | Все отчеты                  |
| GET    | /api/reports/{id}                | Отчет по ID                 |
| POST   | /api/reports/employee-report     | Создать отчет сотрудников   |
| DELETE | /api/reports/{id}                | Удалить отчет               |

## Веб-интерфейс

| Страница           | URL                  | Описание                           |
|--------------------|----------------------|------------------------------------|
| Главная            | /                    | Стартовая страница                 |
| Вход               | /login               | Аутентификация по email            |
| Регистрация        | /register            | Регистрация нового пользователя    |
| Сотрудники         | /employees           | Список и управление сотрудниками   |
| Отделы             | /departments         | Список отделов                     |
| Отчеты             | /reports             | Формирование отчетов               |
| Профиль            | /profile             | Данные текущего пользователя       |

## Учетные данные

После запуска автоматически создаются тестовые пользователи:

| Роль  | Email               | Пароль  |
|-------|---------------------|---------|
| Admin | admin@company.com   | admin   |
| User  | user@company.com    | user    |

Администратор имеет права на редактирование сотрудников и назначение ролей.

## Инициализация данных

При запуске приложение создает:
- Роли: ADMIN, USER
- Отделы: IT отдел, HR отдел, Финансовый отдел
- Тестовых сотрудников с привязкой к отделам
- Пользователей admin и user

Конфигурация данных по-умолчанию: `DataInitializer.java`

## Безопасность

- Пароли шифруются BCrypt
- Аутентификация по email
- Ролевая модель доступа (ADMIN/USER)
- Только администраторы могут редактировать сотрудников
- Администратор может назначать роль ADMIN другим пользователям

## Конфигурация

Основные настройки в `application.properties`:

```properties
# Сервер
server.port=8080
server.address=0.0.0.0

# База данных (dev профиль - H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

## Решение проблем

**Порт 8080 занят**
```properties
server.port=8081
```

**Нет доступа из сети**
Откройте порт 8080 в брандмауэре Windows:
```powershell
New-NetFirewallRule -DisplayName "Spring Boot" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow
```
