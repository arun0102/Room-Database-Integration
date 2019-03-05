package arun.pkg.roomdatabaseintegration.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import arun.pkg.roomdatabaseintegration.db.dao.StudentsDao;
import arun.pkg.roomdatabaseintegration.db.entities.Student;

@Database(entities = {Student.class}, version = 1, exportSchema = false)
public abstract class StudentsDatabase extends RoomDatabase {
    private static final Object lock = new Object();
    private static StudentsDatabase INSTANCE = null;

    public static StudentsDatabase getInstance(Context context) {
        synchronized (lock) {
            if (null == INSTANCE) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StudentsDatabase.class, "students.db")
                        // .allowMainThreadQueries()     * Can be added to allow DB transactions on the main thread *
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
            return INSTANCE;
        }
    }

    /**
     * Migration code to be executed on change of versions from 1 to 2
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE student_new (id int, name TEXT, age TEXT, PRIMARY KEY(id))");
            // Copy the data
            database.execSQL(
                    "INSERT INTO student_new (id, name, age) SELECT id, name, age FROM student");
            // Remove the old table
            database.execSQL("DROP TABLE student");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE student_new RENAME TO student");
        }
    };

    public abstract StudentsDao studentsDao();
}