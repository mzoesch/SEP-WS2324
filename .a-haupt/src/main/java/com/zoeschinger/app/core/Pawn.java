package com.zoeschinger.app.core;

import com.zoeschinger.app.courses.CourseCoordinate;
import com.zoeschinger.app.cards.ACard;
import com.zoeschinger.app.cards.IPlayableCard;
import com.zoeschinger.app.cards.upgrade.*;

import java.util.ArrayList;

/** A pawn interacts with the game board. */
public class Pawn
{
    private PlayerController possessor;

    private int energyCubes;

    /** Damage cards may also be in the programming cards. */
    private final ArrayList<IPlayableCard> storedProgrammingCardsInDeck;
    private final ArrayList<IPlayableCard> currentlyPlayingProgrammingCards; // Max 5
    private final ArrayList<IPlayableCard> playedProgrammingCards;

    private final ArrayList<ATemporaryUpgradeCard> ownedTemporaryUpgradeCards; // Max 3?
    private final ArrayList<APermanentUpgradeCard> ownedPermanentUpgradeCards; // Max 3?

    private int checkpointTokens; // Needed to win

    public Pawn()
    {
        this.energyCubes = 0;

        this.storedProgrammingCardsInDeck = new ArrayList<IPlayableCard>();
        this.currentlyPlayingProgrammingCards = new ArrayList<IPlayableCard>();
        this.playedProgrammingCards = new ArrayList<IPlayableCard>();


        this.ownedTemporaryUpgradeCards = new ArrayList<ATemporaryUpgradeCard>();
        this.ownedPermanentUpgradeCards = new ArrayList<APermanentUpgradeCard>();
        this.checkpointTokens = 0;
    }

    public void prepareForNextRound()
    {
        return;
    }

    public void setPossessor(PlayerController possessor)
    {
        this.possessor = possessor;
    }

    public PlayerController getPossessor()
    {
        return possessor;
    }

    public CourseCoordinate getPosition()
    {
        // A little long, we may have to define shortcut methods / variables e.g., lobby in player controller ...
        return EServerInstance.INSTANCE.getLobbyByID(this.getPossessor().getLobbyID()).getGameState().getAuthGameMode().getCourse().getPawnPosition(this).getCoordinate();
    }

    /** We should handle moving the pawn in the game mode (e.g., trigger moving other pawns, etc.) */
    public void setPosition(CourseCoordinate coordinates)
    {
        // We need to set this in the Course class
    }

    public int getEnergyCubes()
    {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes)
    {
        this.energyCubes = energyCubes;
    }

    public int getCheckpointTokens1()
    {
        return checkpointTokens;
    }

    public void addCheckpointToken()
    {
        return;
    }


}
