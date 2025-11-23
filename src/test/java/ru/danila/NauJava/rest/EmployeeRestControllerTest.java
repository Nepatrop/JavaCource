package ru.danila.NauJava.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // ТОЛЬКО тестовый профиль
class EmployeeRestControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getAllEmployees_ShouldReturnOkStatus() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/employees")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    void createEmployee_ShouldReturnSuccess() {
        String employeeJson = """
            {
                "id": 100,
                "firstName": "Тестовый",
                "lastName": "Сотрудник",
                "department": "IT",
                "position": "Разработчик"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(employeeJson)
                .when()
                .post("/api/employees")
                .then()
                .statusCode(200)
                .body(containsString("Сотрудник создан"));
    }
}
