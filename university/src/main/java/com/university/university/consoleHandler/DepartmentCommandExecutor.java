package com.university.university.consoleHandler;

import com.university.university.DTO.DepartmentDto;
import com.university.university.DTO.DepartmentStatisticsDto;
import com.university.university.DTO.LectorDto;
import com.university.university.entities.Department;
import com.university.university.entities.Lector;
import com.university.university.services.DepartmentService;
import com.university.university.services.LectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DepartmentCommandExecutor {

    private final DepartmentService departmentService;
    private final LectorService lectorService;
    private final Scanner scanner = new Scanner(System.in);

    public void printHeadOfDepartment(String input) {
        String departmentName = input.replace("Who is head of department", "").trim();
        String headName = departmentService.getHeadOfDepartment(departmentName);
        System.out.println("Head of " + departmentName + " department is " + headName);
    }

    public void printDepartmentStatistics(String input) {
        String departmentName = input.split(" ")[1];
        DepartmentStatisticsDto stats = departmentService.getStatistics(departmentName);

        System.out.println("assistans - " + stats.getAssistants());
        System.out.println("associate professors - " + stats.getAssociateProfessors());
        System.out.println("professors - " + stats.getProfessors());
    }

    public void printAverageSalary(String input) {
        String departmentName = input.replace("Show the average salary for the department", "").replace(".", "").trim();
        Double avg = departmentService.getAverageSalary(departmentName);
        System.out.printf("The average salary of %s is %.2f%n", departmentName, avg);
    }

    public void printEmployeeCount(String input) {
        String departmentName = input.replace("Show count of employee for", "").replace(".", "").trim();
        Long count = departmentService.countEmployees(departmentName);
        System.out.println(count);
    }

    public void createDepartment() {
        System.out.print("Enter department name: ");
        String name = scanner.nextLine();

        System.out.print("Enter head of department ID: ");
        Long headId = Long.parseLong(scanner.nextLine());
        Lector head = createLectorFromDto(lectorService.getById(headId));
        head.setId(headId);

        System.out.print("Enter lector IDs to add to department (comma separated): ");
        String idsLine = scanner.nextLine().trim();
        Set<Lector> lectors = new HashSet<>();

        Lector lector;
        LectorDto lectorDto;
        Long lectorId;

        if (!idsLine.isEmpty()) {
            String[] idStrings = idsLine.split(",");
            for (String idStr : idStrings) {
                lectorId = Long.parseLong(idStr.trim());
                lectorDto = lectorService.getById(lectorId);
                lector = createLectorFromDto(lectorDto);
                lector.setId(lectorId);
                lectors.add(lector);
            }
        }

        lectors.add(head);

        Department department = new Department();
        department.setDepartmentName(name);
        department.setHeadOfDepartment(head);
        department.setLectors(lectors);

        DepartmentDto created = departmentService.create(department);
        System.out.println("Created department: " + created);
    }

    public void updateDepartment() {
        System.out.print("Enter department ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());

        DepartmentDto existingDto = departmentService.getById(id);
        Department department = new Department();
        department.setId(id);
        department.setDepartmentName(existingDto.getDepartmentName());

        System.out.printf("Enter new department name (or leave empty to keep '%s'): ", existingDto.getDepartmentName());
        String newName = scanner.nextLine();
        if (!newName.isBlank()) {
            department.setDepartmentName(newName);
        }

        System.out.print("Enter new head of department ID (or leave empty to keep current): ");
        String headIdInput = scanner.nextLine();
        Lector head = null;

        if (!headIdInput.isBlank()) {
            Long headId = Long.parseLong(headIdInput);
            head = createLectorFromDto(lectorService.getById(headId));
            head.setId(headId);
            department.setHeadOfDepartment(head);
        } else {
            LectorDto headDto = existingDto.getHeadOfDepartment();
            if (headDto != null) {
                head = createLectorFromDto(headDto);
                head.setId(headDto.getId());
                department.setHeadOfDepartment(head);
            }
        }

        System.out.print("Enter new lector IDs (comma separated), or leave empty to keep current list: ");
        String idsLine = scanner.nextLine();
        Set<Lector> lectors = new HashSet<>();

        Lector lector;
        LectorDto lectorDto;
        Long lectorId;

        if (!idsLine.isBlank()) {
            String[] idStrings = idsLine.split(",");
            for (String idStr : idStrings) {
                lectorId = Long.parseLong(idStr.trim());
                lectorDto = lectorService.getById(lectorId);
                lector = createLectorFromDto(lectorDto);
                lector.setId(lectorId);
                lectors.add(lector);
            }
        } else {
            for (LectorDto existingLectorDto : existingDto.getLectors()) {
                lector = createLectorFromDto(existingLectorDto);
                lector.setId(existingLectorDto.getId());
                lectors.add(lector);
            }
        }

        if (head != null) {
            lectors.add(head);
        }

        department.setLectors(lectors);

        DepartmentDto updated = departmentService.update(id, department);
        System.out.println("Updated department: " + updated);
    }

    public void deleteDepartment() {
        System.out.print("Enter department ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        departmentService.deleteById(id);
        System.out.println("Department deleted.");
    }

    public void listDepartments() {
        List<DepartmentDto> all = departmentService.getAll();
        all.forEach(System.out::println);
    }

    public void getDepartmentById() {
        System.out.print("Enter department ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        DepartmentDto dto = departmentService.getById(id);
        System.out.println(dto);
    }

    private Lector createLectorFromDto(LectorDto dto) {
        return new Lector(dto.getFirstname(), dto.getLastname(), dto.getDegree(), dto.getSalary());
    }
}
