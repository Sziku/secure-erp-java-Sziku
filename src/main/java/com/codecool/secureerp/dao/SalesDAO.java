package com.codecool.secureerp.dao;

import com.codecool.secureerp.Util;
import com.codecool.secureerp.model.SalesModel;

import java.awt.font.TextHitInfo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {
    private final static int ID_TABLE_INDEX = 0;
    private final static int CUSTOMER_ID_TABLE_INDEX = 1;
    private final static int PRODUCT_TABLE_INDEX = 2;
    private final static int PRICE_TABLE_INDEX = 3;
    private final static int TRANSACTION_DATE_TABLE_INDEX = 4;
    private final static String DATA_FILE = "src/main/resources/sales.csv";
    public static String[] headers = {"Id", "Customer Id", "Product", "Price", "Transaction Date"};

    private static List<List<String>> allDataFromFile;
    private static List<SalesModel> salesDataList =new ArrayList<>();

    public static  List<List<String>> allDataFromFile(List<List<String>> allDataFromFile){
        try {
            allDataFromFile = Util.getAllDataFromFile(DATA_FILE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return allDataFromFile;
    }

    public static String[][] getSaleDataArray(){

        return Util.getUserDataArray(allDataFromFile(allDataFromFile),headers);
    }

    public static List<SalesModel> getSalesDataList(){
        for (List<String> userData:allDataFromFile(allDataFromFile)) {
            SalesModel salesModel = new SalesModel("","","",(float) 0,LocalDate.parse("2000-01-01"));
            salesModel.setId(userData.get(ID_TABLE_INDEX));
            salesModel.setCustomerId(userData.get(CUSTOMER_ID_TABLE_INDEX));
            salesModel.setProduct(userData.get(PRODUCT_TABLE_INDEX ));
            salesModel.setPrice(Float.parseFloat(userData.get(PRICE_TABLE_INDEX)));
            salesModel.setTransactionDate(LocalDate.parse(userData.get(TRANSACTION_DATE_TABLE_INDEX)));

            salesDataList.add(salesModel);
        }
        return salesDataList;
    }

    public static void setSalesDataList(List<SalesModel> salesDataList){
        try {
            Util.writeAllDaraToFile(ObjectListToString(salesDataList),DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String ObjectListToString(List<SalesModel> salesDataList){
        StringBuilder line= new StringBuilder();
        for (SalesModel salesModel:salesDataList) {
            line.append(salesModel.getId())
                    .append(";")
                    .append(salesModel.getCustomerId())
                    .append(";")
                    .append(salesModel.getProduct())
                    .append(";")
                    .append(salesModel.getPrice())
                    .append(";")
                    .append(salesModel.getTransactionDate())
                    .append("\n");
        }
        return line.toString();
    }

}
