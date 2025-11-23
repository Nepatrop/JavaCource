package ru.danila.NauJava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.danila.NauJava.dao.EmployeeRepository;
import ru.danila.NauJava.entity.Employee;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для EmployeeServiceImpl
 * Покрывают положительные и негативные сценарии
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee testEmployee;
    private List<Employee> testEmployees;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("Иван");
        testEmployee.setLastName("Петров");
        testEmployee.setDepartment("IT");
        testEmployee.setPosition("Разработчик");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Мария");
        employee2.setLastName("Сидорова");
        employee2.setDepartment("HR");
        employee2.setPosition("Менеджер");

        testEmployees = Arrays.asList(testEmployee, employee2);
    }

    // ПОЛОЖИТЕЛЬНЫЕ СЦЕНАРИИ

    @Test
    void hireEmployee_ShouldCreateEmployee_WhenValidData() {
        // Given
        when(employeeRepository.read(anyLong())).thenReturn(null);

        // When
        employeeService.hireEmployee(1L, "Иван", "Петров", "IT", "Разработчик");

        // Then
        verify(employeeRepository, times(1)).create(any(Employee.class));
    }

    @Test
    void findEmployeeById_ShouldReturnEmployee_WhenEmployeeExists() {
        // Given
        when(employeeRepository.read(1L)).thenReturn(testEmployee);

        // When
        Employee result = employeeService.findEmployeeById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Иван", result.getFirstName());
        assertEquals("Петров", result.getLastName());
        assertEquals("IT", result.getDepartment());
    }

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() {
        // Given
        when(employeeRepository.findAll()).thenReturn(testEmployees);

        // When
        List<Employee> result = employeeService.getAllEmployees();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Иван", result.get(0).getFirstName());
        assertEquals("Мария", result.get(1).getFirstName());
    }

    @Test
    void updateEmployeePosition_ShouldUpdatePosition_WhenEmployeeExists() {
        // Given
        when(employeeRepository.read(1L)).thenReturn(testEmployee);

        // When
        employeeService.updateEmployeePosition(1L, "Старший разработчик");

        // Then
        verify(employeeRepository, times(1)).update(any(Employee.class));
    }

    @Test
    void transferEmployeeDepartment_ShouldUpdateDepartment_WhenEmployeeExists() {
        // Given
        when(employeeRepository.read(1L)).thenReturn(testEmployee);

        // When
        employeeService.transferEmployeeDepartment(1L, "QA");

        // Then
        verify(employeeRepository, times(1)).update(any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldDeleteEmployee_WhenEmployeeExists() {
        // Given
        when(employeeRepository.read(1L)).thenReturn(testEmployee);

        // When
        employeeService.deleteEmployee(1L);

        // Then
        verify(employeeRepository, times(1)).delete(1L);
    }

    // НЕГАТИВНЫЕ СЦЕНАРИИ

    @Test
    void findEmployeeById_ShouldReturnNull_WhenEmployeeNotExists() {
        // Given
        when(employeeRepository.read(999L)).thenReturn(null);

        // When
        Employee result = employeeService.findEmployeeById(999L);

        // Then
        assertNull(result);
    }

    @Test
    void updateEmployeePosition_ShouldNotUpdate_WhenEmployeeNotExists() {
        // Given
        when(employeeRepository.read(999L)).thenReturn(null);

        // When
        employeeService.updateEmployeePosition(999L, "Новая должность");

        // Then
        verify(employeeRepository, never()).update(any(Employee.class));
    }

    @Test
    void transferEmployeeDepartment_ShouldNotUpdate_WhenEmployeeNotExists() {
        // Given
        when(employeeRepository.read(999L)).thenReturn(null);

        // When
        employeeService.transferEmployeeDepartment(999L, "Новый отдел");

        // Then
        verify(employeeRepository, never()).update(any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldNotDelete_WhenEmployeeNotExists() {
        // Given
        when(employeeRepository.read(999L)).thenReturn(null);

        // When
        employeeService.deleteEmployee(999L);

        // Then
        verify(employeeRepository, never()).delete(999L);
    }

    @Test
    void getAllEmployees_ShouldReturnEmptyList_WhenNoEmployees() {
        // Given
        when(employeeRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Employee> result = employeeService.getAllEmployees();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void hireEmployee_ShouldNotCreate_WhenEmployeeAlreadyExists() {
        // Given
        when(employeeRepository.read(1L)).thenReturn(testEmployee);

        // When
        employeeService.hireEmployee(1L, "Иван", "Петров", "IT", "Разработчик");

        // Then - не должно создавать дубликат
        verify(employeeRepository, never()).create(any(Employee.class));
    }
}
