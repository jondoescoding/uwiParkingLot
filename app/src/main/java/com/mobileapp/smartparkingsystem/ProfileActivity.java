package com.mobileapp.smartparkingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobileapp.smartparkingsystem.models.UserProfileInfo;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {



    private CircleImageView profileImg;
    private TextView userNameText;
    private TextView emailText, phoneNoText;
    private ImageButton editBtn, logoutBtn;
    private UserProfileInfo userProfileInfo;



    private static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userProfileInfo = UserProfileInfo.getInstance();

        init();
        setProgressBar();
        updateTextViews();

    }


    public void setProgressBar(){

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }



    public void updateTextViews(){
        Glide.with(ProfileActivity.this).load(userProfileInfo.getPhotoUrl()).placeholder(R.drawable.profileplaceholder).into(profileImg);
        userNameText.setText(userProfileInfo.getUserName());

        emailText.setText(userProfileInfo.getEmail());
        phoneNoText.setText(userProfileInfo.getPhoneNo());



        if(emailText.getText().equals(""))
            emailText.setText("@email");
        if(phoneNoText.getText().equals(""))
            phoneNoText.setText("+-- --- -------");

    }


    private void init(){
        profileImg = findViewById(R.id.userdata_img_profileimg);
        userNameText = findViewById(R.id.profile_text_username);
        emailText = findViewById(R.id.profile_text_email);
        phoneNoText = findViewById(R.id.profile_text_phoneNo);




        editBtn = findViewById(R.id.profile_img_edit);
        editBtn.setOnClickListener(this);
        logoutBtn = findViewById(R.id.profile_img_signout);
        logoutBtn.setOnClickListener(this);

    }




    public void showToast(String message){
        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_img_signout:
                signout();
                break;

            case R.id.profile_img_edit:
                startEditUserdataActivity();
                break;

            default:

        }
    }

    public void startEditUserdataActivity(){
        Intent intent = new Intent(ProfileActivity.this, UserdataActivity.class);
        startActivity(intent);
    }

    public void signout(){
        progressDialog.show();
        AuthUI.getInstance()
                .signOut(ProfileActivity.this)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(ProfileActivity.this, AuthenticationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



}