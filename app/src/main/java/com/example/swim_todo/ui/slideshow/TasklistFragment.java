package com.example.swim_todo.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.swim_todo.databinding.FragmentTasklistBinding;

public class TasklistFragment extends Fragment {

    private FragmentTasklistBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TasklistViewModel tasklistViewModel =
                new ViewModelProvider(this).get(TasklistViewModel.class);

        binding = FragmentTasklistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}