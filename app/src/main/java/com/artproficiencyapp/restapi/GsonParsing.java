package com.artproficiencyapp.restapi;

import com.google.gson.Gson;

public class GsonParsing<T> {

    public Object parseJsonObject(String output, T objectType) {
        Gson gson = new Gson();
        return gson.fromJson(output, objectType.getClass());
    }
}