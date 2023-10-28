package app.cards;

import app.PlayerController;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


/**
 * <p>Abstract super class for a card.</p>
 */
public abstract class ACard {

    protected final String name;
    private final String backgroundStory;
    private final String effectDescription;

    private final int affection;

    /**
     * <p>Return code if the card was played successfully.</p>
     */
    public static final int RC_OK = 0;
    /**
     * <p>Return code if the card was not played successfully (e.g. blocked by the Handmaid).</p>
     */
    public static final int RC_ERR = 1;
    /**
     * <p>Return code if the card was played successfully and the players hands have
     * been already updated. The caller must not update the hands again.</p>
     */
    public static final int RC_OK_HANDS_UPDATED = 2;
    /**
     * <p>Return code if the card was played successfully and the player has been knocked out.</p>
     */
    public static final int RC_OK_PLAYER_KNOCKED_OUT = 3;

    /**
     * <p>Super constructor.</p>
     *
     * @param name Name of the card.
     * @param backgroundStory Background story of the card.
     * @param effectDescription Description of the effect of the card.
     * @param affection Affection of the card.
     */
    public ACard(String name, String backgroundStory, String effectDescription, int affection) {
        this.name = name;
        this.backgroundStory = backgroundStory;
        this.effectDescription = effectDescription;
        this.affection = affection;

        return;
    }

    // region Getters

    /**
     * @return Name of the card.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Background story of the card.
     */
    public String getBackgroundStory() {
        return this.backgroundStory;
    }

    /**
     * @return Description of the effect of the card.
     */
    public String getEffectDescription() {
        return this.effectDescription;
    }

    /**
     * @return Affection of the card.
     */
    public int getAffection() {
        return this.affection;
    }

    /**
     * @return String representation of the card.
     */
    public String getAsString() {
        return String.format("%s (Affection: %s)", this.name, this.affection);
    }

    /**
     * @return String representation of the card with background story and effect description.
     */
    public String getAsDetailedString() {
        return String.format("%s\n%s\n%s\n", this.getAsString(), this.backgroundStory, this.effectDescription);
    }

    // endregion Getters

    /**
     * <p>Is called if a player should discard this card (manual or forced by an other player).</p>
     *
     * <p>Return codes:</p>
     * <ul>
     *  <li>RC_OK: The card was played successfully.
     *  <li>RC_ERR: The card was not played successfully.
     *  <li>RC_OK_HANDS_UPDATED: The card was played successfully and players hands have been already updated.
     *  <li>RC_OK_PLAYER_KNOCKED_OUT: The card was played successfully and the player has been knocked out.
     * </ul>
     *
     * @param scanner The scanner to read the input from. This is only used if the
     *                card is played manually (if not manually, null is safe to pass).
     * @param PC The Player Controller to play the card for.
     * @param bPlayedManually True if the card is played manually, false otherwise.
     * @param bIsHandCard True if the card is a hand card, false otherwise.
     *                    Null is safe to pass if not played manually.
     * @param messageForPlayerWhenForced Message to display to the player if the card is forced to be
     *                                   played. Null is safe to pass if played manually.
     * @return Return code.
     */
    public abstract int playEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Boolean bIsHandCard,
            String messageForPlayerWhenForced
    );

    /**
     * <p>Gives all valid player's from the point of view from the given PC that
     * are targetable by any card effects.</p>
     *
     * @param PC Provided to exclude the current active player from the list of targetable players.
     * @return All valid Players.
     */
    public static PlayerController[] getAllRemainingPlayersTargetableByCardEffects(PlayerController PC) {
        ArrayList<PlayerController> targetablePCs = new ArrayList<PlayerController>();

        for (PlayerController tPC : PC.getActiveGameMode().getRemainingPlayers()) {
            if (Objects.equals(PC.getPlayerName(), tPC.getPlayerName()))
                continue;
            if (tPC.isProtectedByHandmaid())
                continue;

            targetablePCs.add(tPC);
            continue;
        }

        return targetablePCs.toArray(new PlayerController[0]);
    }
}
