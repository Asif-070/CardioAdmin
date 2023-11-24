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

public class doctoradapter extends RecyclerView.Adapter<doctoradapter.ViewHolder>{

    private Context context;
    private List<doctorlist> dataList;

    public void setSearchList(List<doctorlist> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public doctoradapter(Context context, List<doctorlist> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = dataList.get(position).getImgurl();
        holder.recName.setText(dataList.get(position).getName());
        holder.recSpc.setText(dataList.get(position).getSpc());
        holder.recExp.setText(dataList.get(position).getExp() + " Years");
        holder.recFee.setText(String.valueOf(dataList.get(position).getVisit()) + " TK");
        holder.recId.setText(dataList.get(position).getId());
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.doctor) // Placeholder image while loading (if needed)
                .error(R.drawable.doctor) // Error image if Glide fails to load (if needed)
                .into(holder.recImage);

        String clickedItemId = dataList.get(position).getId();

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorViewActivity.class);
                intent.putExtra("DoctorID", clickedItemId);
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
        TextView recName, recSpc, recExp, recFee, recId;
        CardView recCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.imgdoc);
            recName = itemView.findViewById(R.id.name);
            recSpc = itemView.findViewById(R.id.spc);
            recExp = itemView.findViewById(R.id.exp);
            recFee = itemView.findViewById(R.id.fee);
            recCard = itemView.findViewById(R.id.recCard);
            recId = itemView.findViewById(R.id.id);
        }
    }
}