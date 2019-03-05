package arun.pkg.roomdatabaseintegration.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

@Entity(tableName = "student")
 public class Student implements Serializable {
 
     @PrimaryKey
     @NonNull
     @ColumnInfo(name = "id")
     private int id;
     @Nullable
     @ColumnInfo(name = "name")
     private String name;
     @Nullable
     @ColumnInfo(name = "age")
     private int age;
 
     public Student() {
         // Empty constructor
     }
 
     @NonNull
     public int getId() {
         return id;
     }
 
     public void setId(@NonNull int id) {
         this.id = id;
     }
 
     @Nullable
     public String getName() {
         return name;
     }
 
     public void setName(@Nullable String name) {
         this.name = name;
     }
 
     @Nullable
     public int getAge() {
         return age;
     }
 
     public void setAge(@Nullable int age) {
         this.age = age;
     }
 }