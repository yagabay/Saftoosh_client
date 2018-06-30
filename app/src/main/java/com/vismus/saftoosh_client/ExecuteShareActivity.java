package com.vismus.saftoosh_client;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ExecuteShareActivity extends AppCompatActivity {

    static final String USERNAME = "yagabay@gmail.com";
    static final String PASSWORD = "1qaz@WSX";

    static FirebaseAuth _firebaseAuth;
    static StorageReference _storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _firebaseAuth = FirebaseAuth.getInstance();
        _storageRef = FirebaseStorage.getInstance().getReference();
        authAndUploadFile((Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM));
    }

    void authAndUploadFile(final Uri fileUri){
        _firebaseAuth.signInWithEmailAndPassword(USERNAME, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            uploadFile(fileUri);
                        }
                        else {
                            Toast.makeText(ExecuteShareActivity.this, "Share failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void uploadFile(Uri fileUri){
        String filePath = Utils.getPathFromUri(this, fileUri);
        String targetFileName = Utils.generateRandomIdentifier() + "." + Utils.getFileExtension(filePath);
        StorageReference targetFileRef = _storageRef.child(targetFileName);
        targetFileRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ExecuteShareActivity.this, "Shared successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ExecuteShareActivity.this, "Share failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

}