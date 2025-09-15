package com.example.cardioadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class amb_adapter extends RecyclerView.Adapter<amb_view_holder>{
    Context context;
    List<amb_items> items;

    public void setSearchList(List<amb_items> dataSearchList){
        this.items = dataSearchList;
        notifyDataSetChanged();
    }
    public amb_adapter(Context context, List<amb_items> items) {
        this.context = context;
        this.items = items;
    }

    interface OnDeleteItemClickListener {
        void onDeleteItemClick(int position);
    }

    private OnDeleteItemClickListener deleteItemClickListener;

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        this.deleteItemClickListener = listener;
    }

    @NonNull
    @Override
    public amb_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new amb_view_holder(LayoutInflater.from(context).inflate(R.layout.item_ambulance,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull amb_view_holder holder, int position) {
        holder.Name.setText(items.get(position).getName());
        holder.Phone.setText(items.get(position).getPhone());
        int p = position;
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteItemClickListener != null) {
                    deleteItemClickListener.onDeleteItemClick(p);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
