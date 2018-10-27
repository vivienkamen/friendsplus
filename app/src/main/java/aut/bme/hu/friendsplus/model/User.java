package aut.bme.hu.friendsplus.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public String email;
    public String uid;
    public String imageUri;

    public User() {}

    public User(String username, String email, String uid) {
        this.username = username;
        this.email = email;
        this.uid = uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("username", username);
        result.put("email", email);
        result.put("uid", uid);
        result.put("imageUri", imageUri);

        return result;
    }
}
