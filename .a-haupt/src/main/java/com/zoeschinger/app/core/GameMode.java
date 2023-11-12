package com.zoeschinger.app.core;

import com.zoeschinger.app.courses.Course;
import com.zoeschinger.app.cards.damage.*;
import com.zoeschinger.app.cards.upgrade.*;

import java.util.ArrayList;

public class GameMode
{
    private PlayerController[] playerControllers;

    private final Course course;

    private final ArrayList<AUpgradeCard> upgradeCardsPile;
    private final ArrayList<AUpgradeCard> discardedUpgradeCardsPile;
    private final ArrayList<AUpgradeCard> upgradeCardShop;

    private final ArrayList<SpamCard> spamCards;
    private final ArrayList<TrojanHorseCard> trojanHorseCards;
    private final ArrayList<VirusCard> virusCards;
    private final ArrayList<WormCard> wormCards;

    private final ArrayList<com.zoeschinger.app.cards.programming.ASpecialCard> specialCards;


    public GameMode(String courseTemplate, PlayerController[] playerControllers)
    {
        this.playerControllers = playerControllers;
        this.course = new Course(courseTemplate);

        this.upgradeCardsPile = new ArrayList<AUpgradeCard>();
        this.discardedUpgradeCardsPile = new ArrayList<AUpgradeCard>();
        this.upgradeCardShop = new ArrayList<AUpgradeCard>();

        this.spamCards = new ArrayList<SpamCard>();
        this.trojanHorseCards = new ArrayList<TrojanHorseCard>();
        this.virusCards = new ArrayList<VirusCard>();
        this.wormCards = new ArrayList<WormCard>();

        this.specialCards = new ArrayList<com.zoeschinger.app.cards.programming.ASpecialCard>();

        this.setupGame();
    }

    private void setupGame()
    {
        // Add cards and prepare player pawns and so on.
    }

    public AUpgradeCard[] getShop()
    {
        return upgradeCardShop.toArray(new AUpgradeCard[0]);
    }

    public boolean buyFromShop(PlayerController PC, AUpgradeCard card)
    {
        return true;
    }

    public void prepareNextRound()
    {
        // Call all player pawns to reset, refill shop, etc.
    }

    public void startRound()
    {
        // Call all pawns to start round with priority.

        // Buy phase

        // Programming phase

        // Player register 1 to 5.
    }

    public Course getCourse()
    {
        return course;
    }

    public PlayerController[] getPlayerControllers()
    {
        return null;
    }

    public com.zoeschinger.app.cards.programming.ASpecialCard drawSpecialCard()
    {
        return null;
    }

    public void startTimer()
    {
    }

}
