package com.example.todoapp.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Model.TaskModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.TaskDBHandler;

public class EditTaskActivity extends AppCompatActivity {

    private Button save;
    private Button cancel;
    private  EditText text;
    private TaskDBHandler tdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        save = findViewById(R.id.saveButton);
        cancel = findViewById(R.id.cancelButton);
        text = findViewById(R.id.newTask);
        tdb = new TaskDBHandler(this); // Initialisiere die TaskDBHandler-Instanz


        String editTodo = getIntent().getStringExtra("editTask");
        text.setText(editTodo);
        int taskId = Integer.parseInt(getIntent().getStringExtra("taskId")); //TaskId speichern
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = text.getText().toString();

                if (!input.isEmpty()) {
                    TaskModel task = new TaskModel();
                    tdb.openDatabase();
                    task.setTask(input);
                    tdb.updateTask(taskId, task.getTask()); // Füge den Task in die Datenbank ein
                    Toast.makeText(EditTaskActivity.this, " erfolgreich", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Toast.makeText(EditTaskActivity.this, "Das Feld darf nicht leer sein", Toast.LENGTH_SHORT).show();
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
