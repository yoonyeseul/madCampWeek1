package com.example.madcampweek1.ui.contact;

import static java.security.AccessController.getContext;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.io.*;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactItem {
    String name;
    String number;
    String email;
    String web;
    String job;
    String sns;
    String address;
    int id;
//    int resourceId;

    public ContactItem(String name, String number, String email, String web, String job,String sns, String address, int id) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.web = web;
        this.job = job;
        this.sns = sns;
        this.address = address;

        this.id = id;
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
    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

//    public static ArrayList<ContactItem> getContactList() {
//
//        String json = getJsonString(this.getApplicationContext());
//        return jsonParsing(json);
//
//    }

//    public static ArrayList<ContactItem> createContactList(int numContacts) {
//        ArrayList<ContactItem> contacts = new ArrayList<ContactItem>();
//
//        for (int i = 1; i <= numContacts; i++) {
//            contacts.add(new ContactItem("문석훈", "010-3088-8447"));
//        }
//
//        return contacts;
//
//    }
    public static ArrayList<ContactItem> createContactList(String json) {
        ArrayList<ContactItem> contacts = new ArrayList<ContactItem>();

        contacts = jsonParsing(json);

        return contacts;

    }


//    private static String getJsonString(AccessControlContext context) {
//        String json = null;
//        try {
//            AssetManager am = context.getAssets();
//            InputStream is = am.open("jsons/contact.json");
//            int fileSize = is.available();
//
//            byte[] buffer = new byte[fileSize];
//            is.read(buffer);
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//            return null;
//        }
//
//        return json;
//    }
    private static ArrayList<ContactItem> jsonParsing(String json)
    {
        ArrayList<ContactItem> contactList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray contactArray = jsonObject.getJSONArray("contact");

            for(int i=0; i<contactArray.length(); i++)
            {
                JSONObject contactObject = contactArray.getJSONObject(i);

                ContactItem contact = new ContactItem(contactObject.getString("name"), contactObject.getString("number"),
                        contactObject.getString("email"), contactObject.getString("web"), contactObject.getString("job"),
                        contactObject.getString("sns"), contactObject.getString("address"), contactObject.getInt("id"));
                System.out.println("지금부터 연락처 정보 나올거임" + i);
                System.out.println(contact);
                System.out.println("지금부터 연락처 정보 나올거임" + i);
                contactList.add(contact);
            }
            return contactList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return contactList;
    }
}


