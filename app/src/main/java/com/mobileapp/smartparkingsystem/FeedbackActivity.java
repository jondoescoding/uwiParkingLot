package com.mobileapp.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobileapp.smartparkingsystem.models.StringHolder;

public class FeedbackActivity extends AppCompatActivity {

    EditText nameEt, messageEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        nameEt = findViewById(R.id.feedback_et_name);
        messageEt = findViewById(R.id.feedback_et_message);

        findViewById(R.id.feedback_btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(nameEt) || isEmpty(messageEt)){
                    Toast.makeText(FeedbackActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                }else{
                    StringHolder stringHolder = new StringHolder(nameEt.getText().toString()+"#"+messageEt.getText().toString());
                    FirebaseFirestore.getInstance().collection("feedbacks").add(stringHolder)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                                    nameEt.setText("");
                                    messageEt.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FeedbackActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}