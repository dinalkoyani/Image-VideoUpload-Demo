package com.example.firebaseauthwithuploadimagesdemo;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DocumentClass implements Serializable {
    public String docUrl;
    public Bitmap bitmap;
    public DocumentClass(){

    }
    public DocumentClass(String imagUrl){
        this.docUrl = imagUrl;
    };

    @Exclude
    public Map toMap() {
        HashMap result = new HashMap<>();
        result.put("imageUrl", docUrl);
        return  result;
    }
}
