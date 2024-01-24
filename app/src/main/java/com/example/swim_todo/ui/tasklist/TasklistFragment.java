package com.example.swim_todo.ui.tasklist;
import com.example.swim_todo.R;
import com.example.swim_todo.Task;
import com.example.swim_todo.TaskAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swim_todo.TaskDatabaseHelper;
import com.example.swim_todo.databinding.FragmentTasklistBinding;

import java.util.ArrayList;
import java.util.List;

public class TasklistFragment extends Fragment {

    private FragmentTasklistBinding binding;
    private TaskAdapter taskAdapter;
    private Spinner sortSpinner;
    private CheckBox doneCheckBox;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TasklistViewModel tasklistViewModel = new ViewModelProvider(this).get(TasklistViewModel.class);

        binding = FragmentTasklistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(getActivity());
        List<Task> taskList = dbHelper.getAllTasks();

        sortSpinner = binding.sortSpinner;
        doneCheckBox = binding.doneCheckBox;

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                android.R.layout.simple_spinner_item
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        RecyclerView recyclerView = binding.tasklist;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskAdapter = new TaskAdapter(taskList, this.getParentFragment());
        recyclerView.setAdapter(taskAdapter);

        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            taskAdapter.showUndoneOnly(isChecked);
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                boolean ascending = true;
                switch (position) {
                    case 0:
                        taskAdapter.sortByName(ascending);
                        break;
                    case 1:
                        taskAdapter.sortByTags(ascending);
                        break;
                    case 2:
                        taskAdapter.sortByPriority(ascending);
                        break;
                    case 3:
                        taskAdapter.sortByDueDate(ascending);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Do nothing while nothing selected
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