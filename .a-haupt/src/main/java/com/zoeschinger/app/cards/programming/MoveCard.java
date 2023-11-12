package com.zoeschinger.app.cards.programming;

/**
 * Includes the following cards:
 * <ul>
 *  <li>Move 1
 *  <li>Move 2
 *  <li>Move 3
 *  <li>Back Up (essentially move -1)
 * </ul>
 *
 */
public class MoveCard extends AProgrammingCard
{
    private final int moveAmount;

    public MoveCard(final int moveAmount)
    {
        super(String.format("Move %d", moveAmount));
        this.moveAmount = moveAmount;
    }

    public int getMoveAmount()
    {
        return moveAmount;
    }

}
