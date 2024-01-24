package com.example.swim_todo.ui.addtask;
import com.example.swim_todo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.swim_todo.databinding.FragmentAddtaskBinding;
import com.example.swim_todo.TaskDatabaseHelper;                //Communication with DB
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {

    private FragmentAddtaskBinding binding;
    private EditText addTaskText;
    private EditText addTaskTagsText;
    private Spinner addTaskPrioritySpinner;
    private CheckBox addTaskisDoneCheckbox;
    private CalendarView calendarView;
    private Button addTaskButton;
    private TaskDatabaseHelper dbHelper;
    private long dueDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddTaskViewModel addTaskViewModel =
                new ViewModelProvider(this).get(AddTaskViewModel.class);

        binding = FragmentAddtaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        addTaskText = root.findViewById(R.id.addtasktext);
        addTaskTagsText=root.findViewById(R.id.addtasktags);
        addTaskPrioritySpinner=root.findViewById(R.id.addtaskprioritySpinner);
        addTaskisDoneCheckbox=root.findViewById(R.id.addTaskIsDoneCheckbox);
        calendarView = root.findViewById(R.id.addtaskduedate);
        addTaskButton = root.findViewById(R.id.addtaskbutton);
        dbHelper = new TaskDatabaseHelper(getActivity());

        // Set the initial dueDate to the current date
        dueDate = System.currentTimeMillis();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                dueDate = selectedCalendar.getTimeInMillis();
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = addTaskText.getText().toString();
                String taskTags = addTaskTagsText.getText().toString();
                String priority = addTaskPrioritySpinner.getSelectedItem().toString();
                Boolean isDone = addTaskisDoneCheckbox.isChecked();

                if (!taskText.isEmpty()) {
                    long rowId = dbHelper.insertTask(taskText, dueDate, priority, taskTags, isDone);

                    if (rowId != -1) {
                        Toast.makeText(getActivity(), "Task saved to database!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error saving task!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Taskname cannot be empty!", Toast.LENGTH_SHORT).show();
                }

                // Obtain a reference to the NavController
                NavController navController = Navigation.findNavController(v);

                // Navigate to the EditTask fragment using the appropriate action
                navController.navigate(R.id.action_nav_addtask_to_nav_tasklist);
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