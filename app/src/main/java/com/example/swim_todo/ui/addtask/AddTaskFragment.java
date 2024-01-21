package com.example.swim_todo.ui.addtask;
import com.example.swim_todo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.swim_todo.databinding.FragmentAddtaskBinding;
import com.example.swim_todo.TaskDatabaseHelper;                //Communication with DB
import com.google.android.material.snackbar.Snackbar;

public class AddTaskFragment extends Fragment {

    private FragmentAddtaskBinding binding;
    private EditText addTaskText;
    private Button addTaskButton;
    private EditText addTaskTagsText;
    private CalendarView calendarView;
    private TaskDatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddTaskViewModel addTaskViewModel =
                new ViewModelProvider(this).get(AddTaskViewModel.class);

        binding = FragmentAddtaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        addTaskText = root.findViewById(R.id.addtasktext);
        calendarView = root.findViewById(R.id.addtaskduedate);
        addTaskButton = root.findViewById(R.id.addtaskbutton);
        addTaskTagsText=root.findViewById(R.id.addtasktags);

        dbHelper = new TaskDatabaseHelper(getActivity());

        //Date returned in epochtime(ms)
        long dueDate = calendarView.getDate();

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = addTaskText.getText().toString();
                String taskTags = addTaskTagsText.getText().toString();
                String priority = "Low"; //TODO: Add list in UI: Low, Medium, High, Critical
                Boolean isDone = false;

                if (!taskText.isEmpty()) {
                    long rowId = dbHelper.insertTask(taskText, dueDate, priority, taskTags, isDone);

                    if (rowId != -1) {
                        Toast.makeText(getActivity(), "Task saved to database!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Powiadomienie o niepowodzeniu zapisu
                        Toast.makeText(getActivity(), "Error saving task!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Taskname is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}