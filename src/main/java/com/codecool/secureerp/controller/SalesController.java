package com.codecool.secureerp.controller;

import com.codecool.secureerp.Util;
import com.codecool.secureerp.dao.SalesDAO;
import com.codecool.secureerp.model.SalesModel;
import com.codecool.secureerp.view.TerminalView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesController {
    private static  List<SalesModel> salesModels = SalesDAO.getSalesDataList();

    public static void listTransactions() {
        System.out.println(Arrays.deepToString(SalesDAO.getSaleDataArray()));
        System.out.println(salesModels);
        TerminalView.printTable(SalesDAO.getSaleDataArray());
    }

    public static void addTransaction() {
        String[] userInputs = TerminalView.getInputs(new String[]{"Customer Id", "Product", "Price", "Transaction Date"});
        SalesModel salesModelNew = new SalesModel("","","",(float) 0, LocalDate.parse("2000-01-01"));

        String[] Ids = new String[salesModels.size()];
        int index=0;
        for (SalesModel crmModel:salesModels) {
            Ids[index]=crmModel.getId();
            index++;
        }
        salesModelNew.setId(Util.generateUniqueId(Ids));
        salesModelNew.setCustomerId(userInputs[0]);
        salesModelNew.setProduct(userInputs[1]);
        try {
            salesModelNew.setPrice(Float.parseFloat(userInputs[2]));
        }catch (NumberFormatException e){
            TerminalView.printErrorMessage("Wrong price format. Number must be decimal");
        }

        try {
            salesModelNew.setTransactionDate(LocalDate.parse(userInputs[3]));
        }catch (DateTimeParseException e){
            TerminalView.printErrorMessage("Wrong time format. (YYYY-MM-DD)");
            addTransaction();
        }
        salesModels.add(salesModelNew);
        SalesDAO.setSalesDataList(salesModels);
    }

    public static void updateTransactions() {
        String id= TerminalView.getInput("Give the User ID");
        int index = 0;
        for (SalesModel salesModel : salesModels) {
            if (salesModel.getId().equals(id)){
                System.out.println("Current costumerID: "+salesModel.getCustomerId()+" Current product: "+salesModel.getProduct()+" Current price: "+salesModel.getPrice()+" Current Transaction date: "+salesModel.getTransactionDate());
                String[] userInputs = TerminalView.getInputs(new String[]{"New costumerID", "New product", "New price","New Transaction date"});
                salesModel.setCustomerId(userInputs[0]);
                salesModel.setProduct(userInputs[1]);
                try {
                    salesModel.setPrice(Float.parseFloat(userInputs[2]));
                }catch (NumberFormatException e){
                    TerminalView.printErrorMessage("Wrong price format. Number must be decimal");
                }

                try {
                    salesModel.setTransactionDate(LocalDate.parse(userInputs[3]));
                }catch (DateTimeParseException e){
                    TerminalView.printErrorMessage("Wrong time format. (YYYY-MM-DD)");
                    addTransaction();
                };
            } else if(index==salesModels.size()){
                TerminalView.printErrorMessage("Invalid user ID\n");
            }
            index++;
        }
        SalesDAO.setSalesDataList(salesModels);
    }

    public static void deleteTransactions() {
        String id = TerminalView.getInput("Give user ID to delete it:");
        int index=0;
        for (SalesModel salesModel : salesModels) {
            if (salesModel.getId().equals(id)){
                salesModels.remove(index);
                break;
            } else if(index==salesModels.size()){
                TerminalView.printErrorMessage("Invalid user ID\n");
            }
            index++;
        }
        SalesDAO.setSalesDataList(salesModels);
    }

    public static void getBiggestRevenueTransaction() {
        float biggest=0;
        int index=0;
        int indexBiggest=0;
        for (SalesModel salesModel:salesModels) {
            if(salesModel.getPrice()>biggest){
                biggest=salesModel.getPrice();
                indexBiggest=index;
            }
            index++;
        }
        TerminalView.printGeneralResults(salesModels.get(indexBiggest).getId(), "The biggest revenue transaction: ");
    }

    public static void getBiggestRevenueProduct() {
        float max=0;
        String[] productName =new String[]{""};
        Map<String, Float> productsWithPriceSum = new HashMap<>();
        for (SalesModel salesModel: salesModels){
            if(productsWithPriceSum.containsKey(salesModel.getProduct())){
                productsWithPriceSum.put(salesModel.getProduct(), productsWithPriceSum.get(salesModel.getProduct())+salesModel.getPrice());
            }else {
                productsWithPriceSum.put(salesModel.getProduct(), salesModel.getPrice());
            }
        }
        for (Map.Entry<String,Float> entry : productsWithPriceSum.entrySet()){
            if (entry.getValue()>max){
                max= entry.getValue();
                productName[0]=entry.getKey();
            }
        }
        TerminalView.printGeneralResults(productName[0], "Product that made the biggest revenue altogether: ");
    }

    public static void countTransactionsBetween() {
        String[] userInputs=TerminalView.getInputs(new String[]{"Start date: ", "End date: "});
        int transactionCount =0;
        for (SalesModel salesModel: salesModels){
            if(salesModel.getTransactionDate().isAfter(LocalDate.parse(userInputs[0])) && salesModel.getTransactionDate().isBefore(LocalDate.parse(userInputs[1]))){
                transactionCount++;
            }

        }
        TerminalView.printGeneralResults(String.valueOf(transactionCount), "Transactions between two dates");
    }

    public static void sumTransactionsBetween() {
        String[] userInputs=TerminalView.getInputs(new String[]{"Start date: ", "End date: "});
        float priceSum =0;
        for (SalesModel salesModel: salesModels){
            if(salesModel.getTransactionDate().isAfter(LocalDate.parse(userInputs[0])) && salesModel.getTransactionDate().isBefore(LocalDate.parse(userInputs[1]))){
                priceSum+=salesModel.getPrice();
            }

        }
        TerminalView.printGeneralResults(String.valueOf(priceSum), "Transactions between two dates");
    }

    public static void runOperation(int option) {
        switch (option) {
            case 1: {
                listTransactions();
                break;
            }
            case 2: {
                addTransaction();
                break;
            }
            case 3: {
                updateTransactions();
                break;
            }
            case 4: {
                deleteTransactions();
                break;
            }
            case 5: {
                getBiggestRevenueTransaction();
                break;
            }
            case 6: {
                getBiggestRevenueProduct();
                break;
            }
            case 7: {
                countTransactionsBetween();
                break;
            }
            case 8: {
                sumTransactionsBetween();
                break;
            }
            case 0:
                return;
            default:
                throw new IllegalArgumentException("There is no such option");
        }
    }

    public static void displayMenu() {
        String[] options = {
                "Back to main menu",
                "List transactions",
                "Add new transaction",
                "Update transaction",
                "Remove transaction",
                "Get the transaction that made the biggest revenue",
                "Get the product that made the biggest revenue altogether",
                "Count number of transactions between",
                "Sum the price of transactions between"
        };

        TerminalView.printMenu("Sales", options);
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
