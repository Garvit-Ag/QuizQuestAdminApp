package com.example.quizquestadminapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizquestadminapp.Models.SubCategoryModel;
import com.example.quizquestadminapp.databinding.ActivityUploadSubCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class UploadSubCategoryActivity extends AppCompatActivity {

    ActivityUploadSubCategoryBinding binding;
    FirebaseDatabase database;
    Dialog loadingDialog;
    private String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); //it is not there
        binding = ActivityUploadSubCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        categoryId = getIntent().getStringExtra("catId");

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(true);

        binding.btnUploadCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subCatName = binding.edtSubCategory.getText().toString();

                if(subCatName.isEmpty()){
                    binding.edtSubCategory.setError("Enter Sub-Category name");
                }
                else{

                    storeData(subCatName);
                }
            }
        });

    }

    private void storeData(String subCatName) {

        loadingDialog.show();
        SubCategoryModel model = new SubCategoryModel(subCatName);
        database.getReference().child("categories").child(categoryId).child("subCategories")
                .push()
                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        loadingDialog.dismiss();
                        Toast.makeText(UploadSubCategoryActivity.this, "data uploaded", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(UploadSubCategoryActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}