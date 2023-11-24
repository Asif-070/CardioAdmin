package com.example.cardioadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class patientadapter extends RecyclerView.Adapter<patientadapter.ViewHolder> {

    private Context context;
    private List<patientlist> dataList;

    public void setSearchList(List<patientlist> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public patientadapter(Context context, List<patientlist> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = dataList.get(position).getImgurl();
        holder.recName.setText(dataList.get(position).getName());
        holder.recType.setText(dataList.get(position).getType());
        holder.recBlood.setText("Blood: " + dataList.get(position).getBt());
        holder.recId.setText(dataList.get(position).getId());
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.editprofile) // Placeholder image while loading (if needed)
                .error(R.drawable.editprofile) // Error image if Glide fails to load (if needed)
                .into(holder.recImage);

        String clickedItemId = dataList.get(position).getId();

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PatientViewActivity.class);
                intent.putExtra("PatientID", clickedItemId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView recImage;
        TextView recName, recType, recBlood, recId;
        CardView recCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.imgdoc);
            recName = itemView.findViewById(R.id.name);
            recType = itemView.findViewById(R.id.type);
            recBlood = itemView.findViewById(R.id.blood);
            recId = itemView.findViewById(R.id.recid);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}
