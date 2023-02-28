package com.codecool.secureerp.view;

import java.util.Scanner;

public class TerminalView {

    private static final Scanner scanner = new Scanner(System.in);

    public static void printMessage(String message) {

        System.out.println(message);
    }

    public static void printMenu(String title, String[] options) {
        System.out.println(title+":");
        for (int i=1; i< options.length; i++){
            System.out.printf("(%d) %s\n",i,options[i]);
        }
        System.out.println("(0) "+options[0]);
    }

    public static void printGeneralResults(String result, String label) {

        System.out.printf("%s: %s\n",label,result);
    }

    public static void printTable(String[][] table) {
        int[] maxCharacterWordIantheColumn = getMaxCharacterWordIantheColumn(table);
        nonWordLine(maxCharacterWordIantheColumn,"/", "\\");
        for (int i = 0; i < table.length; i++) {
            if (i > 0) {
                nonWordLine(maxCharacterWordIantheColumn,"|", "|");
            }
            System.out.format("|");
            for (int j = 0; j < table[i].length; j++) {
                System.out.format(" ");
                System.out.format(table[i][j]);
                for (int k = 0; k < maxCharacterWordIantheColumn[j] - 1 - table[i][j].length(); k++) {
                    System.out.format(" ");
                }
                System.out.format("|");
            }
            System.out.format("\n");
        }
        nonWordLine(maxCharacterWordIantheColumn,"\\", "/");
    }

    public static String getInput(String label) {
        System.out.println(label);
        String userInput="";
        userInput=scanner.nextLine();
        return userInput;
    }

    public static String[] getInputs(String[] labels) {
        String[] userInputs = new String[labels.length];
        int index = 0;
        String userInput = "";
        for (String label: labels) {
            System.out.println(label+":");
            userInput=scanner.nextLine();
            userInputs[index]=userInput;
            index++;
        }
        return userInputs;
    }

    public static void printErrorMessage(String message) {

        System.out.println(message);
    }
    private static int[] getMaxCharacterWordIantheColumn(String[][] table) {
        int[] maxCharacterWordIantheColumn = new int[table[0].length];
        int maxCharacterWord = 0;
        for (int i = 0; i < table[0].length; i++) {
            for (String[] strings : table) {
                if (strings[i].length() > maxCharacterWord) {
                    maxCharacterWord = strings[i].length();
                }
            }
            maxCharacterWordIantheColumn[i] = maxCharacterWord + 2;
            maxCharacterWord = 0;
        }
        return maxCharacterWordIantheColumn;
    }

    private static void nonWordLine(int[] maxCharacterWordIantheColumn, String start, String end) {
        System.out.format(start);
        for (int i = 0; i < maxCharacterWordIantheColumn.length; i++) {
            for (int j = 0; j < maxCharacterWordIantheColumn[i]; j++) {
                System.out.format("-");
            }
            if (i == maxCharacterWordIantheColumn.length - 1) {
                System.out.format(end);
            } else {
                System.out.format("|");
            }
        }
        System.out.format("\n");
    }
}
