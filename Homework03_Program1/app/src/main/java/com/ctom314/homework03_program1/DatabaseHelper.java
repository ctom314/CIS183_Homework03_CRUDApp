package com.ctom314.homework03_program1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "studentDB";
    private static final String STUDENT_TABLE_NAME = "Students";
    private static final String MAJOR_TABLE_NAME = "Majors";
    private static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context c)
    {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create Majors Table
        db.execSQL("CREATE TABLE " + MAJOR_TABLE_NAME + " (" +
                "majorId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "majorName VARCHAR(50), " +
                "majorPrefix VARCHAR(10))");

        // Create Students Table
        db.execSQL("CREATE TABLE " + STUDENT_TABLE_NAME + " (" +
                "studentId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "fName VARCHAR(50), " +
                "lName VARCHAR(50), " +
                "username VARCHAR(50), " +
                "email VARCHAR(50), " +
                "age INTEGER, " +
                "gpa REAL, " +
                "majorId INTEGER, " +
                "FOREIGN KEY (majorId) REFERENCES " + MAJOR_TABLE_NAME + "(majorId))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j)
    {
        // Drop tables, then create
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + MAJOR_TABLE_NAME + ";");

        onCreate(db);
    }

    // Getters
    public String getStudentTableName()
    {
        return STUDENT_TABLE_NAME;
    }

    public String getMajorTableName()
    {
        return MAJOR_TABLE_NAME;
    }

    // Init dummy data
    public void initAllTables()
    {
        initMajors();
        initStudents();
    }

    // Dummy data for Students Table
    private void initStudents()
    {
        // Only add data if DB is empty
        if (countRows(STUDENT_TABLE_NAME) == 0)
        {
            // Get database
            SQLiteDatabase db = this.getWritableDatabase();

            // Add students
            db.execSQL("INSERT INTO " + STUDENT_TABLE_NAME +
                    " (fName, lName, username, email, age, gpa, majorId) " +
                    "VALUES ('Tom', 'Smith', 'tsmith', 'tsmith@mail.com', 20, 3.5, 1);");
            db.execSQL("INSERT INTO " + STUDENT_TABLE_NAME +
                    " (fName, lName, username, email, age, gpa, majorId) " +
                    "VALUES ('Jack', 'Douglas', 'jdouglas', 'jdouglas@hotmail.com', 17, 2.9, 2);");
            db.execSQL("INSERT INTO " + STUDENT_TABLE_NAME +
                    " (fName, lName, username, email, age, gpa, majorId) " +
                    "VALUES ('Franklin', 'Jonas', 'fjonas', 'fjonas@gmail.com', 23, 3.2, 1);");
            db.execSQL("INSERT INTO " + STUDENT_TABLE_NAME +
                    " (fName, lName, username, email, age, gpa, majorId) " +
                    "VALUES ('Catherine', 'Robinson', 'crobinson', 'crobinson@mail.com', 19, 3, 3);");
            db.execSQL("INSERT INTO " + STUDENT_TABLE_NAME +
                    " (fName, lName, username, email, age, gpa, majorId) " +
                    "VALUES ('Johnny', 'Johnson', 'jjohnson', 'jjohnson@gmail.com', 18, 2.4, 2);");

            // Close database
            db.close();
        }
    }

    // Dummy data for Majors Table
    private void initMajors()
    {
        // Only add data if DB is empty
        if (countRows(MAJOR_TABLE_NAME) == 0)
        {
            // Get database
            SQLiteDatabase db = this.getWritableDatabase();

            // Add majors
            db.execSQL("INSERT INTO " + MAJOR_TABLE_NAME + " (majorName, majorPrefix) " +
                    "VALUES ('Computer Science', 'CIS');");
            db.execSQL("INSERT INTO " + MAJOR_TABLE_NAME + " (majorName, majorPrefix) " +
                    "VALUES ('Accounting', 'ACC');");
            db.execSQL("INSERT INTO " + MAJOR_TABLE_NAME + " (majorName, majorPrefix) " +
                    "VALUES ('Biology', 'BIO');");
        }
    }

    // Count rows in table
    public int countRows(String tableName)
    {
        // Get count
        String query = "SELECT COUNT(*) FROM " + tableName + ";";

        // Get database
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor != null) 
        {
            cursor.moveToFirst();

            // Cannot just return cursor.getInt(0) because the cursor cannot be closed that way.
            // Must store the count in a var so the cursor can be closed properly.
            int count = cursor.getInt(0);
            cursor.close();

            return count;
        }

        // Close database
        db.close();
        
        // Return -1 if error
        return -1;
    }

    // Get Student Object from database
    public Student getStudent(int studentId)
    {
        Student student = null;
        
        // Get database
        SQLiteDatabase db = this.getReadableDatabase();

        // Get student
        String studentQuery = "SELECT * FROM " + STUDENT_TABLE_NAME + " WHERE studentId = '" + studentId + "';";
        Cursor cursor = db.rawQuery(studentQuery, null);
        
        if (cursor != null)
        {
            cursor.moveToFirst();

            // Get student vars
            String fName = cursor.getString(1);
            String lName = cursor.getString(2);
            String username = cursor.getString(3);
            String email = cursor.getString(4);
            int age = cursor.getInt(5);
            double gpa = cursor.getDouble(6);
            int majorId = cursor.getInt(7);

            // Get major name
            String majorName = getMajor(majorId).getName();

            // Make student
            student = new Student(fName, lName, username, email, age, gpa, majorName);

            cursor.close();
        }

        // Close database
        db.close();

        // Return student
        // TODO: Replace majorName with majorId
        return student;
    }
    
    // Get Major Object from database
    public Student.Major getMajor(int majorId)
    {
        Student.Major major = null;
        
        // Get database
        SQLiteDatabase db = this.getReadableDatabase();

        // Get major
        String majorQuery = "SELECT majorName, majorPrefix FROM " + MAJOR_TABLE_NAME + " WHERE majorId = '" + majorId + "';";
        Cursor cursor = db.rawQuery(majorQuery, null);
        
        if (cursor != null)
        {
            cursor.moveToFirst();

            // Get major vars
            String majorName = cursor.getString(0);
            String majorPrefix = cursor.getString(1);

            // Make major
            major = new Student.Major(majorId, majorName, majorPrefix);

            cursor.close();
        }

        // Close database
        db.close();

        // Return major
        return major;
    }

    // Get all students from database
    // Will be used to sync with studentList
    public ArrayList<Student> getAllStudents()
    {
        ArrayList<Student> students = new ArrayList<>();

        // Get database
        SQLiteDatabase db = this.getReadableDatabase();

        // Get students
        String studentQuery = "SELECT * FROM " + STUDENT_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(studentQuery, null);

        if (cursor != null)
        {
            cursor.moveToFirst();

            // Loop through students
            while (!cursor.isAfterLast())
            {
                // Get student vars
                String fName = cursor.getString(1);
                String lName = cursor.getString(2);
                String username = cursor.getString(3);
                String email = cursor.getString(4);
                int age = cursor.getInt(5);
                double gpa = cursor.getDouble(6);
                int majorId = cursor.getInt(7);

                // Get major name
                String majorName = getMajor(majorId).getName();

                // Make student
                Student student = new Student(fName, lName, username, email, age, gpa, majorName);
                students.add(student);

                // Move to next student
                cursor.moveToNext();
            }

            cursor.close();
        }

        // Close database
        db.close();

        // Return students
        return students;
    }

    // Get all majors from database
    public ArrayList<Student.Major> getAllMajors()
    {
        ArrayList<Student.Major> majors = new ArrayList<>();

        // Get database
        SQLiteDatabase db = this.getReadableDatabase();

        // Get majors
        String majorQuery = "SELECT * FROM " + MAJOR_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(majorQuery, null);

        if (cursor != null)
        {
            cursor.moveToFirst();

            // Loop through majors
            while (!cursor.isAfterLast())
            {
                // Get major vars
                int majorId = cursor.getInt(0);
                String majorName = cursor.getString(1);
                String majorPrefix = cursor.getString(2);

                // Make major
                Student.Major major = new Student.Major(majorId, majorName, majorPrefix);
                majors.add(major);

                // Move to next major
                cursor.moveToNext();
            }

            cursor.close();
        }

        // Close database
        db.close();

        // Return majors
        return majors;
    }
}
