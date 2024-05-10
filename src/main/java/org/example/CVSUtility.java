package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CVSUtility {
    public static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        Path pathToFile = Paths.get(filePath);

        try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
            String line = br.readLine(); // Read the header and ignore

            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                data.add(attributes);
            }
        }
        return data;
    }

    public static void writeCSV(String filePath, List<String[]> dataLines) throws IOException {
        Path pathToFile = Paths.get(filePath);
        try (BufferedWriter bw = Files.newBufferedWriter(pathToFile)) {
            for (String[] data : dataLines) {
                bw.write(String.join(",", data));
                bw.newLine();
            }
        }
    }
}
