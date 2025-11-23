package ru.danila.NauJava.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.Position;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    private EntityManager m_entityManager;

    @Override
    public List<Employee> findEmployeesByPositionTitle(String t_positionTitle) {
        CriteriaBuilder criteriaBuilder = m_entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        Join<Employee, Position> positionJoin = employeeRoot.join("m_position", JoinType.INNER);

        Predicate positionPredicate = criteriaBuilder.equal(positionJoin.get("m_title"), t_positionTitle);

        criteriaQuery.select(employeeRoot).where(positionPredicate);

        return m_entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Employee> findEmployeesWithSalaryAbove(Double t_minSalary) {
        CriteriaBuilder criteriaBuilder = m_entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        // Условие по зарплате
        Predicate salaryPredicate = criteriaBuilder.greaterThanOrEqualTo(employeeRoot.get("m_salary"), t_minSalary);

        criteriaQuery.select(employeeRoot).where(salaryPredicate);

        return m_entityManager.createQuery(criteriaQuery).getResultList();
    }
}
