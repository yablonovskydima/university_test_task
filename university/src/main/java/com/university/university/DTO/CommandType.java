package com.university.university.DTO;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public enum CommandType {
    WHO_IS_HEAD("Who is head of department"),
    STATISTICS("Show .* statistics"),
    AVG_SALARY("Show the average salary for the department"),
    EMPLOYEE_COUNT("Show count of employee for"),
    GLOBAL_SEARCH("Global search by"),
    LIST_DEPARTMENTS("list departments"),
    CREATE_DEPARTMENT("create department"),
    UPDATE_DEPARTMENT("update department"),
    DELETE_DEPARTMENT("delete department"),
    GET_DEPARTMENT("get department"),
    LIST_LECTORS("list lectors"),
    CREATE_LECTOR("create lector"),
    UPDATE_LECTOR("update lector"),
    DELETE_LECTOR("delete lector"),
    GET_LECTOR("get lector");

    private final String pattern;

    public static Optional<CommandType> parse(String input) {
        return Arrays.stream(values())
                .filter(c -> input.matches("(?i)" + c.pattern + ".*"))
                .findFirst();
    }
}
