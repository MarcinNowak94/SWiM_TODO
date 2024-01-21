package com.example.swim_todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swim_todo.ui.tasklist.EditTask;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private final Fragment fragment;

    public TaskAdapter(List<Task> taskList, Fragment fragment) {
        this.taskList = taskList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, fragment);

        // Dodaj obsługę kliknięcia do otwierania fragmentu edycji
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Przekazanie zadania do fragmentu edycji
                EditTask editTaskFragment = new EditTask();
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                editTaskFragment.setArguments(bundle);

                //FIXME: Application crashes when changing fragment here
                FragmentTransaction transaction = fragment.requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(fragment.requireView().getId(), editTaskFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskNameTextView;
        private final TextView taskTagsTextView;
        private final TextView taskPriorityTextView;
        private final TextView taskDueDateTextView;
        private final TextView taskIsDoneTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            taskTagsTextView = itemView.findViewById(R.id.taskTagsTextView);
            taskPriorityTextView = itemView.findViewById(R.id.taskPriorityTextView);
            taskDueDateTextView = itemView.findViewById(R.id.taskDueDateTextView);
            taskIsDoneTextView = itemView.findViewById(R.id.taskIsDoneTextView);
        }

        public void bind(Task task, Fragment fragment) {
            taskNameTextView.setText(task.getName());
            taskTagsTextView.setText(task.getTags());
            taskPriorityTextView.setText(task.getPriority());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDueDate = dateFormat.format(task.getDueDate());
            taskDueDateTextView.setText(formattedDueDate);

            String isDoneText = task.isDone() ? "Done" : "Not Done";
            taskIsDoneTextView.setText(isDoneText);

            // Dodaj inne pola, jeżeli to konieczne
        }
    }
}