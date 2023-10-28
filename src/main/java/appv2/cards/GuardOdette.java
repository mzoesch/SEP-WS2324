package appv2.cards;


import appv2.core.PlayerController;

/**
 * <p>Guard Odette card.</p>
 * @see ACard
 */
public class GuardOdette extends ACard {

    /**
     * <p>Name of the card.</p>
     */
    public static final String NAME = "Guard Odette";
    private static final int CARD_AFFECTION = 1;

    /**
     * <p>Minimum value when guessing the affection of a hand.</p>
     */
    private static final int MIN_AFFECTION_WHEN_GUESSING = 2;
    /**
     * <p>Maximum value when guessing the affection of a hand.</p>
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

    @Override
    public int playCard(PlayerController PC, boolean bPlayedManually, String messageForPlayerWhenForced, StringBuilder stdoutPipeline, StringBuilder stderrPipeline) {
        return 0;
    }

    @Override
    public int callback(PlayerController PC, PlayerController targetPC, StringBuilder stdoutPipeline, StringBuilder stderrPipeline, String messageForPlayerWhenForced) {
        return 0;
    }

}
