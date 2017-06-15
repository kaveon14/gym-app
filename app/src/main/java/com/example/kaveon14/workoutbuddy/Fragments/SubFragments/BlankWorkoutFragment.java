package com.example.kaveon14.workoutbuddy.Fragments.SubFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.kaveon14.workoutbuddy.DataBase.Data.Exercise;
import com.example.kaveon14.workoutbuddy.DataBase.TableManagers.SubWorkoutTable;
import com.example.kaveon14.workoutbuddy.Fragments.MainFragments.ExerciseFragment;
import com.example.kaveon14.workoutbuddy.R;
import java.util.ArrayList;
import java.util.List;
import static com.example.kaveon14.workoutbuddy.DataBase.DatabaseManagment.DataBaseContract.SubWorkoutData.COLUMN_EXERCISE_NAMES;
import static com.example.kaveon14.workoutbuddy.DataBase.DatabaseManagment.DataBaseContract.SubWorkoutData.COLUMN_EXERCISE_REPS;
import static com.example.kaveon14.workoutbuddy.DataBase.DatabaseManagment.DataBaseContract.SubWorkoutData.COLUMN_EXERCISE_SETS;

public class BlankWorkoutFragment extends Fragment {

    public BlankWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank_workout, container, false);
        setTextView(rootView);
        setListView(rootView);
        return rootView;
    }

    private void setTextView(View rootView) {
        TextView textView = (TextView) rootView.findViewById(R.id.workoutNameView);
        textView.setText(SubWorkoutFragment.clickedSubWorkout.getSubWorkoutName());
    }

    private void setListView(View rootView) {
        ListView listView = (ListView) rootView.findViewById(R.id.blankWorkout_listView);
        listView.setAdapter(setWorkoutAdapter());
        viewExerciseOnClick(listView);
    }

    private void viewExerciseOnClick(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise clickedExercise = getSubWorkoutExercise(position);
                showExercise(clickedExercise);
            }
        });
    }

    private void showExercise(Exercise clickedExercise) {
        ExerciseFragment.clickedExercise = clickedExercise;
        BlankExerciseFragment blankExerciseFragment = new BlankExerciseFragment();
        getFragmentManager().beginTransaction()
                .hide(this)
                .add(R.id.blankExercise_fragment,blankExerciseFragment)
                .addToBackStack(null)
                .commit();
    }

    private WorkoutAdapter setWorkoutAdapter() {
        SubWorkoutTable subWorkoutTable = new SubWorkoutTable(getContext());
        String tableName = SubWorkoutFragment.clickedSubWorkout.getSubWorkoutName() + "_wk";
        int amountOfExercises = subWorkoutTable.getColumn(tableName,COLUMN_EXERCISE_NAMES).size();
        List<Exercise> exercises = new ArrayList<>();
        for(int x=0;x<amountOfExercises;x++) {
            exercises.add(getSubWorkoutExercise(x));
        }
        return new WorkoutAdapter(getContext(),exercises);
    }

    private Exercise getSubWorkoutExercise(int increment) {
        SubWorkoutTable subWorkoutTable = new SubWorkoutTable(getContext());
        String tableName = SubWorkoutFragment.clickedSubWorkout.getSubWorkoutName() +"_wk";

        List<String> exerciseNames = subWorkoutTable.getColumn(tableName,COLUMN_EXERCISE_NAMES);
        List<String> exerciseSets = subWorkoutTable.getColumn(tableName,COLUMN_EXERCISE_SETS);
        List<String> exerciseReps = subWorkoutTable.getColumn(tableName,COLUMN_EXERCISE_REPS);

        Exercise exercise = new Exercise(exerciseNames.get(increment),null);
        exercise.setExerciseSets(exerciseSets.get(increment));
        exercise.setExerciseReps(exerciseReps.get(increment));

        return exercise;
    }

    private static class WorkoutAdapter extends BaseAdapter  {

        private List<Exercise> exerciseList;
        private Context context;
        private TextView exerciseNameView;

        public WorkoutAdapter(Context context,List<Exercise> exercises) {
            this.context = context;
            this.exerciseList = exercises;
        }

        public int getCount() {
            return exerciseList.size();
        }

        public Object getItem(int position) {
            return exerciseList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View rowView, ViewGroup viewGroup) {
            Exercise exercise = exerciseList.get(position);
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.blank_workout_list_item, null);
            }
            setListItemView(rowView,exercise);
            return rowView;
        }

        private void setListItemView(View rowView,Exercise exercise) {
            setExerciseNameTextView(rowView,exercise);
            setExerciseRepsTextView(rowView,exercise);
            setExerciseSets(rowView,exercise);
        }

        private void setExerciseNameTextView(View rowView,Exercise exercise) {
            exerciseNameView = (TextView) rowView.findViewById(R.id.nameView);
            exerciseNameView.setText(exercise.getExerciseName());
        }

        private void setExerciseRepsTextView(View rowView,Exercise exercise) {
            TextView repsView = (TextView) rowView.findViewById(R.id.repsView);
            repsView.setText(exercise.getExerciseReps());
        }

        private void setExerciseSets(View rowView,Exercise exercise) {
            TextView setsView = (TextView) rowView.findViewById(R.id.setsView);
            setsView.setText(exercise.getExerciseSets());
        }

    }
}
