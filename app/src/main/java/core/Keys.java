package core;

/**
 * Created by lucas on 15/12/2017.
 */

public enum Keys {
    EMAIL("email"),
    WISHS("wishs"),
    USERS("users");

    private String key;

    Keys (String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
