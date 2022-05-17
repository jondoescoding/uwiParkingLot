package com.mobileapp.smartparkingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobileapp.smartparkingsystem.models.UserProfileInfo;

import java.util.Arrays;
import java.util.List;

public class AuthenticationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 321;
    BottomNavigationView bottomNavigationView;
    private UserProfileInfo userProfileInfo;


    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);


        FirebaseApp.initializeApp(this);
        userProfileInfo = UserProfileInfo.getInstance();

        // Create and launch sign-in intent
        if(FirebaseAuth.getInstance().getCurrentUser()==null ) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.Theme_SmartParkingSystem)
                            .setLogo(R.drawable.applogo)
                            .build(),
                    RC_SIGN_IN);
        }else if(FirebaseAuth.getInstance().getCurrentUser()!=null && FirebaseAuth.getInstance().getCurrentUser().getUid()!=null) {

            loadAllData();
            startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
        }

    }

    public void loadAllData(){
        updateFirestoreToUserprofile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            boolean isNewUser = response.isNewUser();
            //showToast("isNewUser MainActivity: " + isNewUser);
            if (resultCode == RESULT_OK) {
                if (isNewUser) {
                    Intent intent = new Intent(AuthenticationActivity.this, UserdataActivity.class);
                    intent.putExtra("isNewUser", true);
                    startActivity(intent);
                } else {
                    loadAllData();
                    startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
                }



            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Login failed with error code: " + response.getError().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateFirestoreToUserprofile() {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        userProfileInfo.setId(document.getString("id"));
                        userProfileInfo.setUserName(document.getString("userName"));
                        userProfileInfo.setEmail(document.getString("email"));
                        userProfileInfo.setPhotoUrl(document.getString("photoUrl"));
                        userProfileInfo.setPhoneNo(document.getString("phoneNo"));



                    } else {
                        showToast("No such user");
                    }

                } else {
                    showToast(task.getException().getLocalizedMessage());
                }
            }
        });
    }

    public void showToast(String message){
        Toast.makeText(AuthenticationActivity.this, message, Toast.LENGTH_LONG).show();
    }
}