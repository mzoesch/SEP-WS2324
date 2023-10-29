package appv2.cards;

import appv2.core.PlayerController;
import appv2.core.GameState;

import java.util.Objects;


/**
 * <p>Priest Tomas card.</p>
 * <p><b>Special Effect:</b> <br />
 * When discarded the player can look at another player's hand. If all players are
 * protected (e.g. by the Handmaid) the effect is being cancelled. </p>
 * <br />
 * @see ACard
 * @see #playCard(PlayerController, boolean, String, StringBuilder, StringBuilder) playCard
 */
public non-sealed class PriestTomas extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Priest Tomas";
    private static final int CARD_AFFECTION = 2;

    /**
     * <p>Constructor.</p>
     */
    public PriestTomas() {
        super(
                PriestTomas.NAME,
                "Open, honest, and uplifting, Father Tomas always seeks out the opportunity to do good. "
                    + "With the arrest of the Queen, he is often seen about the palace, acting as confessor, "
                    + "counselor, and friend.",
                "When you discard the Priest, you can look at another player’s hand. Do not reveal the "
                    + "hand to any other players",
                PriestTomas.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * When discarded the player can look at another player's hand
     * ({@link ACard#RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED}). If all players are
     * protected (e.g. by the Handmaid) the effect is being cancelled ({@link ACard#RC_OK}). </p>
     * <br />
     * {@inheritDoc}
     * @see ACard#RC_OK
     * @see ACard#RC_ERR
     * @see ACard#RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED
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
            stdoutPipeline.append("Choose a player to view the hand of.\n");

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

            return ACard.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED;
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
     * the caller Player Controller will see the targets hands ({@link ACard#RC_OK_HANDS_UPDATED}).</p>
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

        if (Objects.equals(PC.getHandCard().getName(), PriestTomas.NAME)) {
            PC.addToDiscardedCardsPile(PC.getHandCard());
            PC.setHandCard(PC.getTableCard());
            PC.setTableCard(null);
        }
        else {
            PC.addToDiscardedCardsPile(PC.getTableCard());
            PC.setTableCard(null);
        }

        stdoutPipeline.append(String.format(
           "The card of %s is %s.\n",
           targetPC.getPlayerName(),
           targetPC.getHandCard().getAsString()
        ));
        return ACard.RC_OK_HANDS_UPDATED;
    }

}
