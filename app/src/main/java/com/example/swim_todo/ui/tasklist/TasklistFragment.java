package com.example.swim_todo.ui.tasklist;
import com.example.swim_todo.Task;
import com.example.swim_todo.TaskAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TasklistViewModel tasklistViewModel =
                new ViewModelProvider(this).get(TasklistViewModel.class);


        binding = FragmentTasklistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(getActivity());
        List<Task> taskList = dbHelper.getAllTasks();

        // Inicjalizacja RecyclerView i adaptera
        RecyclerView recyclerView = binding.tasklist;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}