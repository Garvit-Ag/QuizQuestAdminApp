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

import com.example.quizquestadminapp.Models.QuestionsModel;
import com.example.quizquestadminapp.Models.SubCategoryModel;
import com.example.quizquestadminapp.QuestionsActivity;
import com.example.quizquestadminapp.R;
import com.example.quizquestadminapp.databinding.RvSubcategoryDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewHolder>{

    Context context;
    ArrayList<QuestionsModel>list;
    private String catId;
    private String subCatId;

    public QuestionAdapter(Context context, ArrayList<QuestionsModel> list, String catId, String subCatId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
        this.subCatId = subCatId;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_subcategory_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        QuestionsModel categoryModel = list.get(position);

        holder.binding.subCategoryName.setText(categoryModel.getQuestion());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you Sure, you want to delete this category?");
                builder.setPositiveButton("Yes",(dialogInterface, i) -> {

                    FirebaseDatabase.getInstance().getReference().child("categories").child(catId).child("subCategories").child(subCatId)
                            .child("questions").child(categoryModel.getKey())
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

        RvSubcategoryDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvSubcategoryDesignBinding.bind(itemView);
        }
    }
}
