package ru.danila.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.danila.NauJava.entity.Department;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.Position;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.EmployeeRepository;
import ru.danila.NauJava.repository.PositionRepository;

import java.time.LocalDate;

@Component
public class JpaDataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Очищаем данные (для демонстрации)
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
        positionRepository.deleteAll();

        // Создаем отделы
        Department itDepartment = new Department("IT", "Information Technology");
        Department hrDepartment = new Department("HR", "Human Resources");
        Department financeDepartment = new Department("Finance", "Financial Department");

        departmentRepository.save(itDepartment);
        departmentRepository.save(hrDepartment);
        departmentRepository.save(financeDepartment);

        // Создаем должности
        Position developerPosition = new Position("Developer", "Software Developer", "G10");
        Position managerPosition = new Position("Manager", "Department Manager", "G12");
        Position analystPosition = new Position("Analyst", "Business Analyst", "G9");

        positionRepository.save(developerPosition);
        positionRepository.save(managerPosition);
        positionRepository.save(analystPosition);

        // Создаем сотрудников
        Employee emp1 = new Employee("Иван", "Петров", "ivan@company.com", LocalDate.now(), 50000.0);
        emp1.setDepartment(itDepartment);
        emp1.setPosition(developerPosition);

        Employee emp2 = new Employee("Мария", "Сидорова", "maria@company.com", LocalDate.now(), 70000.0);
        emp2.setDepartment(hrDepartment);
        emp2.setPosition(managerPosition);

        Employee emp3 = new Employee("Алексей", "Козлов", "alexey@company.com", LocalDate.now(), 60000.0);
        emp3.setDepartment(financeDepartment);
        emp3.setPosition(analystPosition);

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        employeeRepository.save(emp3);

        System.out.println("JPA тестовые данные созданы");
        System.out.println("Отделов: " + departmentRepository.count());
        System.out.println("Должностей: " + positionRepository.count());
        System.out.println("Сотрудников: " + employeeRepository.count());
    }
}
