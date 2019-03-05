package arun.pkg.roomdatabaseintegration;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.List;
import java.util.stream.Collectors;

import arun.pkg.roomdatabaseintegration.background.StudentWorker;
import arun.pkg.roomdatabaseintegration.db.StudentsDatabase;
import arun.pkg.roomdatabaseintegration.db.dao.StudentsDao;
import arun.pkg.roomdatabaseintegration.db.entities.Student;

/**
 * View model class to communicate with the worker classes to perform DB operations.
 */
public class StudentsViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> mStudentArr = new MutableLiveData<>();

    public StudentsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getAllStudentsData() {
        WorkManager workManager = WorkManager.getInstance();
        Data.Builder builder = new Data.Builder();
        builder.putInt(Constants.ACTION_TYPE, Constants.ACTION_INSERT);

        OneTimeWorkRequest insertRequest = new OneTimeWorkRequest.Builder(StudentWorker.class)
                .setInputData(builder.build()).build();

        Data.Builder queryBuilder = new Data.Builder();
        queryBuilder.putInt(Constants.ACTION_TYPE, Constants.ACTION_QUERY);
        OneTimeWorkRequest queryRequest = new OneTimeWorkRequest.Builder(StudentWorker.class)
                .setInputData(queryBuilder.build()).build();

        workManager.beginWith(insertRequest).then(queryRequest).enqueue();
    }

    public void deleteStudent(String name) {
        WorkManager workManager = WorkManager.getInstance();
        Data.Builder builder = new Data.Builder();
        builder.putInt(Constants.ACTION_TYPE, Constants.ACTION_DELETE);
        builder.putString(Constants.STUDENT_NAME, name);

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(StudentWorker.class)
                .setInputData(builder.build()).build();

        workManager.enqueue(request);
    }

    public void observeStudentChanges(Context context) {
        StudentsDatabase database = StudentsDatabase.getInstance(context);
        StudentsDao mStudentsDao = database.studentsDao();
        LiveData<List<Student>> studentsLiveData = mStudentsDao.getStudents();
        studentsLiveData.observe((LifecycleOwner) context, students -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mStudentArr.setValue(students.stream()
                        .map(Student::getName)
                        .collect(Collectors.toList()));
            }
        });
    }

    public MutableLiveData<List<String>> getStudentArrLiveDat() {
        return mStudentArr;
    }
}
