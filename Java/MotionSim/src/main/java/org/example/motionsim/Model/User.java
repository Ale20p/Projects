package org.example.motionsim.Model;

public class User {
    private String email;
    private String password;
    private UserSettings settings = new UserSettings();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserSettings getSettings() {
        return settings;
    }
}
