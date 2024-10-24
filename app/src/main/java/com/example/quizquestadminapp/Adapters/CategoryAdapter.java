package com.example.quizquestadminapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizquestadminapp.Models.CategoryModel;
import com.example.quizquestadminapp.R;
import com.example.quizquestadminapp.SubCategoryActivity;
import com.example.quizquestadminapp.databinding.RvCategoryDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder>{

    Context context;
    ArrayList<CategoryModel>list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_category_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        CategoryModel categoryModel = list.get(position);

        holder.binding.categoryName.setText(categoryModel.getCategoryName());

        Picasso.get()
                .load(categoryModel.getCategoryImage())
                .placeholder(R.drawable.admin2)
                .into(holder.binding.categoryImages);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("catId",categoryModel.getKey());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you Sure, you want to delete this category?");
                builder.setPositiveButton("Yes",(dialogInterface, i) -> {

                    FirebaseDatabase.getInstance().getReference().child("categories").child(categoryModel.getKey())
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                                    
                                }
                            });

                });
                builder.setNegativeButton("No",(dialogInterface, i) -> {

                    dialogInterface.cancel();

                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RvCategoryDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvCategoryDesignBinding.bind(itemView);
        }
    }
}
