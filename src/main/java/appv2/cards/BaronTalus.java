package appv2.cards;


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

    @Override
    public int playCard() {
        return 0;
    }

}
