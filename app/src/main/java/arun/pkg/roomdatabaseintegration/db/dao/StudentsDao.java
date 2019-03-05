package arun.pkg.roomdatabaseintegration.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import arun.pkg.roomdatabaseintegration.db.entities.Student;

@Dao
 public interface StudentsDao {

     @Query("SELECT * FROM student")
     List<Student> getAllStudentsData();

    @Query("Select COUNT(name) from student")
    Integer getStudentsCount();
 
     @Query("SELECT * FROM student")
     LiveData<List<Student>> getStudents();
/*
      *   Insert may result in conflicts which can be easily handled using onConflict   attribute.
      *   List of options are REPLACE, ROLLBACK, ABORT, FAIL & IGNORE
      */
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insertStudent(Student student);
 
     @Query("SELECT * FROM student where age > :age")
     List<Student> getStudentsFromAge(int age);
 
     @Query("DELETE FROM student WHERE name = :name")
     int deleteStudentByName(String name);
 }