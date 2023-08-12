package com.example.todoapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Model.UserSingleton;
import com.example.todoapp.R;
import com.example.todoapp.Utils.LoginDbHandler;
import com.example.todoapp.Utils.TaskDBHandler;

public class SettingsActivity extends AppCompatActivity {
    private Button deleteAcc;
    private Button cancel;
    private EditText text;
    private LoginDbHandler loginDbHandler;
    private TaskDBHandler taskDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        deleteAcc = findViewById(R.id.deleteAcc);
        text = findViewById(R.id.deleteAccText);
        cancel = findViewById(R.id.cancel);
        UserSingleton userSingleton = UserSingleton.getInstance();
        loginDbHandler = new LoginDbHandler(this);
        taskDBHandler = new TaskDBHandler(this);


        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = text.getText().toString();
                if (input.equals(userSingleton.getPassword())) {
                    loginDbHandler.deleteUser(userSingleton.getId());
                    taskDBHandler.deleteAllTask(userSingleton.getId());
                    //überprüft ob Passworteingabe mit dem des Users übereinstimmt
                    Intent gotoRegister = new Intent(SettingsActivity.this, RegisterActivity.class);
                    startActivityForResult(gotoRegister, 5);
                } else {
                    Toast.makeText(SettingsActivity.this, "Falsches Passwort", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}