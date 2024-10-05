package com.ctom314.homework03_program1;

import java.io.Serializable;

public class Student implements Serializable
{
    // Vars
    private String fName;
    private String lName;
    private String username;
    private String email;
    private int age;
    private double gpa;
    private String major;

    // Constructor
    public Student(String f, String l, String u, String e, int a, double g, String m)
    {
        fName = f;
        lName = l;
        username = u;
        email = e;
        age = a;
        gpa = g;
        major = m;
    }

    // Getters
    public String getFName()
    {
        return fName;
    }

    public String getLName()
    {
        return lName;
    }

    public String getUsername()
    {
        return username;
    }

    public String getEmail()
    {
        return email;
    }

    public int getAge()
    {
        return age;
    }

    public double getGpa()
    {
        return gpa;
    }

    public String getMajor()
    {
        return major;
    }

    // Setters
    public void setFName(String f)
    {
        fName = f;
    }

    public void setLName(String l)
    {
        lName = l;
    }

    public void setUsername(String u)
    {
        username = u;
    }

    public void setEmail(String e)
    {
        email = e;
    }

    public void setAge(int a)
    {
        age = a;
    }

    public void setGpa(double g)
    {
        gpa = g;
    }

    // Major static class
    static class Major
    {
        // Vars
        private int id;
        private String name;
        private String prefix;

        // Constructor
        public Major(int i, String n, String p)
        {
            id = i;
            name = n;
            prefix = p;
        }

        // Getters
        public int getId()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }

        public String getPrefix()
        {
            return prefix;
        }

        // Setters
        public void setId(int i)
        {
            id = i;
        }

        public void setName(String n)
        {
            name = n;
        }

        public void setPrefix(String p)
        {
            prefix = p;
        }
    }
}
