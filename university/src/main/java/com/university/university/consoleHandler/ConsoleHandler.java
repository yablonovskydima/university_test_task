package com.university.university.consoleHandler;

import com.university.university.DTO.CommandType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ConsoleHandler implements CommandLineRunner {
    private final DepartmentCommandExecutor departmentExecutor;
    private final LectorCommandExecutor lectorExecutor;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to University Console. Type a command:");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Exiting...");
                break;
            }

            try {
                handleCommand(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleCommand(String input) {
        CommandType.parse(input).ifPresentOrElse(command -> {
            switch (command) {
                case WHO_IS_HEAD -> departmentExecutor.printHeadOfDepartment(input);
                case STATISTICS -> departmentExecutor.printDepartmentStatistics(input);
                case AVG_SALARY -> departmentExecutor.printAverageSalary(input);
                case EMPLOYEE_COUNT -> departmentExecutor.printEmployeeCount(input);
                case GLOBAL_SEARCH -> lectorExecutor.executeGlobalSearch(input);
                case LIST_DEPARTMENTS -> departmentExecutor.listDepartments();
                case CREATE_DEPARTMENT -> departmentExecutor.createDepartment();
                case UPDATE_DEPARTMENT -> departmentExecutor.updateDepartment();
                case DELETE_DEPARTMENT -> departmentExecutor.deleteDepartment();
                case GET_DEPARTMENT -> departmentExecutor.getDepartmentById();
                case LIST_LECTORS -> lectorExecutor.listLectors();
                case CREATE_LECTOR -> lectorExecutor.createLector();
                case UPDATE_LECTOR -> lectorExecutor.updateLector();
                case DELETE_LECTOR -> lectorExecutor.deleteLector();
                case GET_LECTOR -> lectorExecutor.getLectorById();
            }
        }, () -> System.out.println("Unknown command."));
    }


}
