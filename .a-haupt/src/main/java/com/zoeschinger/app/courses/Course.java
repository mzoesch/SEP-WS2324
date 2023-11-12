package com.zoeschinger.app.courses;

import com.zoeschinger.app.core.Pawn;

public class Course
{
    /** We may read predefined templates from a file (or final array) and construct the equivalent course on the fly. */
    private String template;

    private final Field[][] fields;

    public Course(String template)
    {
        this.template = template;

        this.fields = constructCourse();

    }

    private Field[][] constructCourse()
    {
        return null;
    }

    public Field getField(CourseCoordinate coordinate)
    {
        return null;
    }

    /** Advanced courses can have multiple reboot fields. */
    public Field getAssociatedRebootField(CourseCoordinate coordinate)
    {
        return null;
    }

    public Field getPawnPosition(Pawn pawn)
    {
        return null;
    }

    // etc.

}
