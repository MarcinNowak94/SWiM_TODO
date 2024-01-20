package com.example.swim_todo.ui.addtask;
import com.example.swim_todo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.swim_todo.databinding.FragmentAddtaskBinding;
import com.google.android.material.snackbar.Snackbar;

public class AddTaskFragment extends Fragment {

    private FragmentAddtaskBinding binding;
    private EditText addTaskText;
    private Button addTaskButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddTaskViewModel addTaskViewModel =
                new ViewModelProvider(this).get(AddTaskViewModel.class);

        binding = FragmentAddtaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicjalizacja pól tekstowych i przycisku
        addTaskText = root.findViewById(R.id.addtasktext);
        addTaskButton = root.findViewById(R.id.addtaskbutton);

        // Obsługa kliknięcia przycisku
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pobranie tekstu z pola tekstowego
                String taskText = addTaskText.getText().toString();

                // Sprawdzenie, czy pole tekstowe nie jest puste
                if (!taskText.isEmpty()) {
                    // Wyświetlenie Toast z zawartością pola tekstowego
                    Toast.makeText(getActivity(), "Dodano zadanie: " + taskText, Toast.LENGTH_SHORT).show();
                } else {
                    // Powiadomienie o pustym polu tekstowym
                    Toast.makeText(getActivity(), "Pole tekstowe jest puste!", Toast.LENGTH_SHORT).show();
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