package com.codecool.secureerp;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class Util {
    public final static Random random = new Random();
    public final static String lettersUppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static String lettersLowercase = "abcdefghijklmnopqrstuvwxyz";
    public final static String digits = "0123456789";
    private static final String COMMA_DELIMITER = ";";

    public static int getRandomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static String randomChars(String chars, int amount) {
        char[] randomChars = new char[amount];
        for (int i = 0; i < amount; i++) {
            int randomIndex = getRandomInt(0, chars.length());
            randomChars[i] = chars.charAt(randomIndex);
        }
        return String.valueOf(randomChars);
    }

    public static String shuffle(String original) {
        char[] chars = original.toCharArray();
        for (int i = 0; i < original.length() - 1; i++) {
            int j = getRandomInt(i, chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return String.valueOf(chars);
    }

    public static String generateId() {
        int smallLettersAmount = 4;
        int capitalLettersAmount = 2;
        int digitsAmount = 2;
        int specialCharsAmount = 2;
        String allowedSpecialChars = "_+-!";
        String pool = randomChars(lettersUppercase, capitalLettersAmount)
                + randomChars(lettersLowercase, smallLettersAmount)
                + randomChars(digits, digitsAmount) + randomChars(allowedSpecialChars, specialCharsAmount);
        return shuffle(pool);
    }

    public static String generateUniqueId(String[] ids) {
        while (true) {
            boolean foundIdentical = false;
            String newId = generateId();
            for (String id : ids) {
                if (newId.equals(id)) {
                    foundIdentical = true;
                    break;
                }
            }
            if (!foundIdentical)
                return newId;
        }
    }

    public static boolean tryParseInt(String userInput) {
        try {
            Integer.parseInt(userInput);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Path getPath(String fileName) {
        File file = new File(fileName);
        return Paths.get(file.getAbsolutePath());
    }

    public static List<List<String>> getAllDataFromFile(String filePath) throws FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }
        return records;
    }
    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public static String[][] getUserDataArray(List<List<String>> allDataFromFile, String[] header){
        String[][] allUserData = new String[allDataFromFile.size()+1][header.length];

        for (int i = 0; i < header.length; i++) {
            allUserData[0][i]=header[i];
        }

        for (int i=1; i<=allDataFromFile.size(); i++) {
            for (int j = 0; j < header.length; j++) {
                allUserData[i][j]=allDataFromFile.get(i-1).get(j);
            }
        }

        return allUserData;
    }

    public static  void writeAllDaraToFile(String line, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(line);
        writer.flush();
        writer.close();
    }

    public static boolean emailSpellChecker(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static boolean nameSpellChecker(String name) {
        String[] nameArray = name.split("\\s");
        System.out.println(Arrays.toString(nameArray));
        String regexPattern ="^[a-zA-Z\\s]+";
        for (String s : nameArray){
            if(!Pattern.compile(regexPattern).matcher(s).matches()){
                return false;
            }
        }
        return true;
    }


}
