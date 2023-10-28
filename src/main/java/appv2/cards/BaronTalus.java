package appv2.cards;

import appv2.core.PlayerController;

import java.util.Objects;


/**
 * <p>Baron Talus card.</p>
 * @see ACard
 */
public class BaronTalus extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Baron Talus";
    private static final int CARD_AFFECTION = 3;

    /**
     * <p>Constructor.</p>
     */
    public BaronTalus() {
        super(
                BaronTalus.NAME,
                "The scion of an esteemed house that has long been a close ally of the royal family, "
                        + "Baron Talus has a quiet and gentle demeanor that conceals a man used to being obeyed. "
                        + "His suggestions are often treated as if they came from the King himself.",
                "When you discard the Baron, choose another player still in the round. You and that player "
                        + "secretly compare your hands. The player with the lower number is knocked out of the round. "
                        + "In case of a tie, nothing happens.",
                BaronTalus.CARD_AFFECTION
        );

        return;
    }

    /**
     * <p><b>Special Effect:</b> <br />
     * When discarded the player can compare his hand with another player. The player with the
     * lower affection of his card is knocked out of the round. If all players are
     * protected (e.g. by the Handmaid) the effect is being cancelled.</p>
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
            stdoutPipeline.append("Choose a player to compare hands with.\n");
            return ACard.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED;
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
            String messageForPlayerWhenForced
    ) {

        if (targetPC.isProtected()) {
            stderrPipeline.append("This player is protected.\n");
            return ACard.RC_ERR;
        }

        if (Objects.equals(PC.getHandCard().getName(), BaronTalus.NAME)) {
            PC.addToDiscardedCardsPile(PC.getHandCard());
            PC.setHandCard(PC.getTableCard());
            PC.setTableCard(null);
        }
        else {
            PC.addToDiscardedCardsPile(PC.getTableCard());
            PC.setTableCard(null);
        }

        if (PC.getHandCard().getAffection() == targetPC.getHandCard().getAffection()) {
            stdoutPipeline.append("You have the same card affection. Nothing happens.\n");
            return ACard.RC_OK_HANDS_UPDATED;
        }

        if (PC.getHandCard().getAffection() < targetPC.getHandCard().getAffection()) {
            stdoutPipeline.append("You have lost the comparison.\n");
            PC.setKnockedOut(true, false, null);
            PC.setIsPlaying(false);
            PC.setPlayedCard(true);
            return ACard.RC_OK_PLAYER_KNOCKED_OUT;
        }

        if (PC.getHandCard().getAffection() > targetPC.getHandCard().getAffection()) {
            stdoutPipeline.append(String.format(
                    "You have won the comparison. %s is knocked out of the round.\n",
                    targetPC.getPlayerName()
            ));
            targetPC.setKnockedOut(true, false, null);

            PC.setIsPlaying(false);
            PC.setPlayedCard(true);

            return ACard.RC_OK_HANDS_UPDATED;
        }

        throw new RuntimeException("Beyond the impossible.");
    }

}
