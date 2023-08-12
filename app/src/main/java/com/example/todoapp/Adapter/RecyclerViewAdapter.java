package com.example.todoapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Model.TaskModel;
import com.example.todoapp.R;
import com.example.todoapp.Views.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    // Konstruktor, um die Datenliste zu übergeben
    private List<TaskModel> todoList; // Liste von TaskModels
    private ArrayList<TaskModel> editDelList = new ArrayList<>(); // Liste von TaskModels,-->
    //ausgewählte Objekte zum bearbeiten oder löschen
    private HomeActivity activity;


    public RecyclerViewAdapter(HomeActivity activity) {
        this.activity = activity;
//Adapter wird Homeactivity übergeben
    }


    // Wird aufgerufen, um einen ViewHolder zu erstellen, der die ganzen Tasks darstellt
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        //LayoutInflater verwandelt xml_layouts in JavaObjekte
        //layout für die Taskobjekte in task_layout festgelegt
        return new ViewHolder(itemView);
    }

    // Wird aufgerufen, um die Daten an einen ViewHolder zu binden und anzuzeigen
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // hier auf die tasks zugreifen
        TaskModel item = todoList.get(position); //Taskmodel aus der RecyclerView Liste abrufen
        holder.taskCheckBox.setText(item.getTask());
        holder.taskCheckBox.setChecked(toBoolean(item.getStatus()));  // status wurde als int deklariert -> hier in Bool umwandeln

        holder.taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //OnCheckedChangeListener falls Taskobjekt ausgewählt wurde(isChecked)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  auf die CheckBox-Aktion reagieren

                if (isChecked) {
                    editDelList.add(item);
                } else {
                    editDelList.remove(item);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;  // if n not 0 return true
    }

    public void setTasks(List<TaskModel> todoList) {
        this.todoList = todoList;
        //aktualisiert die TaskList
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return todoList.size();

    }

    public ArrayList<TaskModel> getEditDelList() {
        return editDelList;
        // gibt Liste der ausgewählten Aufgaben zurück
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        TextView taskText;

        // enthält Layout für jedes TaskObjekt und wird in onCreateView aufgerufen
        ViewHolder(View view) {
            super(view);
            taskCheckBox = view.findViewById(R.id.todoCheck4);
            taskText = view.findViewById(R.id.RecylerView);
        }
    }
}
