package ru.danila.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.danila.NauJava.dto.EmployeeDTO;
import ru.danila.NauJava.service.EmployeeService;

import java.util.List;

@Component
public class CommandProcessor {

    private final EmployeeService m_employeeService;

    @Autowired
    public CommandProcessor(EmployeeService t_employeeService) {
        this.m_employeeService = t_employeeService;
    }

    public void processCommand(String t_input) {
        String[] cmd = t_input.trim().split(" ");
        try {
            switch (cmd[0].toLowerCase()) {
                case "hire" -> {
                    // hire [id] [firstName] [lastName] [department] [position]
                    if (cmd.length < 6) {
                        break;
                    }
                    // Объединяем все аргументы после 4-го в название должности
                    String position = combineArguments(cmd, 5);
                    m_employeeService.hireEmployee(Long.valueOf(cmd[1]), cmd[2], cmd[3], cmd[4], position);
                }
                case "find" -> {
                    // find [id]
                    if (cmd.length < 2) {
                        break;
                    }
                }
                case "update-position" -> {
                    // update-position [id] [newPosition]
                    if (cmd.length < 3) {
                        break;
                    }
                    // Объединяем все аргументы после 2-го в новую должность
                    String newPosition = combineArguments(cmd, 2);
                    m_employeeService.updateEmployeePosition(Long.valueOf(cmd[1]), newPosition);
                }
                case "transfer" -> {
                    // transfer [id] [newDepartment]
                    if (cmd.length < 3) {
                        break;
                    }
                    // Объединяем все аргументы после 2-го в название отдела
                    String newDepartment = combineArguments(cmd, 2);
                    m_employeeService.transferEmployeeDepartment(Long.valueOf(cmd[1]), newDepartment);
                }
                case "delete" -> {
                    // Формат: delete [id]
                    if (cmd.length < 2) {
                        break;
                    }

                    try {
                        Long employeeId = Long.valueOf(cmd[1]);

                        // Проверяем существование перед удалением
                        EmployeeDTO employeeToDelete = m_employeeService.findEmployeeById(employeeId);
                        if (employeeToDelete == null) {
                            break;
                        }

                        // Если сотрудник найден - удаляем
                        m_employeeService.deleteEmployee(employeeId);
                    } catch (NumberFormatException e) {
                    }
                }
                case "list" -> {
                    // list
                    List<EmployeeDTO> employees = m_employeeService.getAllEmployees();
                    if (!employees.isEmpty()) {
                        employees.forEach(System.out::println);
                    }
                }
                case "help" -> printHelp();
            }
        } catch (Exception e) {
            System.err.println("Ошибка выполнения команды: " + e.getMessage());
        }
    }

    /**
     * Объединяет аргументы массива начиная с указанного индекса
     * @param t_args массив аргументов
     * @param t_startIndex начальный индекс
     * @return объединенная строка
     */
    private String combineArguments(String[] t_args, int t_startIndex) {
        if (t_startIndex >= t_args.length) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = t_startIndex; i < t_args.length; i++) {
            if (i > t_startIndex) {
                result.append(" ");
            }
            result.append(t_args[i]);
        }
        return result.toString();
    }

    private void printHelp() {
        System.out.println("""
            Доступные команды:
            hire [id] [firstName] [lastName] [department] [position] - добавить сотрудника
            find [id] - найти сотрудника по ID
            update-position [id] [newPosition] - обновить должность
            transfer [id] [newDepartment] - перевести в другой отдел
            delete [id] - удалить сотрудника
            list - показать всех сотрудников
            help - показать эту справку
            exit - выйти из приложения
            """);
    }
}