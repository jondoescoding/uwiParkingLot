package com.mobileapp.smartparkingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobileapp.smartparkingsystem.models.UserProfileInfo;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserdataActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText userNameEdit;
    private EditText emailEdit;
    private EditText phoneNoEdit;
    private Button imgUploadBtn, doneBtn;
    private CircleImageView profileImg;
    private boolean isImageChange = false;

    private final int GALLERY_REQUEST = 12;
    private Uri selectedImage = null;
    private UserProfileInfo userProfileInfo;
    private boolean isNewUser = false;


    private Spinner occupationSpinner;


    private static ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;

    private String currentProfileImageUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        init();
        setProgressBar();
        userProfileInfo = UserProfileInfo.getInstance();



        isNewUser = getNewUserStatus();
        if (isNewUser)
            updateFiresbaseToUserprofile();
        else {
            currentProfileImageUrl = userProfileInfo.getPhotoUrl();
        }


        updateTextviews();

    }

    public void setProgressBar() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Updating User Information");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }





    public boolean getNewUserStatus() {
        if (getIntent().hasExtra("isNewUser"))
            return getIntent().getExtras().getBoolean("isNewUser");
        return false;
    }

    public void init() {


        userNameEdit = findViewById(R.id.userdata_edit_user);
        emailEdit = findViewById(R.id.userdata_edit_email);
        phoneNoEdit = findViewById(R.id.userdata_edit_phoneno);
        imgUploadBtn = findViewById(R.id.userdata_btn_uploadpic);
        imgUploadBtn.setOnClickListener(this);
        doneBtn = findViewById(R.id.userdata_btn_done);
        doneBtn.setOnClickListener(this);
        profileImg = findViewById(R.id.userdata_img_profileimg);


    }

    public void updateFiresbaseToUserprofile() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userProfileInfo.setId(firebaseUser.getUid());
        if (firebaseUser.getEmail() != null)
            userProfileInfo.setEmail(firebaseUser.getEmail());
        else
            userProfileInfo.setEmail("");
        if (firebaseUser.getPhoneNumber() != null)
            userProfileInfo.setPhoneNo(firebaseUser.getPhoneNumber());
        else
            userProfileInfo.setPhoneNo("");
        if (firebaseUser.getPhotoUrl() != null) {
            userProfileInfo.setPhotoUrl(firebaseUser.getPhotoUrl().toString());

            currentProfileImageUrl = firebaseUser.getPhotoUrl().toString();
        } else
            userProfileInfo.setPhotoUrl("");
        if (firebaseUser.getDisplayName() != null)
            userProfileInfo.setUserName(firebaseUser.getDisplayName());
        else
            userProfileInfo.setUserName("");
    }

    public void updateTextviews() {

        userNameEdit.setText(userProfileInfo.getUserName());

        emailEdit.setText(userProfileInfo.getEmail());
//        if(!userProfileInfo.getEmail().equals("")) {
//            emailEdit.setFocusable(false);
//            emailEdit.setFocusableInTouchMode(false);
//            emailEdit.setClickable(false);
//        }
        phoneNoEdit.setText(userProfileInfo.getPhoneNo());



        Glide.with(UserdataActivity.this).load(userProfileInfo.getPhotoUrl()).placeholder(R.drawable.profileplaceholder).into(profileImg);


    }


    public void updateEditTextsToUserProfile() {
        UserProfileInfo userProfileInfo = UserProfileInfo.getInstance();
        userProfileInfo.setUserName(userNameEdit.getText().toString());
        userProfileInfo.setEmail(emailEdit.getText().toString());
        userProfileInfo.setPhoneNo(phoneNoEdit.getText().toString());
        userProfileInfo.setPhotoUrl(currentProfileImageUrl);

    }



    public void showToast(String message) {
        Toast.makeText(UserdataActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public boolean isDataChangedByUser() {
        UserProfileInfo userProfileInfo = UserProfileInfo.getInstance();

        //showToast("IsChanged: " + isChanged);
        boolean result = !userProfileInfo.getUserName().equals(userNameEdit.getText().toString())
                || !userProfileInfo.getEmail().equals(emailEdit.getText().toString())
                || !userProfileInfo.getPhoneNo().equals(phoneNoEdit.getText().toString());

        return result;
    }

    public boolean isProfilePhotoChangedByUser() {
        return isImageChange;
    }

    public void updateUserProfileToFirestore() {
        UserProfileInfo userProfileInfo = UserProfileInfo.getInstance();
        FirebaseFirestore.getInstance().collection("users").document(userProfileInfo.getId()).set(userProfileInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (isNewUser) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(UserdataActivity.this, AuthenticationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    goBackIfInfoUpdate();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast(e.getLocalizedMessage());
            }
        });

    }

    public void goBackIfInfoUpdate() {
        Intent intent = new Intent(UserdataActivity.this, AuthenticationActivity.class);
//        intent.putExtra("fromUserInfo", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void updateUserProfilePhotoToStorage() {
        progressDialog.show();
        if (selectedImage != null) {

            UserProfileInfo userProfileInfo = UserProfileInfo.getInstance();
            final StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://responsiveclassregistration.appspot.com").child("users/profile_pictures/" + userProfileInfo.getId() + "." + GetFileExtension(selectedImage));

            storageReference.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    currentProfileImageUrl = uri.toString();
                                    isImageChange = false;
                                    //showToast("Image updated Successfully");

                                    updateEditTextsToUserProfile();
                                    updateUserProfileToFirestore();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                                }
                            });

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            showToast(exception.getLocalizedMessage());
                        }
                    });
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    selectedImage = data.getData();
                    Glide.with(UserdataActivity.this).load(selectedImage).centerCrop().into(profileImg);
                    isImageChange = true;
                    break;
            }
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userdata_btn_uploadpic:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                break;
            case R.id.userdata_btn_done:
                    if (isProfilePhotoChangedByUser()) {
                        //showToast("Profile Pic Updating");
                        updateUserProfilePhotoToStorage();
                    } else if (isDataChangedByUser()) {
                        //showToast("Data is updating");
                        updateEditTextsToUserProfile();
                        updateUserProfileToFirestore();
//                    if(isValidEmail(emailEdit.getText().toString()) && isValidPhoneNo(phoneNoEdit.getText().toString())){
//                        updateEditTextsToUserProfile();
//                        updateUserProfileToFirestore();
//                    }else
//                        showWrongInputToast();
                    } else {
                        onBackPressed();
                    }
                break;
            default:

        }
    }

}