package ru.danila.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.danila.NauJava.entity.Employee;
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
                        System.out.println("Ошибка: неверное количество аргументов. Используйте: hire [id] [firstName] [lastName] [department] [position]");
                        break;
                    }
                    // Объединяем все аргументы после 4-го в название должности
                    String position = combineArguments(cmd, 5);
                    m_employeeService.hireEmployee(Long.valueOf(cmd[1]), cmd[2], cmd[3], cmd[4], position);
                    System.out.println("Сотрудник успешно добавлен.");
                }
                case "find" -> {
                    // find [id]
                    if (cmd.length < 2) {
                        System.out.println("Ошибка: укажите ID сотрудника. Используйте: find [id]");
                        break;
                    }
                    Employee employee = m_employeeService.findEmployeeById(Long.valueOf(cmd[1]));
                    System.out.println(employee != null ? employee : "Сотрудник не найден.");
                }
                case "update-position" -> {
                    // update-position [id] [newPosition]
                    if (cmd.length < 3) {
                        System.out.println("Ошибка: неверное количество аргументов. Используйте: update-position [id] [newPosition]");
                        break;
                    }
                    // Объединяем все аргументы после 2-го в новую должность
                    String newPosition = combineArguments(cmd, 2);
                    m_employeeService.updateEmployeePosition(Long.valueOf(cmd[1]), newPosition);
                    System.out.println("Должность сотрудника обновлена.");
                }
                case "transfer" -> {
                    // transfer [id] [newDepartment]
                    if (cmd.length < 3) {
                        System.out.println("Ошибка: неверное количество аргументов. Используйте: transfer [id] [newDepartment]");
                        break;
                    }
                    // Объединяем все аргументы после 2-го в название отдела
                    String newDepartment = combineArguments(cmd, 2);
                    m_employeeService.transferEmployeeDepartment(Long.valueOf(cmd[1]), newDepartment);
                    System.out.println("Сотрудник переведен в другой отдел.");
                }
                case "delete" -> {
                    // Формат: delete [id]
                    if (cmd.length < 2) {
                        System.out.println("Ошибка: укажите ID сотрудника. Используйте: delete [id]");
                        break;
                    }

                    try {
                        Long employeeId = Long.valueOf(cmd[1]);

                        // Проверяем существование перед удалением
                        Employee employeeToDelete = m_employeeService.findEmployeeById(employeeId);
                        if (employeeToDelete == null) {
                            System.out.println("Ошибка: Сотрудник с ID " + employeeId + " не найден");
                            break;
                        }

                        // Если сотрудник найден - удаляем
                        m_employeeService.deleteEmployee(employeeId);
                        System.out.println("Сотрудник " + employeeToDelete.getFirstName() + " " + employeeToDelete.getLastName() + " удален.");

                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: неверный формат ID. ID должен быть числом.");
                    }
                }
                case "list" -> {
                    // list
                    List<Employee> employees = m_employeeService.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("Список сотрудников пуст.");
                    } else {
                        System.out.println("\nСписок всех сотрудников\n");
                        employees.forEach(System.out::println);
                    }
                }
                case "help" -> printHelp();
                default -> System.out.println("Неизвестная команда. Введите 'help' для списка команд.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: неверный формат ID. ID должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка выполнения команды: " + e.getMessage());
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