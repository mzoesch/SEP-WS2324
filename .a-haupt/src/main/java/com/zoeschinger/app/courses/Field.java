package com.zoeschinger.app.courses;

import com.zoeschinger.app.core.Pawn;
import com.zoeschinger.app.courses.modifier.AFieldModifier;

/** A field on the board. */
public class Field
{
    private final CourseCoordinate coordinate;

    private Pawn pawn;
    private Checkpoint checkpoint;
    private final AFieldModifier modifier;

    public Field(CourseCoordinate coordinate, AFieldModifier modifier)
    {
        this.modifier = modifier;
        this.coordinate = coordinate;
    }

    public CourseCoordinate getCoordinate()
    {
        return this.coordinate;
    }

    public boolean isOccupiedByAPawn()
    {
        return this.pawn != null;
    }

    public boolean hasModifier()
    {
        return this.modifier != null;
    }

    public AFieldModifier getModifier()
    {
        return this.modifier;
    }

    public boolean hasCheckpoint()
    {
        return this.checkpoint != null;
    }

    public Checkpoint getCheckpoint()
    {
        return this.checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint)
    {
        this.checkpoint = checkpoint;
    }

    public void setPawn(Pawn pawn)
    {
        this.pawn = pawn;
    }

    public Pawn getPawn()
    {
        return this.pawn;
    }

}
