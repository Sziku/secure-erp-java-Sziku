package com.codecool.secureerp.dao;

import com.codecool.secureerp.Util;
import com.codecool.secureerp.model.HRModel;
import com.codecool.secureerp.model.SalesModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HRDAO {
    private final static int ID_TABLE_INDEX = 0;
    private final static int NAME_TABLE_INDEX = 1;
    private final static int BIRTH_DATE_TABLE_INDEX = 2;
    private final static int DEPARTMENT_TABLE_INDEX = 3;
    private final static int CLEARANCE_TABLE_INDEX = 4;
    private final static String DATA_FILE = "src/main/resources/hr.csv";
    public static String[] headers = {"Id", "Name", "Date of birth", "Department", "Clearance"};

    private static List<List<String>> allDataFromFile;

    private static List<HRModel> hrDataList = new ArrayList<>();

    public static  List<List<String>> allDataFromFile(List<List<String>> allDataFromFile){
        try {
            allDataFromFile = Util.getAllDataFromFile(DATA_FILE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return allDataFromFile;
    }

    public static String[][] getHrDataArray(){
        return Util.getUserDataArray(allDataFromFile(allDataFromFile),headers);
    }

    public static List<HRModel> hrDataList(){
        for (List<String> userData : allDataFromFile(allDataFromFile)) {
            HRModel hrModel = new HRModel("","", LocalDate.parse("1900-01-01"), "", 0);
            hrModel.setId(userData.get(ID_TABLE_INDEX));
            hrModel.setName(userData.get(NAME_TABLE_INDEX));
            hrModel.setBirthDate(LocalDate.parse(userData.get(BIRTH_DATE_TABLE_INDEX)));
            hrModel.setDepartment(userData.get(DEPARTMENT_TABLE_INDEX));
            hrModel.setClearance(Integer.parseInt(userData.get(CLEARANCE_TABLE_INDEX)));

            hrDataList.add(hrModel);
        }
        return hrDataList;
    }

    public static void setHrDataList(List<HRModel> hrDataList){
        try {
            Util.writeAllDaraToFile(ObjectListToString(hrDataList),DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String ObjectListToString(List<HRModel> hrDataList){
        StringBuilder line= new StringBuilder();
        for (HRModel hrModel : hrDataList) {
            line.append(hrModel.getId())
                    .append(";")
                    .append(hrModel.getName())
                    .append(";")
                    .append(hrModel.getBirthDate())
                    .append(";")
                    .append(hrModel.getDepartment())
                    .append(";")
                    .append(hrModel.getClearance())
                    .append("\n");
        }
        return line.toString();
    }
}
