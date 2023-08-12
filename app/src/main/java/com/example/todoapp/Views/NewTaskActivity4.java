package com.example.todoapp.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Model.TaskModel;
import com.example.todoapp.Model.UserSingleton;
import com.example.todoapp.R;
import com.example.todoapp.Utils.TaskDBHandler;

public class NewTaskActivity4 extends AppCompatActivity {

    Button save;
    Button cancel;
    EditText text;
    private TaskDBHandler tdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        save = findViewById(R.id.saveB);
        cancel = findViewById(R.id.cancelB);
        text = findViewById(R.id.newTaskTv);
        tdb = new TaskDBHandler(this); // Initialisiere die TaskDBHandler-Instanz
        UserSingleton userSingleton = UserSingleton.getInstance();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = text.getText().toString();

                if (!input.isEmpty()) {
                    TaskModel task = new TaskModel();
                    tdb.openDatabase();
                    task.setTask(input);
                    task.setStatus(0); // standardmäßig als unchecked markiert
                    task.setFragment_id(4);
                    tdb.insertTask(userSingleton.getId(), task); // Füge den Task in die Datenbank ein

                    Toast.makeText(NewTaskActivity4.this, " erfolgreich", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Toast.makeText(NewTaskActivity4.this, "Das Feld darf nicht leer sein", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

