package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * The CSVUtility class provides utility methods for reading and writing CSV files.
 *
 * @author Alessandro Pomponi
 */
class CSVUtility {

    /**
     * Returns the base path for the CSV files.
     *
     * @return the base path for the CSV files
     */
    private static String getBasePath() {
        return System.getProperty("user.home") + File.separator + "MyAppData" + File.separator;
    }

    /**
     * Reads data from a CSV file and returns it as a list of string arrays.
     *
     * @param fileName the name of the CSV file
     * @return a list of string arrays representing the data in the CSV file
     */
    public static List<String[]> readCSV(String fileName) {
        List<String[]> data = new ArrayList<>();
        Path filePath = Paths.get(getBasePath() + fileName);
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                return new ArrayList<>();
            }
            try (BufferedReader br = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] attributes = line.split(",");
                    data.add(attributes);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error reading CSV file: " + e.getMessage());
        }
        return data;
    }

    /**
     * Writes data to a CSV file.
     *
     * @param fileName the name of the CSV file
     * @param dataLines a list of string arrays representing the data to be written to the CSV file
     * @param append true to append to the file, false to overwrite the file
     */
    public static void writeCSV(String fileName, List<String[]> dataLines, boolean append) {
        Path filePath = Paths.get(getBasePath() + fileName);
        try {
            Files.createDirectories(filePath.getParent());
            OpenOption[] options = append
                    ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND}
                    : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};
            try (BufferedWriter bw = Files.newBufferedWriter(filePath, options)) {
                for (String[] data : dataLines) {
                    bw.write(String.join(",", data));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error writing to CSV file: " + e.getMessage());
        }
    }
}
