package com.example.firebaseauthwithuploadimagesdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {
    ArrayList<DocumentClass> documentClassArrayList;
    Context context;
    private OnDownloadItemClickListener listener;
    private OnShareItemClickListener shareListener;
    private OnViewItemClickListener ViewListener;

    public GridViewAdapter(Context context,ArrayList<DocumentClass> documentClassArrayList){
        this.context = context;
        this.documentClassArrayList = documentClassArrayList;
    }
    public void setDownloadClick(OnDownloadItemClickListener listener){
        this.listener = listener;
    }
    public interface OnDownloadItemClickListener{
        void downloadClick(int position);
    }
    public void setShareClick(OnShareItemClickListener shareListener){
        this.shareListener = shareListener;
    }

    public interface OnShareItemClickListener{
        void shareClick(int position);
    }

    public void setViewClick(OnViewItemClickListener viewListener){
        this.ViewListener = viewListener;
    }

    public interface OnViewItemClickListener{
        void viewClick(int position);
    }
    @NonNull
    @Override
    public GridViewAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_layout, parent, false);

        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewAdapter.GridViewHolder holder, int position) {
        holder.imgGrid.setImageBitmap(setImageFromFirebase(documentClassArrayList.get(position).docUrl.replace("imageUrl=","")));
    }

    @Override
    public int getItemCount() {
        if(documentClassArrayList != null){
            return documentClassArrayList.size();
        }
        return 0;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGrid,imgDownLoad,imgView,imgShare;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGrid = itemView.findViewById(R.id.imgGrid);
            imgDownLoad =  itemView.findViewById(R.id.imgDownLoad);
            imgView = itemView.findViewById(R.id.imgView);
            imgShare = itemView.findViewById(R.id.imgShare);

            imgDownLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("DOWNLOAD CLICK" ,"CALL");
                    if(listener != null){

                        listener.downloadClick(getAdapterPosition());
                    }
                }
            });

            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Share CLICK" ,"CALL");
                    if(shareListener != null){
                        shareListener.shareClick(getAdapterPosition());
                    }
                }
            });

            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Share CLICK" ,"CALL");
                    if(ViewListener != null){
                        ViewListener.viewClick(getAdapterPosition());
                    }
                }
            });
        }

    }
    private Bitmap setImageFromFirebase(String encodeImage){
        byte[] decodedString = Base64.decode(encodeImage, Base64.NO_WRAP);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}


