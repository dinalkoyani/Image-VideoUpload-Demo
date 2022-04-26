package com.example.firebaseauthwithuploadimagesdemo.Database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert
    void insert(ImageEntity... images);

    @Query("SELECT * FROM ImageEntity")
    List<ImageEntity> getAllImage();

    @Query("SELECT * FROM ImageEntity where image_id = :imageId")
    List<ImageEntity> getImageByImageId(int imageId);
}
