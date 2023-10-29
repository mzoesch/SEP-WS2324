package appv2.cards;

import appv2.core.PlayerController;


/**
 * <p>Abstract super class for all cards.</p>
 */
public abstract sealed class ACard permits
        BaronTalus,
        CountessWilhelmina,
        GuardOdette,
        HandmaidSusannah,
        KingArnaud,
        PriestTomas,
        PrinceArnaud,
        PrincessAnnette
{

    /**
     * <p>Return code if the card was played successfully.</p>
     */
    public static final int RC_OK = 0;
    /**
     * <p>Return code if the card was not played successfully (e.g. blocked by the
     * Handmaid ({@link HandmaidSusannah}) or card can not be played due to other card
     * in hand ({@link CountessWilhelmina})).</p>
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
     * <p>Return code if the caller should now choose a player and call the
     * callback method of this card with a valid Player Controller.</p>
     * @see ACard#callback(PlayerController, PlayerController, StringBuilder, StringBuilder, String[])
     */
    public static final int RC_CHOOSE_ANY_PLAYER = 4;
    /**
     * <p>Return code if the caller should now choose a player self excluded and call the
     * callback method of this card with a valid Player Controller.</p>
     * @see ACard#callback(PlayerController, PlayerController, StringBuilder, StringBuilder, String[])
     */
    public static final int RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED = 5;
    /**
     * <p>Return code if the caller should now choose a player self excluded and call the
     * callback method of this card with a valid Player Controller and a valid integer.</p>
     * @see ACard#callback(PlayerController, PlayerController, StringBuilder, StringBuilder, String[])
     */
    public static final int RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER = 6;

    protected final String name;
    private final String backgroundStory;
    private final String effectDescription;

    private final int affection;

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

    /**
     * <p>Is called if a player should discard this card (manual or forced by an other player).</p>
     *
     * @param PC The Player Controller to play the card for.
     * @param bPlayedManually True if the player played the card manually, false otherwise.
     * @param messageForPlayerWhenForced Message to display to the player on his next turn if the
     *                                   card is forced to be played. Null is safe to pass fi played manually.
     * @param stdoutPipeline Standard output pipeline.
     * @param stderrPipeline Standard error pipeline.
     * @return Return codes:
     * <ul>
     *  <li>RC_OK: The card was played successfully. The caller will need to figure
     *             out how to update the affected players' hands accordingly.
     *  <li>RC_ERR: The card was not played successfully. The caller will need to revert
     *              changed variables accordingly.
     *  <li>RC_OK_HANDS_UPDATED: The card was played successfully and players hands have been already updated.
     *                           The caller must not update the hands again.
     *  <li>RC_OK_PLAYER_KNOCKED_OUT: The card was played successfully and the player has been knocked out.
     *                                The caller won't need to do anything else.
     * </ul>
     * @see ACard#RC_OK
     * @see ACard#RC_ERR
     * @see ACard#RC_OK_HANDS_UPDATED
     * @see ACard#RC_OK_PLAYER_KNOCKED_OUT
     */
    public abstract int playCard(
            PlayerController PC,
            boolean bPlayedManually,
            String messageForPlayerWhenForced,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline
    );

    /**
     * <p>Should be called if the card was already played successfully
     * ({@link #playCard(PlayerController, boolean, String, StringBuilder, StringBuilder) playCard})
     * and the caller should now choose a player and call the callback method of this card with a valid
     * Player Controller and valid args if needed.</p>
     *
     * @param PC The Player Controller to play the card for.
     * @param targetPC Target Player Controller.
     * @param stdoutPipeline Standard output pipeline.
     * @param stderrPipeline Standard error pipeline.
     * @param args Arguments to pass to the callback method.
     * @return Return codes:
     * <ul>
     *   <li>RC_OK_HANDS_UPDATED: The card was played successfully and players hands have been already updated.
     *                            The caller must not update the hands again.
     *   <li>RC_ERR: The card was not played successfully. The target Player Controller was invalid or protected.
     *               The caller must revert any changes and call this method again.
     * </ul>
     * @see ACard#RC_OK_HANDS_UPDATED
     * @see ACard#RC_ERR
     */
    public abstract int callback(
            PlayerController PC,
            PlayerController targetPC,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline,
            String[] args
    );

    // region Getters and Setters

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
        return String.format("%s [%s]", this.name, this.affection);
    }

    // endregion Getters and Setters

}
