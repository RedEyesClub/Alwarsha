package com.alwarsha.app;

import org.simpleframework.xml.Element;

/**
 * Created by Farid on 4/18/14.
 */
@Element (name="StaffMember")
public class StaffMember {
    @Element (name="name")
    private String name;
    @Element (name="id")
    private int id;
    @Element (name="status")
    private String status;
    @Element (name="password")
    private String password;

    public StaffMember()
    {
        this.status = "";
        this.password = "";
        this.id = 0;
        this.name = "";
    }

    public StaffMember(String status, String name, String password) {
        this.status = status;
        this.password = password;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
