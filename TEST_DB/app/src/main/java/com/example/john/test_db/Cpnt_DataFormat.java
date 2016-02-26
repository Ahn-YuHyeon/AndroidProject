package com.example.john.test_db;

public class Cpnt_DataFormat {
    public int _id;
    public String name;
    public String contact;
    public String email;

    public Cpnt_DataFormat( ){}

    public Cpnt_DataFormat(int _id, String name, String contact, String email){
        this._id = _id;
        this.name = name;
        this.contact = contact;
        this.email = email;
    }
}


