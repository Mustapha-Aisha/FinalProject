package com.FinalProject.NextGenFinalProject.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.info")
public class AppInfo {
    private String title;
    private String Description;
    private String version;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
}

//
//ASSIGNMENT 11
//Create an EmployeeBook application using Spring Boot:
//That takes the Employee details such as employee ID, employee name, department,
//designation, date of joining, date of birth, marital status, and, the date of marriage should be
//saved in the database only if the employee is married.
//The employee id of the employee for which the data needs to view should be entered and
//validated. If the employee id is correct, the details should be displayed else an error message
//should be displayed.
//Then Create an option to Bring out all list of all employees.
//An Option to Delete an employee from the database.
//An Option to Update an employee details.
//NOTE:
//● Make use of CRUD OPERATIONS
//● Create a SWAGGER DOCUMENTATION
//● Use an EXCEPTIONMAPPER to enable the employee not to leave any field empty