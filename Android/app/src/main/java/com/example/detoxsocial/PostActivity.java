package com.example.detoxsocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.detoxsocial.Fragments.HomeFragment;
import com.example.detoxsocial.Fragments.SearchFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    String url="";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView close, imageToPost;
    TextView postText;
    EditText caption;

    RelativeLayout progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.post_close);
        imageToPost = findViewById(R.id.post_imageAdd);
        postText = findViewById(R.id.post_post_Text);
        caption = findViewById(R.id.post_caption);
        progressBar = findViewById(R.id.post_progress);

        storageReference = FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, DashboardActivity.class));
                finish();
            }
        });

        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if(imageUri !=null){
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+ getFileExtension(imageUri));

            uploadTask = reference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        url = downloadUri.toString();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = databaseReference.push().getKey();

                        HashMap<String, Object> hashmap = new HashMap<>();
                        hashmap.put("postid", postid);
                        hashmap.put("postimage", url);
                        hashmap.put("caption",caption.getText().toString());
                        hashmap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        databaseReference.child(postid).setValue(hashmap);

                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        startActivity(new Intent(PostActivity.this,DashboardActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(PostActivity.this,"Request failed!",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(PostActivity.this,"No image selected!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageToPost.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this,"Something went wrong!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, DashboardActivity.class));
            finish();
        }
    }
}