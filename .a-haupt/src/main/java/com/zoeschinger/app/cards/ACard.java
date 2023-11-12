package com.zoeschinger.app.cards;

public abstract class ACard
{
    private String name;

    public ACard(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}
