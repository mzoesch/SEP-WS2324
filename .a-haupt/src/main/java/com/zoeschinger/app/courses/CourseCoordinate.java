package com.zoeschinger.app.courses;

public class CourseCoordinate
{
    private final int x;
    private final int y;

    public CourseCoordinate(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    /** As example. */
    public CourseCoordinate getLeft()
    {
       return null;
    }

    public CourseCoordinate getRight()
    {
       return null;
    }

    public CourseCoordinate getTop()
    {
       return null;
    }

    public CourseCoordinate getBottom()
    {
       return null;
    }

}
