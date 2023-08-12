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
public class Todo1Fragment extends Fragment {
    private RecyclerView recyclerView;
    private Button addButton;
    private Button settings;
    private Button delButton;
    private RecyclerViewAdapter taskAdapter;
    private List<TaskModel> taskList;
    private TaskDBHandler tdbh;
    private TaskModel task;

    UserSingleton userSingleton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo1, container, false);
        //fragment_layout in view gespeichert
        addButton = view.findViewById(R.id.AddTask1);
        settings = view.findViewById(R.id.settings);
        delButton = view.findViewById(R.id.DeleteTask1);
        taskList = new ArrayList<>();
        tdbh = new TaskDBHandler(getActivity());

        if(!tdbh.todoTableExists("todo")) { // falls todo nicht existiert soll sie erstellt werden
            tdbh.onCreate(tdbh.getWritableDatabase());
        }

        tdbh.openDatabase();
        userSingleton = UserSingleton.getInstance();
        recyclerView = view.findViewById(R.id.RecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //ermöglicht lineare Anordnung der Taskobjekte
        taskAdapter = new RecyclerViewAdapter((HomeActivity) getActivity());
        recyclerView.setAdapter(taskAdapter);

        task = new TaskModel();
        task.setFragment_id(1);
// damit die Aufgaben direkt nach dem öffnen der App angezeigt werden. musste zuerst
// im signin activity die userId korrekt gesetzt werden, damit auf diese mit .getId() zugegriffen werden kann
//Fehler davor entstand, weil die userId erst in NewTaskActivity gesetzt wurde und nicht direkt nach Login

        // Aufgaben aus der Datenbank abrufen
        taskList = tdbh.getAllTasks(userSingleton.getId(), task.getFragment_id());
        taskAdapter.setTasks(taskList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (taskAdapter.getEditDelList().size() == 1) { // prüft ob nur ein todo ausgewählt wurde
                    for (TaskModel task : taskAdapter.getEditDelList()) {
                        //getDelTodo gibt die ArrayListe der ausgewählten tasks zurück
                        String todo = task.getTask();
                        int todoId = task.getId();
                        // id und task als Extra mitgeben
                        Intent goToEditTask = new Intent(getActivity(), EditTaskActivity.class);
                        goToEditTask.putExtra("editTask", todo); // name der nachricht + wert
                        goToEditTask.putExtra("taskId", String.valueOf(todoId)); // name der nachricht + wert
                        startActivityForResult(goToEditTask, 1);
                    }
                } else if (taskAdapter.getEditDelList().size() > 1) {
                    //fallls sich in der Liste mehr als ein Objekt befindet
                    Toast.makeText(getActivity(), "Es kann nur ein task bearbeitet werden", Toast.LENGTH_SHORT).show();


                } else { // falls nichts ausgewählt wurde einfach zu NewTaskActivity
                    Intent gotoNewTask = new Intent(getActivity(), NewTaskActivity1.class);
                    startActivityForResult(gotoNewTask, 1);
                }
            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gehe durch die Aufgabenliste und lösche die ausgewählten Aufgaben
                for (TaskModel task : taskAdapter.getEditDelList()) {
                    //getDelTodo gibt die ArrayListe der ausgewählten tasks zurück
                    int taskId = task.getId();
                    tdbh.deleteTask(taskId);
                    // für jedes Taskobjekt die id erhalten und damit task aus der datenbank löschen

                }

                // Aktualisiert die Aufgabenliste im Adapter
                taskList = tdbh.getAllTasks(userSingleton.getId(), task.getFragment_id());
                taskAdapter.setTasks(taskList);

                // Leert die delTodo-Liste, da die ausgewählten Aufgaben jetzt gelöscht wurden
                taskAdapter.getEditDelList().clear();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(goToSettings, 1);
            }
        });
        return view;

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userSingleton = UserSingleton.getInstance();
        // Aktualisiert die Aufgabenliste im Adapter
        taskList = tdbh.getAllTasks(userSingleton.getId(), task.getFragment_id());
        taskAdapter.setTasks(taskList);


    }
}
