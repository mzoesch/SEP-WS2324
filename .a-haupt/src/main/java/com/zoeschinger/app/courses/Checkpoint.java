package com.zoeschinger.app.courses;

/** A checkpoint is not a modifier, because it can freely move around the course during the gameplay. */
public class Checkpoint
{
    private int checkpointID;

    public Checkpoint(int checkpointID)
    {
        this.checkpointID = checkpointID;
    }

    public int getCheckpointID()
    {
        return this.checkpointID;
    }

}
