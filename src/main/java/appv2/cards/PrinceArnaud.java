package appv2.cards;

import appv2.core.GameState;
import appv2.core.PlayerController;

import java.util.Objects;

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

    /**
     * <p><b>Special Effect:</b> <br />
     * As written in the rules. If the owner has the Countess in their hand,
     * they must discard the Countess and take the Prince.<br />
     * When discarded the player must choose another player to discard their
     * hand and draw a new one (self included).</p>
     * <br />
     * {@inheritDoc}
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
