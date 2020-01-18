package com.fitscorp.apps.indika.schoolbus.firebase.model;

public class Bus {

    public String id;
    public String status;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Bus() {
    }

    public Bus(String id, String status) {
        this.id = id;
        this.status = status;
    }

}
