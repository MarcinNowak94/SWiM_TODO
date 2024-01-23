package com.example.swim_todo.ui.edittask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.swim_todo.R;
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

                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.action_nav_edit_task_to_nav_tasklist);
                    }
                });

                binding.editTaskDeleteTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteConfirmationDialog();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Error: Task ID not provided", Toast.LENGTH_SHORT).show();
            }
        }
        return root;
    }

    private void saveEditedTask() {
        String editedTaskText = binding.editTaskText.getText().toString();
        String editedTaskTags = binding.editTaskTags.getText().toString();
        String editedTaskPriority = binding.editTaskPrioritySpinner.getSelectedItem().toString();
        Boolean editedTaskisDone = binding.editTaskIsDoneCheckbox.isChecked();

        DatePicker datePicker = binding.editTaskDatePicker;
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        long editedTaskdueDate = calendar.getTimeInMillis();

        // Check if the task was changed
        if (!editedTaskText.equals(task.getName())
                || !editedTaskTags.equals(task.getTags())
                || !editedTaskPriority.equals(task.getPriority())
                || !editedTaskisDone.equals(task.isDone())
                || !(editedTaskdueDate==task.getDueDate())
        ) {
            if (!editedTaskText.isEmpty()) {
                task.setName(editedTaskText);
                task.setTags(editedTaskTags);
                task.setPriority(editedTaskPriority);
                task.setDone(editedTaskisDone);
                task.setDueDate(editedTaskdueDate);

                dbHelper.updateTask(task);
                Toast.makeText(getActivity(), "Task edited and saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Task was not changed
            Toast.makeText(getActivity(), "No changes to save!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing (cancel the dialog)
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteTask() {
        // Perform the deletion of the task
        dbHelper.deleteTask(task.getID());
        Toast.makeText(getActivity(), "Task deleted!", Toast.LENGTH_SHORT).show();

        // Navigate back to the TasklistFragment
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_edit_task_to_nav_tasklist);
    }
}
