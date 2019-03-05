package arun.pkg.roomdatabaseintegration.background;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import arun.pkg.roomdatabaseintegration.Constants;
import arun.pkg.roomdatabaseintegration.db.StudentsDatabase;
import arun.pkg.roomdatabaseintegration.db.dao.StudentsDao;
import arun.pkg.roomdatabaseintegration.db.entities.Student;

/**
 * Worker class to execute DB related queries in background
 */
public class StudentWorker extends Worker {
    @NonNull
    private final StudentsDao mStudentsDao;

    public StudentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        StudentsDatabase database = StudentsDatabase.getInstance(context);
        mStudentsDao = database.studentsDao();
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        int action = getInputData().getInt(Constants.ACTION_TYPE, Constants.ACTION_INSERT);
        switch (action) {
            case Constants.ACTION_INSERT:
                if (!isStudentsDataAvailable()) {
                    addStudents();
                }
                break;
            case Constants.ACTION_QUERY:
                List<Student> studentsList = getAllStudents();
                List<String> namesArr;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    namesArr = studentsList.stream()
                            .map(Student::getName)
                            .collect(Collectors.toList());
                    String[] arr = new String[namesArr.size()];
                    Data outputData = new Data.Builder()
                            .putStringArray(Constants.STUDENTS_LIST, namesArr.toArray(arr))
                            .build();
                    return Result.success(outputData);
                }
            case Constants.ACTION_DELETE:
                String name = getInputData().getString(Constants.STUDENT_NAME);
                deleteStudentWithName(name);
                return Result.success();
        }

        return Result.success();
    }

    /**
     * Check if students data is available in database or not
     *
     * @return true if students count in DB is more than 0
     */
    private boolean isStudentsDataAvailable() {
        Integer count = mStudentsDao.getStudentsCount();
        return count > 0;
    }

    /**
     * Add students data in DB
     */
    private void addStudents() {
        // Let's insert 20 students data into database
        for (int i = 0; i < 20; i++) {
            Student student = new Student();
            student.setId(i + 1);
            student.setName("Student " + (i + 1));
            student.setAge(i + 5);

            insertStudentsData(student);
        }
    }

    /**
     * Running insert command
     *
     * @param student student data to be inserted
     */
    private void insertStudentsData(Student student) {
        mStudentsDao.insertStudent(student);
    }

    /**
     * Get list of all the students available in DB
     *
     * @return List of students
     */
    private List<Student> getAllStudents() {
        // Get list of all students
        List<Student> studentList = mStudentsDao.getAllStudentsData();
        return studentList;
    }

    /**
     * Run delete command
     *
     * @param name student name to be deleted
     */
    private void deleteStudentWithName(String name) {
        // delete students with name
        mStudentsDao.deleteStudentByName(name);
    }

    /**
     * Get list of students with age greater than the mentioned date
     *
     * @param age filter value to get students more than this age
     * @return List of students with age more than the specified age
     */
    private List<Student> getStudentsWithAge(int age) {
        // Get list of all students with age greater than
        List<Student> studentList = mStudentsDao.getStudentsFromAge(age);
        return studentList;
    }
}
