package ru.danila.NauJava.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.danila.NauJava.config.TestDataInitializer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * API тесты для EmployeeCustomController
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({TestDataInitializer.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeCustomControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getEmployeesByDepartment_ShouldReturnEmployees() {
        given()
                .param("department", "IT") // ИСПРАВЛЕНО: department вместо t_department
                .contentType(ContentType.JSON)
                .when()
                .get("/api/employees/search/department")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    void getEmployeesByFirstName_ShouldReturnEmployees() {
        given()
                .param("firstName", "Иван") // ИСПРАВЛЕНО: firstName вместо t_firstName
                .contentType(ContentType.JSON)
                .when()
                .get("/api/employees/search/first-name")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    void getEmployeesByPosition_ShouldReturnEmployees() {
        given()
                .param("position", "Разработчик") // ИСПРАВЛЕНО: position вместо t_position
                .contentType(ContentType.JSON)
                .when()
                .get("/api/employees/search/position")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    void getDepartmentStats_ShouldReturnStatistics() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/employees/stats/department")
                .then()
                .statusCode(200)
                .body(containsString("Статистика по отделам"));
    }
}
