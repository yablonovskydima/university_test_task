package com.university.university.consoleHandler;

import com.university.university.DTO.LectorDto;
import com.university.university.entities.Degree;
import com.university.university.entities.Lector;
import com.university.university.services.LectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class LectorCommandExecutor {

    private final LectorService lectorService;
    private final Scanner scanner = new Scanner(System.in);

    public void executeGlobalSearch(String input) {
        String template = input.replace("Global search by", "").trim();
        List<LectorDto> lectors = lectorService.globalSearch(template);

        if (lectors.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            String result = lectors.stream()
                    .map(l -> l.getFirstname() + " " + l.getLastname())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            System.out.println(result);
        }
    }

    public void createLector() {
        Lector lector = inputLectorData(null);
        LectorDto created = lectorService.create(
                lector.getFirstname(),
                lector.getLastname(),
                lector.getDegree().name(),
                lector.getSalary()
        );
        System.out.println("Created lector: " + created);
    }

    public void updateLector() {
        Long id = readLong("Enter lector ID to update: ");
        LectorDto existingDto = lectorService.getById(id);

        Lector lector = new Lector();
        lector.setId(id);
        lector.setFirstname(existingDto.getFirstname());
        lector.setLastname(existingDto.getLastname());
        lector.setDegree(existingDto.getDegree());
        lector.setSalary(existingDto.getSalary());

        System.out.printf("Enter new firstname (or leave empty to keep '%s'): ", lector.getFirstname());
        String firstname = scanner.nextLine();
        if (!firstname.isBlank()) {
            lector.setFirstname(firstname);
        }

        System.out.printf("Enter new lastname (or leave empty to keep '%s'): ", lector.getLastname());
        String lastname = scanner.nextLine();
        if (!lastname.isBlank()) {
            lector.setLastname(lastname);
        }

        System.out.printf("Enter new degree (ASSISTANT, ASSOCIATE_PROFESSOR, PROFESSOR) (or leave empty to keep '%s'): ",
                lector.getDegree());
        String degreeInput = scanner.nextLine();
        if (!degreeInput.isBlank()) {
            try {
                lector.setDegree(Degree.valueOf(degreeInput.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid degree. Keeping previous value.");
            }
        }

        System.out.printf("Enter new salary (or leave empty to keep %.2f): ", lector.getSalary());
        String salaryInput = scanner.nextLine();
        if (!salaryInput.isBlank()) {
            try {
                lector.setSalary(Double.parseDouble(salaryInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Keeping previous salary.");
            }
        }

        LectorDto updated = lectorService.update(id, lector);
        System.out.println("Updated lector: " + updated);
    }


    public void deleteLector() {
        Long id = readLong("Enter lector ID to delete: ");
        lectorService.deleteById(id);
        System.out.println("Lector deleted.");
    }

    public void listLectors() {
        lectorService.getAll().forEach(System.out::println);
    }

    public void getLectorById() {
        Long id = readLong("Enter lector ID: ");
        System.out.println(lectorService.getById(id));
    }

    private Lector inputLectorData(Lector existing) {
        boolean isUpdate = existing != null;
        if (!isUpdate) existing = new Lector();

        String input;
        String fieldLabel;

        fieldLabel = "firstname";
        System.out.printf("Enter %s%s: ", fieldLabel, isUpdate ? " (" + existing.getFirstname() + ")" : "");
        input = scanner.nextLine();
        if (!input.isBlank()) existing.setFirstname(input);

        fieldLabel = "lastname";
        System.out.printf("Enter %s%s: ", fieldLabel, isUpdate ? " (" + existing.getLastname() + ")" : "");
        input = scanner.nextLine();
        if (!input.isBlank()) existing.setLastname(input);

        fieldLabel = "degree (ASSISTANT, ASSOCIATE_PROFESSOR, PROFESSOR)";
        System.out.printf("Enter %s%s: ", fieldLabel, isUpdate ? " (" + existing.getDegree() + ")" : "");
        input = scanner.nextLine();
        if (!input.isBlank()) {
            try {
                existing.setDegree(Degree.valueOf(input.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid degree. Keeping previous value.");
            }
        }

        fieldLabel = "salary";
        System.out.printf("Enter %s%s: ", fieldLabel, isUpdate ? String.format(" (%.2f)", existing.getSalary()) : "");
        input = scanner.nextLine();
        if (!input.isBlank()) {
            try {
                existing.setSalary(Double.parseDouble(input));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Keeping previous salary.");
            }
        }

        return existing;
    }

    private Long readLong(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return readLong(prompt);
        }
    }
}
