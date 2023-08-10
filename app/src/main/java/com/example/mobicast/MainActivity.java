package com.example.mobicast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button goLiveBtn;
    TextInputEditText liveIdInput,nameInput;

    String liveID,name,userID;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("name_pref",MODE_PRIVATE);

        goLiveBtn = findViewById(R.id.go_live_btn);
        liveIdInput = findViewById(R.id.live_id_input);
        nameInput = findViewById(R.id.name_input);

        nameInput.setText(sharedPreferences.getString("name",""));


        liveIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                liveID = liveIdInput.getText().toString();
                if(liveID.length()==0){
                    goLiveBtn.setText("Start New Live");
                }else{
                    goLiveBtn.setText("Join a live");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goLiveBtn.setOnClickListener((v)->{
            name = nameInput.getText().toString();

            if (name.isEmpty()){
                nameInput.setError("Name is Required");
                nameInput.requestFocus();
                return;
            }

            liveID = liveIdInput.getText().toString();

            if(liveID.length()>0 && liveID.length()!=5){
                liveIdInput.setError("Invalid LIVE ID");
                liveIdInput.requestFocus();
                return;
            }
            startMeeting();


        });

    }

    void startMeeting(){
        Log.i("LOG","Start meeting");
        sharedPreferences.edit().putString("name",name).apply();

        boolean isHost = true;
        if(liveID.length()==5)
            isHost = false;
        else
            liveID = generateLiveID();

        userID = UUID.randomUUID().toString();

        Intent intent = new Intent(MainActivity.this,LiveActivity.class);
        intent.putExtra("user_id",userID);
        intent.putExtra("name",name);
        intent.putExtra("live_id",liveID);
        intent.putExtra("host",isHost);
        startActivity(intent);


    }

    String generateLiveID(){
        StringBuilder id = new StringBuilder();
        while (id.length()!=5){
            int random = new Random().nextInt(10);
            id.append(random);
        }
        return id.toString();
    }
}