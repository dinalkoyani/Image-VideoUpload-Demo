package com.example.firebaseauthwithuploadimagesdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebaseauthwithuploadimagesdemo.Database.ImageDao;
import com.example.firebaseauthwithuploadimagesdemo.Database.ImageDatabase;
import com.example.firebaseauthwithuploadimagesdemo.Database.ImageEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity implements GridViewAdapter.OnDownloadItemClickListener,GridViewAdapter.OnShareItemClickListener,GridViewAdapter.OnViewItemClickListener {
    RecyclerView imgGrid;
    FirebaseDatabase database;
    GridViewAdapter gridViewAdapter;
    ArrayList<DocumentClass> documentClassArrayList= new ArrayList<>();
    ProgressBar progressBar;
    ImageDao imageDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        initViews();
        getSupportActionBar().setTitle("Images");
        getSupportActionBar().setHomeButtonEnabled(true);
        database = FirebaseDatabase.getInstance();
        getDocDataFromFirebase();
       /* if(gridViewAdapter != null) {
            Log.i(TAG, "onCreate: ");
            gridViewAdapter.setDownloadClick(this);
        }*/


    }

    private void initViews(){
        imgGrid = findViewById(R.id.imgGrid);
        progressBar = findViewById(R.id.progressBar);
    }


    private void getDocDataFromFirebase(){
        if (CheckInternetConnection.getInstance(this).isOnline()) {
            imgGrid.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference ref = database
                    .getReference("User")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("files");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        // setImageFromFirebase(ds.getValue().toString());
                        DocumentClass documentClass = new DocumentClass();
                        documentClass.docUrl = ds.getValue().toString();
                        documentClassArrayList.add(documentClass);

                    }
                    imgGrid.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    int numberOfColumns = 2;
                    GridLayoutManager layoutManager = new GridLayoutManager(ImageListActivity.this, numberOfColumns);
                    layoutManager.setReverseLayout(true);
                    imgGrid.setLayoutManager(layoutManager);
                    gridViewAdapter = new GridViewAdapter(ImageListActivity.this, documentClassArrayList);
                    gridViewAdapter.setDownloadClick(ImageListActivity.this);
                    gridViewAdapter.setShareClick(ImageListActivity.this);
                    gridViewAdapter.setViewClick(ImageListActivity.this);
                    imgGrid.setAdapter(gridViewAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(this,"No Internet!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void downloadClick(int position) {
        if(documentClassArrayList != null&&documentClassArrayList.size()>0) {
            imageDao = ImageDatabase.getInstance(this).imageDao();
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImage(documentClassArrayList.get(position).docUrl.replace("imageUrl=", ""));
            imageDao.insert(imageEntity);
            Toast.makeText(this, "Download image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap setImageFromFirebase(String encodeImage){
        byte[] decodedString = Base64.decode(encodeImage, Base64.NO_WRAP);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    @Override
    public void shareClick(int position) {
        if(documentClassArrayList != null&&documentClassArrayList.size()>0){
                Bitmap mBitmap = setImageFromFirebase(documentClassArrayList.get(position).docUrl.replace("imageUrl=",""));

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));
        }
    }

    @Override
    public void viewClick(int position) {
        openImageDialog(position);
    }

    private void openImageDialog(int position){
        final Dialog dialog = new Dialog(ImageListActivity.this);
        dialog.setContentView(R.layout.dialog_view_image);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
       // dialog.addContentView(new View(this), (new WindowManager.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT)));

        dialog.show();
        ImageView image = (ImageView) dialog.findViewById(R.id.viewImage);
        ImageView imageClose = (ImageView) dialog.findViewById(R.id.imgClose);


        if(documentClassArrayList != null && documentClassArrayList.size()>0){
            image.setImageBitmap(setImageFromFirebase(documentClassArrayList.get(position).docUrl.replace("imageUrl=","")));
        }

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

    }
//    ImageDao image_dao = ImageDatabase.getInstance(this).imageDao();
//    Image image = new Image(imageSources);
//    image_dao.insert(image);
//    finish();
}