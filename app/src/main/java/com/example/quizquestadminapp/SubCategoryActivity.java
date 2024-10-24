package com.example.quizquestadminapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizquestadminapp.Adapters.CategoryAdapter;
import com.example.quizquestadminapp.Adapters.SubCategoryAdapter;
import com.example.quizquestadminapp.Models.CategoryModel;
import com.example.quizquestadminapp.Models.SubCategoryModel;
import com.example.quizquestadminapp.databinding.ActivityMainBinding;
import com.example.quizquestadminapp.databinding.ActivitySubCategoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {

    ActivitySubCategoryBinding binding;
    FirebaseDatabase database;
    SubCategoryAdapter adapter;
    ArrayList<SubCategoryModel> list;
    Dialog loadingDialog;

    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubCategoryBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        categoryId = getIntent().getStringExtra("catId");

        list = new ArrayList<>();

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvCategory.setLayoutManager(layoutManager);
        adapter= new SubCategoryAdapter(this,list,categoryId);
        binding.rvCategory.setAdapter(adapter);

        database.getReference().child("categories").child(categoryId).child("subCategories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                        SubCategoryModel model = dataSnapshot.getValue(SubCategoryModel.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);
                    }

                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
                }
                else{

                    loadingDialog.dismiss();
                    Toast.makeText(SubCategoryActivity.this, "Category not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.dismiss();
                Toast.makeText(SubCategoryActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this,UploadSubCategoryActivity.class);
                intent.putExtra("catId",categoryId);
                startActivity(intent);
            }
        });
    }
}