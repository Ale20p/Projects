package org.example.motionsim.Model;

public final class Session {
    private static User current;

    public static void set(User user) {
        current = user;
    }

    public static User get() {
        return current;
    }

    private Session() {}
}
