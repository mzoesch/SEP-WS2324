package Cards;

public class HandmaidSusannah extends Card {

    public HandmaidSusannah() {
        super(
            "Handmaid Susannah",
            "Few would trust a mere Handmaid with a letter of importance. Fewer still understand "
                + "Susannah's cleverness, or her skilled ability at playing the foolish Handmaid. That "
                + "the Queen’s confidante and loyal servant escaped any attention after the Queen's "
                + "arrest is a testament to her clever mind.",
            "When you discard the Handmaid, you are immune to the effects of other players' cards until "
                + "the start of your next turn. If all players other than the player whose turn it is are "
                + "protected by the Handmaid, the player must choose him- or herself for a card's effects, "
                + "if possible.",
                4
        );

        return;
    }

    @Override
    public void PlayEffect() {
        System.out.printf("%s has been played by you.\n", this.name);
        return;
    }
}
