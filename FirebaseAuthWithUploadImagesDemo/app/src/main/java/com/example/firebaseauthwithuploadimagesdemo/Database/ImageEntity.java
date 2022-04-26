package com.example.firebaseauthwithuploadimagesdemo.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Image")
public class ImageEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    private int id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @ColumnInfo(name = "imageUrl")
    public String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
