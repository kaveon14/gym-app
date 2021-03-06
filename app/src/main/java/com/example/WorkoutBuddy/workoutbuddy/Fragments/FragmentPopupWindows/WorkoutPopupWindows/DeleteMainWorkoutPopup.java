package com.example.WorkoutBuddy.workoutbuddy.Fragments.FragmentPopupWindows.WorkoutPopupWindows;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.WorkoutBuddy.workoutbuddy.DataBase.Data.MainWorkout;
import com.example.WorkoutBuddy.workoutbuddy.DataBase.TableManagers.MainWorkoutTable;
import com.example.WorkoutBuddy.workoutbuddy.Fragments.Managers.PopupWindowManager;
import com.example.WorkoutBuddy.workoutbuddy.Fragments.MainFragments.MainWorkoutFragment;
import com.example.WorkoutBuddy.workoutbuddy.R;
import java.util.List;

public class DeleteMainWorkoutPopup extends PopupWindowManager {

    private int position;
    private List<MainWorkout> mainWorkoutList;
    private MainWorkoutFragment.RecyclerAdapter recyclerAdapter;

    public DeleteMainWorkoutPopup(View root,Context context) {
        setRootView(root);
        setPopupViewId(R.id.recycleViewPopupLayout);
        setWindowManagerContext(context);
        setPopupLayout(R.layout.recycleview_popup_layout);
    }

    public void showPopupWindow() {
        displayPopupWindow();
        setMainWorkoutListView();
        Button btn = (Button) popupLayout.findViewById(R.id.button);
        btn.setText("Delete MainWorkout");
    }

    public void setRecyclerAdapter(MainWorkoutFragment.RecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
    }

    public void setMainWorkoutList(List<MainWorkout> mainWorkoutList) {
        this.mainWorkoutList = mainWorkoutList;
    }

    public void setPosition(int posit) {//why is this even passed in
        this.position = position;
    }

    private void setMainWorkoutListView() {
        ListView listView = (ListView) popupLayout.findViewById(R.id.deleteSubWorkoutPopup_listView);
        listView.setAdapter(new SubWorkoutAdapter(mainWorkoutList));
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetSubWorkoutListViewColors(parent);
                parent.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                if(position != 0) {
                    setDeleteButton(mainWorkoutList.get(position),position);
                } else {
                    resetDeleteButton();
                    Toast.makeText(context,"Can Not Delete This MainWorkout!"
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDeleteButton(MainWorkout mainWorkout,int position) {
        Button btn = (Button) popupLayout.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMainWorkout(mainWorkout,position);
                Toast.makeText(context,"MainWorkout Successfully Deleted!"
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
                Toast.makeText(context,"Can Not Delete This MainWorkout!"
                        ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMainWorkout(MainWorkout mainWorkout,int position) {
        mainWorkoutList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);

        MainWorkoutTable mainWorkoutTable = new MainWorkoutTable(context);
        mainWorkoutTable.deleteMainWorkout(mainWorkout.getMainWorkoutName());
    }

    //create better method possibly use a qeuqe
    private void resetSubWorkoutListViewColors(AdapterView<?> parent) {
        for(int x=0;x<parent.getCount();x++) {
            View view = parent.getChildAt(x);
            view.setBackgroundColor(Color.WHITE);
        }
    }

    private class SubWorkoutAdapter extends BaseAdapter {

        private List<MainWorkout> mainWorkoutList;

        public SubWorkoutAdapter(List<MainWorkout> mainWorkoutList) {
            this.mainWorkoutList = mainWorkoutList;
        }

        @Override
        public int getCount() {
            return mainWorkoutList.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public MainWorkout getItem(int position) {
            return mainWorkoutList.get(position);
        }

        @Override
        public View getView(int position,View rowView,ViewGroup viewGroup) {
            MainWorkout mainWorkout = mainWorkoutList.get(position);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.simple_list_item,null);
            setListItemView(rowView,mainWorkout);
            return rowView;
        }

        private void setListItemView(View rowView,MainWorkout mainWorkout) {
            TextView textView = (TextView) rowView.findViewById(R.id.simpleTextView);
            textView.setText(mainWorkout.getMainWorkoutName());
        }
    }
}
