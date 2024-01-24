package com.example.swim_todo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swim_todo.ui.edittask.EditTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private List<Task> originalTaskList; //Unfiltered tasklist
    private final Fragment fragment;
    private boolean showUndoneOnly = false;

    public TaskAdapter(List<Task> taskList, Fragment fragment) {
        this.taskList = taskList;
        this.originalTaskList = new ArrayList<>(taskList);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTask editTaskFragment = new EditTask();
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskID", task.getID());
                editTaskFragment.setArguments(bundle);

                // Get NavController and navigate to the EditTask fragment via action
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_nav_tasklist_to_nav_edit_task, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        this.originalTaskList = new ArrayList<>(tasks);
        notifyDataSetChanged();
    }

    public void showUndoneOnly(boolean showUndone) {
        this.showUndoneOnly = showUndone;
        applyFilter();
    }

    public void sortByName(boolean ascending) {
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return ascending ? t1.getName().compareTo(t2.getName()) :
                        t2.getName().compareTo(t1.getName());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByTags(boolean ascending) {
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return ascending ? t1.getTags().compareTo(t2.getTags()) :
                        t2.getTags().compareTo(t1.getTags());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByPriority(boolean ascending) {
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return ascending ? t1.getPriority().compareTo(t2.getPriority()) :
                        t2.getPriority().compareTo(t1.getPriority());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByDueDate(boolean ascending) {
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                if (ascending) {
                    return Long.compare(t1.getDueDate(), t2.getDueDate());
                } else {
                    return Long.compare(t2.getDueDate(), t1.getDueDate());
                }
            }
        });
        notifyDataSetChanged();
    }

    private void applyFilter() {
        if (showUndoneOnly) {
            taskList = originalTaskList.stream()
                    .filter(task -> !task.isDone())
                    .collect(Collectors.toList());
        } else {
            taskList = new ArrayList<>(originalTaskList);
        }
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskIDTextView;
        private final TextView taskNameTextView;
        private final TextView taskTagsTextView;
        private final TextView taskPriorityTextView;
        private final TextView taskDueDateTextView;
        private final TextView taskIsDoneTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskIDTextView = itemView.findViewById(R.id.taskIDTextView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            taskTagsTextView = itemView.findViewById(R.id.taskTagsTextView);
            taskPriorityTextView = itemView.findViewById(R.id.taskPriorityTextView);
            taskDueDateTextView = itemView.findViewById(R.id.taskDueDateTextView);
            taskIsDoneTextView = itemView.findViewById(R.id.taskIsDoneTextView);
        }

        public void bind(Task task, Fragment fragment) {
            taskIDTextView.setText(String.valueOf(task.getID()));
            taskNameTextView.setText(task.getName());
            taskTagsTextView.setText(task.getTags());
            taskPriorityTextView.setText(task.getPriority());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDueDate = dateFormat.format(task.getDueDate());
            taskDueDateTextView.setText(formattedDueDate);

            String isDoneText = task.isDone() ? "Done" : "Not Done";
            taskIsDoneTextView.setText(isDoneText);
            int backgroundColor = isDoneText.equals("Done") ? Color.GREEN : Color.TRANSPARENT;
            taskIsDoneTextView.getRootView().setBackgroundColor(backgroundColor);
        }
    }
}
