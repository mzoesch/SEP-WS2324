package com.zoeschinger.app.cards.programming;

/**
 * Includes the following cards:
 * <ul>
 *  <li>Turn Left
 *  <li>Turn Right
 *  <li>U-Turn
 * </ul>
 */
public class TurnCard extends AProgrammingCard
{
    /** In degrees clockwise (90, 180, 270) */
    private final int turnAmount;

    public TurnCard(String name, final int turnAmount)
    {
        super(String.format("Turn %d", turnAmount));
        this.turnAmount = turnAmount;
    }

    public int getTurnAmount()
    {
        return turnAmount;
    }

}
