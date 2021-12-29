package com.example.madcampweek1.ui.contact;

import java.util.ArrayList;

public class ContactItem {
    String name;
    String number;
//    int resourceId;

    public ContactItem(String name, String number) {
        this.name = name;
        this.number = number;
    }
//
//    public int getResourceId() {
//        return resourceId;
//    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

//    public void setResourceId(int resourceId) {
//        this.resourceId = resourceId;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }

    public static ArrayList<ContactItem> createContactList(int numContacts) {
        ArrayList<ContactItem> contacts = new ArrayList<ContactItem>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new ContactItem("문석훈", "010-3088-8447"));
        }

        return contacts;
    }
}
