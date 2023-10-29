package appv2.cards;

import appv2.core.PlayerController;

import java.util.Objects;


/**
 * <p>King Arnaud IV card.</p>
 * <p><b>Special Effect:</b> <br />
 * As written in the rules. If the owner has the Countess in their hand,
 * they must discard the Countess and take the King.<br />
 * When discarded the player must choose another player to swap hands with (self included).</p>
 *
 * @see ACard
 * @see #playCard(PlayerController, boolean, String, StringBuilder, StringBuilder) playCard
 */
public non-sealed class KingArnaud extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "King Arnaud IV";
    private static final int CARD_AFFECTION = 6;

    /**
     * <p>Constructor.</p>
     */
    public KingArnaud() {
        super(
                KingArnaud.NAME,
                "The undisputed ruler of Tempest... for the moment. Because of his role in the arrest "
                    + "of Queen Marianna, he does not rate as highly with Princess Annette as a father "
                    + "should. He hopes to work himself back into her graces.",
                "When you discard King Arnaud IV, trade the card in your hand with the card held by another "
                    + "player of your choice. You cannot trade with a player who is out of the round.",
                KingArnaud.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p>Special one time use to update the hand of the players after the King was played.</p>
     *
     * @param PC The player controller who invoked the effect.
     * @param targetPC The player controller who was chosen to swap hands with.
     */
    private static void swapHands(PlayerController PC, PlayerController targetPC) {
        ACard tempCard1 = PC.getHandCard();
        ACard tempCard2 = targetPC.getHandCard();

        PC.setHandCard(tempCard2);
        targetPC.setHandCard(tempCard1);

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * As written in the rules. If the owner has the Countess in their hand,
     * they must discard the Countess and take the King ({@link ACard#RC_ERR}).<br />
     * When discarded the player must choose another player to swap hands with
     * (self included) ({@link ACard#RC_CHOOSE_ANY_PLAYER}).</p>
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

            stdoutPipeline.append("Choose a player to swap hands with.\n");
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
     * <p>Returns {@link ACard#RC_ERR} if the target player is protected or invalid. Otherwise the
     * hands of the players are swapped ({@link ACard#RC_OK_HANDS_UPDATED}).</p>
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

        if (Objects.equals(PC, targetPC)) {
            stdoutPipeline.append("You have swapped hands with yourself.\n");

            if (Objects.equals(PC.getHandCard().getName(), KingArnaud.NAME)) {
                PC.addToDiscardedCardsPile(PC.getHandCard());
                PC.setHandCard(PC.getTableCard());
                PC.setTableCard(null);

                return ACard.RC_OK_HANDS_UPDATED;
            }

            PC.addToDiscardedCardsPile(PC.getTableCard());
            PC.setTableCard(null);

            return ACard.RC_OK_HANDS_UPDATED;
        }

        stdoutPipeline.append(String.format("You have swapped hands with %s.\n", targetPC.getPlayerName()));
        if (Objects.equals(PC.getHandCard().getName(), KingArnaud.NAME)) {
            PC.addToDiscardedCardsPile(PC.getHandCard());
            PC.setHandCard(PC.getTableCard());
            PC.setTableCard(null);
        }
        else {
            PC.addToDiscardedCardsPile(PC.getTableCard());
            PC.setTableCard(null);
        }

        KingArnaud.swapHands(PC, targetPC);
        targetPC.setMessageForPlayerNextTurn(
                "A King was played on you.\nYour card has been swapped with another player's card.");

        return ACard.RC_OK_HANDS_UPDATED;
    }

}
