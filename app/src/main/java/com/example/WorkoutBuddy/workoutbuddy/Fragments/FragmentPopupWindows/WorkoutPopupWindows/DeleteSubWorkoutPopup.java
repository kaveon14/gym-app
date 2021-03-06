package com.example.WorkoutBuddy.workoutbuddy.Fragments.FragmentPopupWindows.WorkoutPopupWindows;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.WorkoutBuddy.workoutbuddy.DataBase.TableManagers.MainWorkoutTable;
import com.example.WorkoutBuddy.workoutbuddy.DataBase.TableManagers.SubWorkoutTable;
import com.example.WorkoutBuddy.workoutbuddy.Fragments.MainFragments.MainWorkoutFragment;
import com.example.WorkoutBuddy.workoutbuddy.Fragments.Managers.PopupWindowManager;
import com.example.WorkoutBuddy.workoutbuddy.R;
import java.util.List;

public class DeleteSubWorkoutPopup extends PopupWindowManager {

    private ArrayAdapter subWorkoutAdapter;
    private List<String> subWorkoutNames;
    private int subWorkoutCount;//not sure why needed

    public DeleteSubWorkoutPopup(View root, Context context) {
        setRootView(root);
        setWindowManagerContext(context);
        setPopupLayout(R.layout.recycleview_popup_layout);
        setPopupViewId(R.id.recycleViewPopupLayout);
    }

    public void showPopupWindow() {
        displayPopupWindow();
        setSubWorkoutListView();
    }

    public void setCurrentSubWorkoutCount(int subWorkoutCount) {
        this.subWorkoutCount = subWorkoutCount;
    }

    public void setSubWorkoutAdapter(ArrayAdapter subWorkoutAdapter) {
        this.subWorkoutAdapter = subWorkoutAdapter;
    }

    public void setSubWorkoutNames(List<String> subWorkoutNames) {
        this.subWorkoutNames = subWorkoutNames;
    }

    private void setSubWorkoutListView() {
        ListView listView = (ListView) popupLayout.findViewById(R.id.deleteSubWorkoutPopup_listView);
        listView.setAdapter(subWorkoutAdapter);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetSubWorkoutListViewColors(parent);
                parent.getChildAt(position -
                        parent.getFirstVisiblePosition()).setBackgroundColor(Color.LTGRAY);
                if(MainWorkoutFragment.getClickedMainWorkout()
                        .getMainWorkoutName().equals("Default Workout")){
                    resetDeleteButton();
                    Toast.makeText(context,"Can Not Delete This SubWorkout!"
                            ,Toast.LENGTH_SHORT).show();
                } else {
                    setDeleteBtn(parent.getItemAtPosition(position).toString());
                }
            }
        });
    }

    private void setDeleteBtn(String subWorkoutName) {
        Button btn = (Button) popupLayout.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSubWorkout(subWorkoutName);
                subWorkoutCount--;
                Toast.makeText(context,"SubWorkout Successfully Deleted!"
                        ,Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });
    }

    private void resetDeleteButton() {
        Button btn = (Button) popupLayout.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setHapticFeedbackEnabled(true);
                Toast.makeText(context,"Can Not Delete This SubWWorkout!"
                        ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSubWorkout(String subWorkoutName) {
        subWorkoutNames.remove(subWorkoutName);
        subWorkoutAdapter.notifyDataSetChanged();

        MainWorkoutTable mainWorkoutTable = new MainWorkoutTable(context);
        mainWorkoutTable.deleteSubWorkout(MainWorkoutFragment.getClickedMainWorkout()
                        .getMainWorkoutName(), subWorkoutName);

        SubWorkoutTable subWorkoutTable = new SubWorkoutTable(context);
        String tableName = subWorkoutTable.getCorrectTableName(MainWorkoutFragment
                .getClickedMainWorkout().getMainWorkoutName(),subWorkoutName);

        subWorkoutTable.deleteSubWorkoutTable(tableName);
    }

    private void resetSubWorkoutListViewColors(AdapterView<?> parent) {
        for(int x=0;x <= parent.getLastVisiblePosition() - parent.getFirstVisiblePosition();x++) {
            parent.getChildAt(x).setBackgroundColor(Color.WHITE);
        }
    }

}
