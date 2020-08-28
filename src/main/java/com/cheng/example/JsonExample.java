package com.cheng.example;

import com.cheng.entity.Content;
import com.google.gson.Gson;

public class JsonExample {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Content content = gson.fromJson(JsonData.json, Content.class);
        System.out.println(gson.toJson(content));
    }
}
