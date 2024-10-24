package com.ctom314.homework03_program1;

import java.io.Serializable;

public class Query implements Serializable
{
    // Vars
    private String fName;
    private String lName;
    private String username;
    private String gpaOperator;
    private String gpa;
    private String majorName;
    private boolean searchByMajor;

    // Constructor
    public Query(String f, String l, String u, String gpaOp, String g, String m, boolean s)
    {
        fName = f;
        lName = l;
        username = u;
        gpaOperator = gpaOp;
        gpa = g;
        majorName = m;
        searchByMajor = s;
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

    public String getGpaOperator()
    {
        return gpaOperator;
    }

    public String getGpa()
    {
        return gpa;
    }

    public String getMajorName()
    {
        return majorName;
    }

    public boolean getSearchByMajor()
    {
        return searchByMajor;
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

    public void setGpaOperator(String gpaOp)
    {
        gpaOperator = gpaOp;
    }

    public void setGpa(String g)
    {
        gpa = g;
    }

    public void setMajorName(String m)
    {
        majorName = m;
    }

    public void setSearchByMajor(boolean s)
    {
        searchByMajor = s;
    }

}
