package com.codecool.secureerp.controller;

import com.codecool.secureerp.Util;
import com.codecool.secureerp.dao.HRDAO;
import com.codecool.secureerp.dao.SalesDAO;
import com.codecool.secureerp.model.HRModel;
import com.codecool.secureerp.model.SalesModel;
import com.codecool.secureerp.view.TerminalView;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HRController {

    private static final List<HRModel> hrModels = HRDAO.hrDataList();

    public static void listEmployees() {
        System.out.println(Arrays.deepToString(HRDAO.getHrDataArray()));
        System.out.println(hrModels);
        TerminalView.printTable(HRDAO.getHrDataArray());
    }

    public static void addEmployee() {
        String[] userInputs = TerminalView.getInputs(new String[]{"Name", "Date of birth", "Department", "Clearance"});
        HRModel hrModelNew = new HRModel("","", LocalDate.parse("1900-01-01"), "", 0);

        String[] Ids = new String[hrModels.size()];
        int index = 0;
        for (HRModel hrModel : hrModels) {
            Ids[index] = hrModel.getId();
            index++;
        }
        hrModelNew.setId(Util.generateUniqueId(Ids));
        hrModelNew.setName(userInputs[0]);
        hrModelNew.setBirthDate(LocalDate.parse(userInputs[1]));
        hrModelNew.setDepartment((userInputs[2]));
        hrModelNew.setClearance(Integer.parseInt(userInputs[3]));

        hrModels.add(hrModelNew);
        HRDAO.setHrDataList(hrModels);
    }

    public static void updateEmployee() {
        String id= TerminalView.getInput("Give the User ID");
        int index = 0;
        for (HRModel hrModel : hrModels) {
            if (hrModel.getId().equals(id)){
                System.out.println(" |Current userID: " + hrModel.getId() + " |Current Name: " + hrModel.getName() + " |Current Date of Birth: " +
                        hrModel.getBirthDate() + " |Current department: " + hrModel.getDepartment() + " |Current clearance: " + hrModel.getClearance());
                String[] userInputs = TerminalView.getInputs(new String[]{"New userID", "New name", "New date of birth", "New department", "New Clearance"});
                hrModel.setId(userInputs[0]);
                hrModel.setName(userInputs[1]);
                hrModel.setBirthDate(LocalDate.parse((userInputs[2])));
                hrModel.setDepartment(userInputs[3]);
                hrModel.setClearance(Integer.parseInt(userInputs[4]));
            } else if(index==hrModels.size()){
                TerminalView.printErrorMessage("Invalid user ID\n");
            }
            index++;
        }
        HRDAO.setHrDataList(hrModels);
    }

    public static void deleteEmployee() {
        String id = TerminalView.getInput("Give user ID to delete it:");
        int index=0;
        for (HRModel hrModel : hrModels) {
            if (hrModel.getId().equals(id)){
                hrModels.remove(index);
                break;
            } else if(index==hrModels.size()){
                TerminalView.printErrorMessage("Invalid user ID\n");
            }
            index++;
        }
        HRDAO.setHrDataList(hrModels);
    }

    public static void getOldestAndYoungest() {
        LocalDate earliestBirthdate = hrModels.get(0).getBirthDate();
        String youngestName = hrModels.get(0).getName();
        LocalDate latestBirthdate = hrModels.get(0).getBirthDate();
        String oldestName = hrModels.get(0).getName();

        for (HRModel hrModel : hrModels) {
            if(hrModel.getBirthDate().isBefore(earliestBirthdate)) {
                earliestBirthdate = hrModel.getBirthDate();
                oldestName = hrModel.getName();
            }
            if(hrModel.getBirthDate().isAfter(latestBirthdate)) {
                latestBirthdate = hrModel.getBirthDate();
                youngestName = hrModel.getName();
            }
        }
        TerminalView.printGeneralResults(youngestName, "The youngest employee is");
        TerminalView.printGeneralResults(oldestName, "The oldest employee is");
    }

    public static void getAverageAge() {
        LocalDate currentDate = LocalDate.now();
        float sumOfAge = 0;
        float numberOfEmployees = 0;
        float averageAge;

        for (HRModel hrModel : hrModels) {
            sumOfAge += Period.between(hrModel.getBirthDate(), currentDate).getYears();
            numberOfEmployees++;
        }

        averageAge = sumOfAge / numberOfEmployees;

        TerminalView.printGeneralResults(String.valueOf(averageAge), "The average age of employees' is" );
    }

    public static void nextBirthdays() {
        List<String> employeeNames = new ArrayList<>();
        LocalDate userInputDate = LocalDate.parse(TerminalView.getInput("Please input a date"));
        for (HRModel hrModel : hrModels) {
            long weeks = ChronoUnit.WEEKS.between(userInputDate, hrModel.getBirthDate());
            if (weeks <= 2 && weeks >= 0) {
                employeeNames.add(hrModel.getName());
            }
        }
        if (employeeNames.size() == 0) {
            TerminalView.printErrorMessage("There are no birthdays within two weeks from input period");
        } else {
            TerminalView.printGeneralResults(employeeNames.toString(), "Employee whose birthday is in the next two weeks within input date");
        }
    }

    public static void countEmployeesWithClearance() {
        List<String> employeeNames = new ArrayList<>();
        int userInputClearance = Integer.parseInt(TerminalView.getInput("Please provide input"));
        for (HRModel hrModel : hrModels) {
            if(hrModel.getClearance() >= userInputClearance) {
                employeeNames.add(hrModel.getName());
            }
        }
        if (employeeNames.size() == 0) {
            TerminalView.printErrorMessage("There are no employees with that clearance level or above");
        } else {
            TerminalView.printGeneralResults(employeeNames.toString(), "The employees with the input clearance or above are");
        }
    }

    public static void countEmployeesPerDepartment() {
        int index = 0;
        String[] departments = new String[hrModels.size()];
        Map<String, Integer> employeesPerDepartment = new HashMap<>();

        for (HRModel hrModel : hrModels) {
            departments[index] = (hrModel.getDepartment());
            index++;
        }

        for (String department : departments) {
            if(employeesPerDepartment.containsKey(department)) {
                employeesPerDepartment.put(department, employeesPerDepartment.get(department) + 1);
            } else {
                employeesPerDepartment.put(department, 1);
            }
        }

        TerminalView.printGeneralResults(employeesPerDepartment.toString(), "Employees per department");
    }

    public static void runOperation(int option) {
        switch (option) {
            case 1: {
                listEmployees();
                break;
            }
            case 2: {
                addEmployee();
                break;
            }
            case 3: {
                updateEmployee();
                break;
            }
            case 4: {
                deleteEmployee();
                break;
            }
            case 5: {
                getOldestAndYoungest();
                break;
            }
            case 6: {
                getAverageAge();
                break;
            }
            case 7: {
                nextBirthdays();
                break;
            }
            case 8: {
                countEmployeesWithClearance();
                break;
            }
            case 9: {
                countEmployeesPerDepartment();
                break;
            }
            case 0:
                return;
            default:
                throw new IllegalArgumentException("There is no such option.");
        }
    }

    public static void displayMenu() {
        String[] options = {
                "Back to main menu",
                "List employees",
                "Add new employee",
                "Update employee",
                "Remove employee",
                "Oldest and youngest employees",
                "Employees average age",
                "Employees with birthdays in the next two weeks",
                "Employees with clearance level",
                "Employee numbers by department"
        };
        TerminalView.printMenu("Human Resources", options);
    }

    public static void menu() {
        int operation = -1;
        while (operation != 0) {
            displayMenu();
            String userInput = TerminalView.getInput("Select an operation");
            if (Util.tryParseInt(userInput)) {
                operation = Integer.parseInt(userInput);
                runOperation(operation);
            } else {
                TerminalView.printErrorMessage("This is not a number");
            }
        }
    }
}
