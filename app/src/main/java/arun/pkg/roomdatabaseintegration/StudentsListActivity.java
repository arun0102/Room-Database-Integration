package arun.pkg.roomdatabaseintegration;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

/**
 * Activity class to show list of students and perform insertion and deletion of students from the list
 */
public class StudentsListActivity extends AppCompatActivity implements DeletionSwipeHelper.OnSwipeListener {


    private List<String> mStudentArr;
    private StudentsViewModel mStudentsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        mStudentsViewModel = ViewModelProviders.of(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(StudentsViewModel.class);
        mStudentsViewModel.observeStudentChanges(this);
        mStudentsViewModel.getStudentArrLiveDat().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> studentsList) {
                mStudentArr = studentsList;
                setAdapterData(studentsList);
            }
        });

        mStudentsViewModel.getAllStudentsData();
    }

    private void setAdapterData(List<String> studentArr) {
        RecyclerView studentsRecyclerView = findViewById(R.id.recycler_students);
        RecyclerView.LayoutManager studentsLayoutManager = new LinearLayoutManager(StudentsListActivity.this);
        studentsRecyclerView.setLayoutManager(studentsLayoutManager);

        StudentsAdapter adapter = new StudentsAdapter(this, studentArr);
        studentsRecyclerView.setAdapter(adapter);

        // Code for swipe delete
        ItemTouchHelper.Callback callback = new DeletionSwipeHelper(0, ItemTouchHelper.START, this, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(studentsRecyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int position) {
        mStudentsViewModel.deleteStudent(mStudentArr.get(position));
    }
}
