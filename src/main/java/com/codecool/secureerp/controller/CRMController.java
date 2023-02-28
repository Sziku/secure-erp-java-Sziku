package com.codecool.secureerp.controller;

import com.codecool.secureerp.Util;
import com.codecool.secureerp.dao.CRMDAO;
import com.codecool.secureerp.model.CRMModel;
import com.codecool.secureerp.view.TerminalView;

import java.util.List;

public class CRMController {
    private static final List<CRMModel> crmModels = CRMDAO.crmDataList();

    public static void listCustomers() {
        TerminalView.printTable(CRMDAO.getcrmDataArray());
    }

    public static void addCustomer() {
        String[] userInputs = TerminalView.getInputs(new String[]{"Name", "Email", "Subscriped"});
        CRMModel crmModelNew = new CRMModel("", "", "", false);

        String[] Ids = new String[crmModels.size()];
        int index = 0;
        for (CRMModel crmModel : crmModels) {
            Ids[index] = crmModel.getId();
            index++;
        }
        crmModelNew.setId(Util.generateUniqueId(Ids));

        if (Util.nameSpellChecker(userInputs[0])) {
            crmModelNew.setName(userInputs[0]);
        } else {
            TerminalView.printErrorMessage("Wrong name format. Name only has english letters");
            addCustomer();
        }

        if (Util.emailSpellChecker(userInputs[1])) {
            crmModelNew.setEmail(userInputs[1]);
        } else {
            TerminalView.printErrorMessage("Wrong email format.");
            addCustomer();
        }
        System.out.println(userInputs[2]);
        if (userInputs[2].equals("true")) {
            crmModelNew.setSubscribed(true);
        } else if (userInputs[2].equals("false")) {
            crmModelNew.setSubscribed(false);
        } else {
            TerminalView.printErrorMessage("Wrong subscribed format (true or false).");
        }

        crmModels.add(crmModelNew);
        CRMDAO.setCrmDataDataList(crmModels);

    }

    public static void updateCustomers() {
        String id = TerminalView.getInput("Give the User ID");
        int index = 0;
        for (CRMModel crmodel : crmModels) {
            if (crmodel.getId().equals(id)) {
                System.out.println("Current name: " + crmodel.getName() + " Current email: " + crmodel.getName() + " Current Subscribed: " + crmodel.isSubscribed());
                String[] userInputs = TerminalView.getInputs(new String[]{"New Name", "New Email", "New Subscriped"});
                if (Util.nameSpellChecker(userInputs[0])) {
                    crmodel.setName(userInputs[0]);
                } else {
                    TerminalView.printErrorMessage("Wrong name format. Name only has english letters");
                    addCustomer();
                }

                if (Util.emailSpellChecker(userInputs[1])) {
                    crmodel.setEmail(userInputs[1]);
                } else {
                    TerminalView.printErrorMessage("Wrong email format.");
                    addCustomer();
                }
                System.out.println(userInputs[2]);
                if (userInputs[2].equals("true")) {
                    crmodel.setSubscribed(true);
                } else if (userInputs[2].equals("false")) {
                    crmodel.setSubscribed(false);
                } else {
                    TerminalView.printErrorMessage("Wrong subscribed format (true or false).");
                }
            } else if (index == crmModels.size()) {
                TerminalView.printErrorMessage("Invalid user ID\n");
            }

            index++;

        }

        CRMDAO.setCrmDataDataList(crmModels);
    }

    public static void deleteCustomers() {
        String id = TerminalView.getInput("Give user ID to delete it:");
        int index = 0;
        for (CRMModel crmModel : crmModels) {
            if (crmModel.getId().equals(id)) {
                crmModels.remove(index);
                break;
            } else if (index == crmModels.size()) {
                TerminalView.printErrorMessage("Invalid user ID\n");
            }
            index++;
        }

        CRMDAO.setCrmDataDataList(crmModels);
    }

    public static void getSubscribedEmails() {
        for (CRMModel crmModel : crmModels) {
            if (crmModel.isSubscribed()) {
                TerminalView.printGeneralResults(crmModel.getEmail(), "Subscribed email");
            }
        }
    }

    public static void runOperation(int option) {
        switch (option) {
            case 1: {
                listCustomers();
                break;
            }
            case 2: {
                addCustomer();
                break;
            }
            case 3: {
                updateCustomers();
                break;
            }
            case 4: {
                deleteCustomers();
                break;
            }
            case 5: {
                getSubscribedEmails();
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
                "List customers",
                "Add new customer",
                "Update customer",
                "Remove customer",
                "Subscribed customer emails"
        };
        TerminalView.printMenu("Customer Relationship", options);
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
