package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

class CSVUtility {
    private static String getBasePath() {
        return System.getProperty("user.home") + File.separator + "MyAppData" + File.separator;
    }

    public static List<String[]> readCSV(String fileName) throws IOException {
        Path filePath = Paths.get(getBasePath() + fileName);
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
            return new ArrayList<>();
        }
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                data.add(attributes);
            }
        }
        return data;
    }

    public static void writeCSV(String fileName, List<String[]> dataLines, boolean append) throws IOException {
        Path filePath = Paths.get(getBasePath() + fileName);
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
    }
}
