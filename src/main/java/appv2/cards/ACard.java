package appv2.cards;

import appv2.core.PlayerController;


public abstract class ACard {

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

    public static final int RC_CHOOSE_ANY_PLAYER = 4;
    public static final int RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED = 5;

    protected final String name;
    private final String backgroundStory;
    private final String effectDescription;

    private final int affection;

    public ACard(String name, String backgroundStory, String effectDescription, int affection) {
        this.name = name;
        this.backgroundStory = backgroundStory;
        this.effectDescription = effectDescription;
        this.affection = affection;

        return;
    }

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
        return String.format("%s [%s]", this.name, this.affection);
    }

    /**
     * <p>Is called if a player should discard this card (manual or forced by an other player).</p>
     *
     * @param PC                         The Player Controller to play the card for.
     * @param bPlayedManually            True if the card was played manually by the player, false otherwise.
     * @param messageForPlayerWhenForced Message to display to the player on his next turn if the
     *                                   card is forced to be played. Null is safe to pass fi played manually.
     * @param stdoutPipeline
     * @param stderrPipeline
     * @return Return codes:
     * <ul>
     *  <li>RC_OK: The card was played successfully.
     *  <li>RC_ERR: The card was not played successfully.
     *  <li>RC_OK_HANDS_UPDATED: The card was played successfully and players hands have been already updated.
     *  <li>RC_OK_PLAYER_KNOCKED_OUT: The card was played successfully and the player has been knocked out.
     * </ul>
     */
    public abstract int playCard(
            PlayerController PC,
            boolean bPlayedManually,
            String messageForPlayerWhenForced,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline
    );

    public abstract int callback(
            PlayerController PC,
            PlayerController targetPC, StringBuilder stdoutPipeline, StringBuilder stderrPipeline, String messageForPlayerWhenForced);

}
