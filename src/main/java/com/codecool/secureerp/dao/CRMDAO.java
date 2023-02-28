package com.codecool.secureerp.dao;

import com.codecool.secureerp.Util;
import com.codecool.secureerp.model.CRMModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CRMDAO {
    private final static int ID_TABLE_INDEX = 0;
    private final static int NAME_TABLE_INDEX = 1;
    private final static int EMAIL_TABLE_INDEX = 2;
    private final static int SUBSCRIBED_TABLE_INDEX = 3;
    private final static String DATA_FILE = "src/main/resources/crm.csv";
    public static String[] headers = {"Id", "Name", "Email", "Subscribed"};

    private static List<List<String>> allDataFromFile;
    private static List<CRMModel> crmDataList = new ArrayList<>();

    public static List<List<String>> allDataFromFile(List<List<String>> allDataFromFile) {
        try {
            allDataFromFile = Util.getAllDataFromFile(DATA_FILE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return allDataFromFile;
    }


    public static String[][] getcrmDataArray() {

        return Util.getUserDataArray(allDataFromFile(allDataFromFile), headers);
    }

    public static List<CRMModel> crmDataList() {
        for (List<String> userData : allDataFromFile(allDataFromFile)) {
            CRMModel crmModel = new CRMModel("", "", "", false);
            crmModel.setId(userData.get(ID_TABLE_INDEX));
            crmModel.setName(userData.get(NAME_TABLE_INDEX));
            crmModel.setEmail(userData.get(EMAIL_TABLE_INDEX));
            crmModel.setSubscribed(!userData.get(SUBSCRIBED_TABLE_INDEX).equals("0"));

            crmDataList.add(crmModel);
        }
        return crmDataList;
    }

    public static void setCrmDataDataList(List<CRMModel> crmDataList) {
        try {
            Util.writeAllDaraToFile(ObjectListToString(crmDataList), DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String ObjectListToString(List<CRMModel> crmDataList) {
        StringBuilder line = new StringBuilder();
        for (CRMModel crmModel : crmDataList) {
            line.append(crmModel.getId())
                    .append(";")
                    .append(crmModel.getName())
                    .append(";")
                    .append(crmModel.getEmail())
                    .append(";")
                    .append(crmModel.isSubscribed() ? "1" : "0")
                    .append("\n");
        }
        return line.toString();
    }


}
