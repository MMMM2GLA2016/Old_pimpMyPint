package gla.istic.pimpmypint.firebase.model;

import java.util.List;

/**
 * Created by Antoine Brezillon on 21/03/16.
 */
public class user {

    private String uid;
    private String name;
    private List<user> contacts;

    public user() {
    }

    public user(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public List<user> getContacts() {
        return contacts;
    }
}
