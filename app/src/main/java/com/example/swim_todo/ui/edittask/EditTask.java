package com.example.swim_todo.ui.edittask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.swim_todo.Task;
import com.example.swim_todo.TaskDatabaseHelper;
import com.example.swim_todo.databinding.FragmentEditTaskBinding;

import java.util.Calendar;
import java.util.Date;

public class EditTask extends Fragment {

    private FragmentEditTaskBinding binding;
    private TaskDatabaseHelper dbHelper;
    private Task task;

    public int getYearFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public int getMonthFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public int getDayFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new TaskDatabaseHelper(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            //TODO: change to getTask
            long taskId = bundle.getLong("taskID", -1);

            if (taskId != -1) {
                task = dbHelper.getTask(taskId);
                Date taskduedate=new Date(task.getDueDate());

                binding.editTaskText.setText(task.getName());
                binding.editTaskTags.setText(task.getTags());
                binding.editTaskDatePicker.updateDate(
                        getYearFromDate(taskduedate),
                        getMonthFromDate(taskduedate),
                        getDayFromDate(taskduedate));

                binding.editTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEditedTask();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Error: Task ID not provided", Toast.LENGTH_SHORT).show();
            }
        }

        return root;
    }
    //TODO: Update only if task was changed
    private void saveEditedTask() {
        String editedTaskText = binding.editTaskText.getText().toString();
        String editedTaskTags = binding.editTaskTags.getText().toString();

        if (!editedTaskText.isEmpty()) {
            task.setName(editedTaskText);
            task.setTags(editedTaskTags);

            dbHelper.updateTask(task);
            Toast.makeText(getActivity(), "Task edited and saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
