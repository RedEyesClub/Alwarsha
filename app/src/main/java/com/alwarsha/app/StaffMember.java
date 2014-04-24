package com.alwarsha.app;

/**
 * Created by Farid on 4/18/14.
 */
public class StaffMember {

    private String name;
    private String id;
    private String status;
    private String password;

    public StaffMember(String status, String name, String password) {
        this.status = status;
        this.password = password;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
