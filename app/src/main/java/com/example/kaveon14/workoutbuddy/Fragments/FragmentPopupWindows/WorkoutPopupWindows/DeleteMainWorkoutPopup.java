package com.example.kaveon14.workoutbuddy.Fragments.FragmentPopupWindows.WorkoutPopupWindows;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.kaveon14.workoutbuddy.DataBase.TableManagers.MainWorkoutTable;
import com.example.kaveon14.workoutbuddy.Fragments.FragmentPopupWindows.PopupWindowManager;
import com.example.kaveon14.workoutbuddy.R;
import java.util.List;

public class DeleteMainWorkoutPopup extends PopupWindowManager {

    private ArrayAdapter mainWorkoutAdapter;
    private List<String> mainWorkoutNames;

    public DeleteMainWorkoutPopup(View root) {
        setRootView(root);
        setPopupLayout(R.layout.deletesubworkout_popup_layout);
        setPopupViewId(R.id.deleteSubWorkoutPopup);
    }

    public void showPopupWindow() {
        displayPopupWindow();
        setMainWorkoutListView();
    }

    public void setMainWorkoutAdapter(ArrayAdapter mainWorkoutAdapter) {
        this.mainWorkoutAdapter = mainWorkoutAdapter;
    }

    public void setMainWorkoutNames(List<String> mainWorkoutNames) {
        this.mainWorkoutNames = mainWorkoutNames;
    }

    private void setMainWorkoutListView() {
        ListView listView = (ListView) popupLayout.findViewById(R.id.deleteSubWorkoutPopup_listView);
        listView.setAdapter(mainWorkoutAdapter);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetSubWorkoutListViewColors(parent);
                parent.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                setDeleteButton(parent.getItemAtPosition(position).toString());
            }
        });
    }

    private void setDeleteButton(String mainWorkoutName) {
        Button btn = (Button) popupLayout.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMainWorkout(mainWorkoutName);
                Toast.makeText(context,"MainWorkout Successfully Deleted!"
                        ,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });
    }

    private void deleteMainWorkout(String mainWorkoutName) {
        mainWorkoutNames.remove(mainWorkoutName);
        mainWorkoutAdapter.notifyDataSetChanged();

        MainWorkoutTable mainWorkoutTable = new MainWorkoutTable(context);
        mainWorkoutTable.deleteMainWorkout(mainWorkoutName);
    }

    private void resetSubWorkoutListViewColors(AdapterView<?> parent) {
        for(int x=0;x<parent.getCount();x++) {
            View view = parent.getChildAt(x);
            view.setBackgroundColor(Color.WHITE);
        }
    }
}