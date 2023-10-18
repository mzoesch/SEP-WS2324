package app.cards;

import app.PlayerController;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public abstract class Card {

    protected final String name;
    private final String backgroundStory;
    private final String effectDescription;

    private final int affection;

    public static final int RC_OK = 0;
    public static final int RC_ERR = 1;
    public static final int RC_OK_HANDS_UPDATED = 2;
    public static final int RC_OK_PLAYER_KNOCKED_OUT = 3;

    public Card(String name, String backgroundStory, String effectDescription, int affection) {
        this.name = name;
        this.backgroundStory = backgroundStory;
        this.effectDescription = effectDescription;
        this.affection = affection;

        return;
    }

    // region Getters

    public String getName() {
        return this.name;
    }

    public String getBackgroundStory() {
        return this.backgroundStory;
    }

    public String getEffectDescription() {
        return this.effectDescription;
    }

    public int getAffection() {
        return this.affection;
    }

    public String getAsString() {
        return String.format("%s (Affection: %s)", this.name, this.affection);
    }

    public String getAsDetailedString() {
        return String.format("%s\n%s\n%s\n", this.getAsString(), this.backgroundStory, this.effectDescription);
    }

    // endregion Getters

    /**
     * Return codes: <p>
     *     0: The card was played successfully. <p>
     *     1: The card was not played successfully. <p>
     *     2: The card was played successfully and players hands have been already updated. <p>
     *     3: The card was played successfully and the player has been knocked out. <p>
     */
    public abstract int playEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Boolean bIsHandCard,
            String messageForPlayerWhenForced
    );

    public static PlayerController[] getAllRemainingPlayersTargetableByCardEffects(PlayerController PC) {
        ArrayList<PlayerController> targetablePCs = new ArrayList<PlayerController>();

        for (PlayerController tPC : PC.getActiveGameMode().getRemainingPlayers()) {
            if (Objects.equals(PC.getPlayerName(), tPC.getPlayerName()))
                continue;
            if (tPC.getProtectedByHandmaid())
                continue;

            targetablePCs.add(tPC);
            continue;
        }

        return targetablePCs.toArray(new PlayerController[0]);
    }
}
