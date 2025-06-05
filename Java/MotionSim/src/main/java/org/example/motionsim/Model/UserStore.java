package org.example.motionsim.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserStore {
    private static final File file = new File("Data/users.json");
    private static final Type list_type = new TypeToken<List<User>>(){}.getType();
    private static final Gson gson = new Gson();

    public static List<User> load() {
        if (!file.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(file)) {
            List<User> list = gson.fromJson(r, list_type);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void save(List<User> users) {
        file.getParentFile().mkdirs();
        try (Writer w = new FileWriter(file)) {
            gson.toJson(users, list_type, w);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
