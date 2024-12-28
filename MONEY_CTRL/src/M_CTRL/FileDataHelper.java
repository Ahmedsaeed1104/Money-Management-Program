package M_CTRL;

import java.io.*;
import java.util.StringJoiner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileDataHelper {

    private static final String FILE_PATH = "spending_data.txt";

    // Save data to file
    public void saveData(String totalMoney, String exceptions, String personal, double currentMoney, boolean[] daySelections) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(totalMoney);
            writer.newLine();
            writer.write(exceptions);
            writer.newLine();
            writer.write(personal);
            writer.newLine();
            writer.write(String.valueOf(currentMoney));
            writer.newLine();
            writer.write(convertDaySelectionsToString(daySelections));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load data from file
    public String[] loadData() {
        String[] data = new String[5];
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            data[0] = reader.readLine(); // Total money
            data[1] = reader.readLine(); // Exceptions
            data[2] = reader.readLine(); // Personal
            data[3] = reader.readLine(); // Current money
            data[4] = reader.readLine(); // Day selections
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Convert boolean array to a comma-separated string
    public String convertDaySelectionsToString(boolean[] selections) {
        StringBuilder sb = new StringBuilder();
        for (boolean selected : selections) {
            sb.append(selected ? "1" : "0").append(",");
        }
        return sb.toString();
    }

    // Convert a comma-separated string to a boolean array
    public boolean[] convertStringToDaySelections(String data) {
        boolean[] selections = new boolean[30];
        String[] values = data.split(",");
        for (int i = 0; i < values.length; i++) {
            selections[i] = values[i].equals("1");
        }
        return selections;
    }
}
