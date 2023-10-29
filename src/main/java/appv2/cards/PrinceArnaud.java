package appv2.cards;

import appv2.core.GameState;
import appv2.core.PlayerController;

import java.util.Objects;


/**
 * <p>Prince Arnaud card.</p>
 * <p><b>Special Effect:</b> <br />
 * As written in the rules. If the owner has the Countess in their hand,
 * they must discard the Countess and take the Prince.<br />
 * When discarded the player must choose another player to discard their
 * hand and draw a new one (self included).</p>
 * @see ACard
 * @see #playCard(PlayerController, boolean, String, StringBuilder, StringBuilder) playCard
 */
public non-sealed class PrinceArnaud extends ACard {

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

    /**
     * <p><b>Special Effect:</b> <br />
     * As written in the rules. If the owner has the Countess in their hand,
     * they must discard the Countess and take the Prince ({@link ACard#RC_ERR}).<br />
     * When discarded the player must choose another player to discard their
     * hand and draw a new one (self included) ({@link ACard#RC_CHOOSE_ANY_PLAYER}).</p>
     * <br />
     * {@inheritDoc}
     * @see ACard#RC_OK
     * @see ACard#RC_ERR
     * @see ACard#RC_CHOOSE_ANY_PLAYER
     */
    @Override
    public int playCard(
            PlayerController PC,
            boolean bPlayedManually,
            String messageForPlayerWhenForced,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline
    ) {
        if (bPlayedManually) {
            if (PC.hasCountessWilhelmina()) {
                stderrPipeline.append("You must discard the Countess Wilhelmina.\n");
                return ACard.RC_ERR;
            }

            stdoutPipeline.append("Choose a player to discard their hand.\n");
            return ACard.RC_CHOOSE_ANY_PLAYER;
        }

        if (PC.isProtected()) {
            stderrPipeline.append("This player is protected.\n");
            return ACard.RC_ERR;
        }

        PC.setMessageForPlayerNextTurn(messageForPlayerWhenForced);
        return ACard.RC_OK;
    }

    /**
     * <p>Returns {@link ACard#RC_ERR} if the target player is protected or invalid. Otherwise
     * the target player will draw a new card ({@link ACard#RC_OK_HANDS_UPDATED}).</p>
     * <p>The args are ignored here.</p>
     * {@inheritDoc}
     * @see ACard#RC_ERR
     * @see ACard#RC_OK_HANDS_UPDATED
     */
    @Override
    public int callback(
            PlayerController PC,
            PlayerController targetPC,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline,
            String[] args
    ) {
        if (targetPC.isProtected()) {
            stderrPipeline.append("This player is protected.\n");
            return ACard.RC_ERR;
        }

        if (Objects.equals(PC.getHandCard().getName(), PrinceArnaud.NAME)) {
            PC.addToDiscardedCardsPile(PC.getHandCard());
            PC.setHandCard(PC.getTableCard());
            PC.setTableCard(null);
        }
        else {
            PC.addToDiscardedCardsPile(PC.getTableCard());
            PC.setTableCard(null);
        }

        int RC = targetPC.getHandCard().playCard(
                targetPC,
                false,
                "A Prince was played on you.",
                stdoutPipeline,
                stderrPipeline
        );

        if (RC == ACard.RC_ERR) {
            stderrPipeline.append("This player is protected.\n");
            return ACard.RC_ERR;
        }

        if (Objects.equals(PC, targetPC) && RC == ACard.RC_OK_PLAYER_KNOCKED_OUT) {
            stdoutPipeline.append(
                    "You have discarded your hand and have been knocked out by the side effect of your other card.\n"
            );
            return ACard.RC_OK_HANDS_UPDATED;
        }

        if (RC == ACard.RC_OK_PLAYER_KNOCKED_OUT) {
            stdoutPipeline.append(String.format(
                    "You have discarded %s's hand and they have been knocked out.\n",
                    targetPC.getPlayerName()
            ));
            targetPC.setMessageForPlayerNextTurn("A Prince was played on you.\nYou have been knocked out.");
            return ACard.RC_OK_HANDS_UPDATED;
        }

        targetPC.addToDiscardedCardsPile(targetPC.getHandCard());
        targetPC.setHandCard(GameState.getActiveGameMode().drawCard());

        if (Objects.equals(PC, targetPC)) {
            stdoutPipeline.append(String.format(
                    "You have discarded your hand and drawn %s.\n",
                    PC.getHandCard().getAsString()
            ));
            return ACard.RC_OK_HANDS_UPDATED;
        }

        stdoutPipeline.append(String.format(
                "You have discarded %s's hand and they drawn a new one.\n",
                targetPC.getPlayerName()
        ));
        targetPC.setMessageForPlayerNextTurn("A Prince was played on you.\nYou have drawn a new card.");

        return ACard.RC_OK_HANDS_UPDATED;
    }

}
