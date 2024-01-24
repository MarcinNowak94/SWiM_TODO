package com.example.swim_todo;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swim_todo.databinding.ActivityMainBinding;

//Requirements: https://www.tomaszx.pl/materialy/pum_projekt.pdf
//TO-DO List application. Grades:
    //Grade Requirements DONE (x/12)
    //3     5
    //3,5   7
    //4     8   <-We are here
    //4,5   9
    //5     12

//Remaining features:
    //DONE: Display task list
    //DONE: Add task to list
    //DONE: Edit task from list
    //DONE: Delete task from list
    //DONE: Mark task as done
    //TODO: Generate reminder for tasks To DO today or overdue via Android NotificationCompat
    //DONE: Import an export tasks to text file [done as DB]
    //DONE: Prioritize tasks
    //DONE: Sort tasks by at least 3 criteria, eg: date added, due date, priority, task name, status
    //TODO: Add attachment to tasks – photo, video (eg. from device camera),
    //TODO: Remove attachment from task
    //TODO: Display task attachments

//Additional resources used:
    //Icons:    https://fonts.google.com/icons
    //Example:  https://www.youtube.com/playlist?list=PLhhNsarqV6MQ59jOKACuCzASgBGMuR-km


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_addtask, R.id.nav_calendar, R.id.nav_tasklist, R.id.nav_edittask)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}