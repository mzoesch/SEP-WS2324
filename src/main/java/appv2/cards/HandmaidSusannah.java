package appv2.cards;


import appv2.core.PlayerController;

/**
 * <p>Handmaid Susannah card.</p>
 * @see ACard
 */
public class HandmaidSusannah extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Handmaid Susannah";
    private static final int CARD_AFFECTION = 4;

    /**
     * <p>Constructor.</p>
     */
    public HandmaidSusannah() {
        super(
                HandmaidSusannah.NAME,
                "Few would trust a mere Handmaid with a letter of importance. Fewer still understand "
                        + "Susannah's cleverness, or her skilled ability at playing the foolish Handmaid. That "
                        + "the Queen’s confidante and loyal servant escaped any attention after the Queen's "
                        + "arrest is a testament to her clever mind.",
                "When you discard the Handmaid, you are immune to the effects of other players' cards until "
                        + "the start of your next turn. If all players other than the player whose turn it is are "
                        + "protected by the Handmaid, the player must choose him- or herself for a card's effects, "
                        + "if possible.",
                HandmaidSusannah.CARD_AFFECTION
        );

        return;
    }

    @Override
    public int playCard(
            PlayerController PC,
            boolean bPlayedManually,
            String messageForPlayerWhenForced,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline
    ) {
        if (bPlayedManually) {
            PC.setIsProtected(true);
            stdoutPipeline.append("You are protected from other players card effects until the start of your next turn.\n");
            return ACard.RC_OK;
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
        throw new RuntimeException("HandmaidSusannah.callback() should never be called.");
    }

}
