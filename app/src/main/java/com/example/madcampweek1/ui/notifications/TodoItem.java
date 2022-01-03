package com.example.madcampweek1.ui.notifications;

import com.example.madcampweek1.ui.contact.ContactItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TodoItem {
    Boolean check;
    String content;

//    int resourceId;

    public TodoItem(Boolean check, String content) {
        this.check = check;
        this.content = content;
    }
//
//    public int getResourceId() {
//        return resourceId;
//    }

    public Boolean getCheck() {
        return check;
    }

    public String getContent() {
        return content;
    }

    //    public void setResourceId(int resourceId) {
//        this.resourceId = resourceId;
//    }
//
    public void setCheck(Boolean check) {
        this.check = check;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public static ArrayList<TodoItem> createTodoList(String json, String date) {
        ArrayList<TodoItem> todos = new ArrayList<TodoItem>();
        todos = jsonParsing(json, date);
        return todos;

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
    private static ArrayList<TodoItem> jsonParsing(String json, String date)
    {
        ArrayList<TodoItem> todoList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray todoArray = jsonObject.getJSONArray(date);
            System.out.println(date);

            for(int i=0; i<todoArray.length(); i++)
            {
                JSONObject todoObject = todoArray.getJSONObject(i);

                TodoItem todo = new TodoItem(todoObject.getBoolean("check"), todoObject.getString("content"));

                todoList.add(todo);
            }
            return todoList;
        }catch (JSONException e) {
//            e.printStackTrace();
            System.out.println(json);
        }
        return todoList;
    }
}
