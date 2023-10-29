package appv2.cards;


import appv2.core.PlayerController;
import appv2.core.GameState;

import java.util.Objects;


/**
 * <p>Guard Odette card.</p>
 * <p><b>Special Effect:</b> <br />
 * When discarded the player must guess the affection of another player's card in hand.
 * If they guess correctly, that player will be knocked out. If all players are
 * protected (e.g. by the Handmaid) the effect is being cancelled. </p>
 * <br />
 * @see ACard
 * @see #playCard(PlayerController, boolean, String, StringBuilder, StringBuilder) playCard
 */
public non-sealed class GuardOdette extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Guard Odette";
    private static final int CARD_AFFECTION = 1;

    /**
     * <p>Minimum value when guessing the affection of a hand.</p>
     * @deprecated {@link appv2.core.view.GameController}
     */
    private static final int MIN_AFFECTION_WHEN_GUESSING = 2;
    /**
     * <p>Maximum value when guessing the affection of a hand.</p>
     * @deprecated {@link appv2.core.view.GameController}
     */
    private static final int MAX_AFFECTION_WHEN_GUESSING = 8;

    /**
     * <p>Constructor.</p>
     */
    public GuardOdette() {
        super(
                GuardOdette.NAME,
                "Charged with seeing to the security of the royal family, Odette follows her orders with "
                        + "persistence and diligence... even though her mentor is said to have drowned while fleeing "
                        + "arrest for complicity in the Queen’s treason.",
                "When you discard the Guard, choose a player and name a number (other than 1). If that player "
                        + "has that number in their hand, that player is knocked out of the round. If all other players "
                        + "still in the round cannot be chosen (eg. due to Handmaid or Sycophant), this card is "
                        + "discarded without effect.",
                GuardOdette.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * When discarded the player must guess the affection of another player's card in hand
     * ({@link ACard#RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER}).
     * If they guess correctly, that player will be knocked out. If all players are
     * protected (e.g. by the Handmaid) the effect is being cancelled ({@link ACard#RC_OK}). </p>
     * <br />
     * {@inheritDoc}
     * @see ACard#RC_OK
     * @see ACard#RC_ERR
     * @see ACard#RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER
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
            stdoutPipeline.append("Choose a player to guess the hand of.\n");

            int validTargetPCs = 0;
            for (PlayerController targetPC : GameState.getActiveGameMode().getPlayerControllers()) {
                if (targetPC.isKnockedOut()) {
                    continue;
                }

                if (targetPC.equals(PC)) {
                    continue;
                }

                if (targetPC.isProtected()) {
                    continue;
                }

                validTargetPCs++;
                continue;
            }

            if (validTargetPCs == 0) {
                stdoutPipeline.append("There are no valid targets.\n");
                return ACard.RC_OK;
            }

            return ACard.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER;
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
     * the caller Player Controller will try to guess the affection ({@link ACard#RC_OK_HANDS_UPDATED}).</p>
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

        if (Objects.equals(PC.getHandCard().getName(), GuardOdette.NAME)) {
            PC.addToDiscardedCardsPile(PC.getHandCard());
            PC.setHandCard(PC.getTableCard());
            PC.setTableCard(null);
        }
        else {
            PC.addToDiscardedCardsPile(PC.getTableCard());
            PC.setTableCard(null);
        }

        if (targetPC.getHandCard().getAffection() == Integer.parseInt(args[0])) {
            targetPC.setKnockedOut(true, true, "A Guard was played on you.\nYou have been knocked out.");
            stdoutPipeline.append(String.format(
                    "You have guessed correctly. %s has been knocked out.\n",
                    targetPC.getPlayerName()
            ));
        }
        else {
            stdoutPipeline.append(String.format(
                    "You have guessed incorrectly. %s has not been knocked out.\n",
                    targetPC.getPlayerName()
            ));
        }
        return ACard.RC_OK_HANDS_UPDATED;
    }

}
