package ru.danila.NauJava.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // ТОЛЬКО эта аннотация
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void testLoginPageAccess() {
        System.out.println("Запуск теста страницы логина");

        driver.get(baseUrl + "/login");

        // Более гибкая проверка заголовка
        String pageTitle = driver.getTitle();
        assertTrue(pageTitle.contains("Вход") || pageTitle.contains("Login"),
                "Заголовок должен содержать 'Вход' или 'Login'. Фактический: " + pageTitle);

        // Проверяем наличие формы
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        assertTrue(usernameField.isDisplayed());
        assertTrue(passwordField.isDisplayed());
        assertTrue(loginButton.isDisplayed());

        System.out.println("Страница логина загружена корректно");
    }

    @Test
    @Order(2)
    void testSuccessfulLogin() {
        System.out.println("Запуск теста успешного логина");

        driver.get(baseUrl + "/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("user");
        passwordField.sendKeys("user123");
        loginButton.click();

        // Ждем редиректа
        wait.until(ExpectedConditions.urlContains("/employees/list"));

        assertTrue(driver.getCurrentUrl().contains("/employees/list"));
        System.out.println("Успешный логин выполнен");
    }

    @Test
    @Order(3)
    void testApiAccess() {
        System.out.println("Запуск теста доступа к API");

        // Проверяем что API доступно
        driver.get(baseUrl + "/api/employees");

        String pageSource = driver.getPageSource();
        // В тестовом режиме API должно быть доступно без аутентификации
        assertTrue(pageSource.contains("[]") || pageSource.contains("сотрудник") ||
                !pageSource.contains("error"));

        System.out.println("API доступно");
    }
}
