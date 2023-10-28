package appv2.cards;


import appv2.core.PlayerController;

/**
 * <p>Prince Arnaud card.</p>
 * @see ACard
 */
public class PrinceArnaud extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Prince Arnaud";
    private static final int CARD_AFFECTION = 5;

    /**
     * <p>Constructor.</p>
     */
    public PrinceArnaud() {
        super(
                PrinceArnaud.NAME,
                "As a social gady, Prince Arnaud was not as distressed over his mother’s arrest as one "
                        + "would suppose. Since many women clamor for his attention, he hopes to help his sister "
                        +" find the same banal happiness by playing matchmaker.",
                "When you discard Prince Arnaud, choose one player still in the round (including yourself). "
                        + "That player discards his or her hand (but doesn't apply its effect, unless it is the "
                        + "Princess, see page 8) and draws a new one. If the deck is empty and the player cannot "
                        + "draw a card, that player draws the card that was removed at the start of the round. "
                        + "If all other players are protected by the Handmaid, you must choose yourself.",
                PrinceArnaud.CARD_AFFECTION
        );

        return;
    }

    @Override
    public int playCard(PlayerController PC, boolean bPlayedManually, String messageForPlayerWhenForced, StringBuilder stdoutPipeline, StringBuilder stderrPipeline) {
        return 0;
    }

    @Override
    public int callback(PlayerController PC, PlayerController targetPC, StringBuilder stdoutPipeline, StringBuilder stderrPipeline, String messageForPlayerWhenForced) {
        return 0;
    }

}
