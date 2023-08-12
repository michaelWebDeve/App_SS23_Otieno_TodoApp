package com.example.todoapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.RecyclerViewAdapter;
import com.example.todoapp.Model.TaskModel;
import com.example.todoapp.Model.UserSingleton;
import com.example.todoapp.R;
import com.example.todoapp.Utils.TaskDBHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Todo4Fragment extends Fragment {
    private RecyclerView recyclerView;
    private Button addButton;
    private Button delButton;
    private Button settings;
    private RecyclerViewAdapter taskAdapter;
    private List<TaskModel> taskList;
    private TaskDBHandler tdbh;
    private TaskModel task;


    UserSingleton userSingleton;

    //@SuppressLint("MissingInflatedId")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo4, container, false);
        addButton = view.findViewById(R.id.AddTask4);
        delButton = view.findViewById(R.id.DeleteTask4);
        settings= view.findViewById(R.id.settings);
        taskList = new ArrayList<>();
        tdbh = new TaskDBHandler(getActivity());

        // tdbh.onCreate(tdbh.getWritableDatabase()); // Hier die onCreate-Methode aufrufen
        tdbh.openDatabase();
        userSingleton = UserSingleton.getInstance();

        recyclerView = view.findViewById(R.id.RecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskAdapter = new RecyclerViewAdapter((HomeActivity) getActivity());
        recyclerView.setAdapter(taskAdapter);
        task = new TaskModel();
        task.setFragment_id(4);
        // Aufgaben aus der Datenbank abrufen
        taskList = tdbh.getAllTasks(userSingleton.getId(), task.getFragment_id());
        taskAdapter.setTasks(taskList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (taskAdapter.getEditDelList().size()==1){ // prüft ob nur ein todo ausgewählt wurde
                    for (TaskModel task : taskAdapter.getEditDelList()) {
                        //getDelTodo gibt die ArrayListe der ausgewählten tasks zurück
                        String todo = task.getTask();
                        int todoId = task.getId();

                        Intent goToEditTask = new Intent(getActivity(), EditTaskActivity.class);
                        goToEditTask.putExtra("editTask", todo); // name der nachricht + wert
                        goToEditTask.putExtra("taskId",String.valueOf(todoId)); // name der nachricht + wert
                        startActivityForResult(goToEditTask, 4);
                    }
                }else if (taskAdapter.getEditDelList().size()>1) {
                    Toast.makeText(getActivity(), "Es kann nur ein task bearbeitet werden", Toast.LENGTH_SHORT).show();


                }else {
                    Intent gotoNewTask = new Intent(getActivity(), NewTaskActivity4.class);
                    startActivityForResult(gotoNewTask, 4);
                }
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gehe durch die Aufgabenliste und lösche die ausgewählten Aufgaben
                for (TaskModel task : taskAdapter.getEditDelList()) {
                    int taskId = task.getId();
                    tdbh.deleteTask(taskId);
                // für jedes Taskobjekt die id erhalten und damit task aus der datenbank löschen

                }

                // Aktualisiere die Aufgabenliste im Adapter
                taskList = tdbh.getAllTasks(userSingleton.getId(), task.getFragment_id());
                taskAdapter.setTasks(taskList);

                // Leere die delTodo-Liste, da die ausgewählten Aufgaben jetzt gelöscht wurden
                taskAdapter.getEditDelList().clear();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(goToSettings, 4);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            userSingleton = UserSingleton.getInstance();
            taskList = tdbh.getAllTasks(userSingleton.getId(), task.getFragment_id());
            taskAdapter.setTasks(taskList);

    }
}
