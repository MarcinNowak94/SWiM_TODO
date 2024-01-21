package com.example.swim_todo.ui.tasklist;

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

public class EditTask extends Fragment {

    private FragmentEditTaskBinding binding;
    private TaskDatabaseHelper dbHelper;
    private Task task;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new TaskDatabaseHelper(getActivity());

        // Pobierz dane przekazane z poprzedniego fragmentu (ID zadania do edycji)
        Bundle bundle = getArguments();
        if (bundle != null) {
            long taskId = bundle.getLong("taskId", -1);
            if (taskId != -1) {
                // Wczytaj dane zadania z bazy danych na podstawie ID
                task = dbHelper.getTask(taskId);

                // Ustaw wartości pól formularza na podstawie danych zadania
                binding.editTaskText.setText(task.getName());
                // Ustaw inne pola formularza, takie jak datę, tagi, itp.

                // Obsługa kliknięcia przycisku zapisu edycji
                binding.editTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEditedTask();
                    }
                });
            } else {
                // Obsługa błędu - brak przekazanego ID zadania
                Toast.makeText(getActivity(), "Error: Task ID not provided", Toast.LENGTH_SHORT).show();
            }
        }

        return root;
    }

    private void saveEditedTask() {
        // Pobierz edytowane dane z pól formularza
        String editedTaskText = binding.editTaskText.getText().toString();
        // Pobierz inne edytowane pola formularza, takie jak data, tagi, itp.

        // Sprawdź, czy zadanie nie jest puste
        if (!editedTaskText.isEmpty()) {
            // Zapisz edytowane dane zadania do bazy danych
            task.setName(editedTaskText);
            // Ustaw inne edytowane pola zadania, takie jak data, tagi, itp.
            dbHelper.updateTask(task);

            // Powiadomienie o zapisie edycji
            Toast.makeText(getActivity(), "Task edited and saved!", Toast.LENGTH_SHORT).show();
        } else {
            // Powiadomienie o pustym polu tekstowym
            Toast.makeText(getActivity(), "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}