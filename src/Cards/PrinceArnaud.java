package Cards;

public class PrinceArnaud extends Card {

    public PrinceArnaud() {
        super(
            "Prince Arnaud",
            "As a social gady, Prince Arnaud was not as distressed over his mother’s arrest as one "
                + "would suppose. Since many women clamor for his attention, he hopes to help his sister "
                +" find the same banal happiness by playing matchmaker.",
            "When you discard Prince Arnaud, choose one player still in the round (including yourself). "
                + "That player discards his or her hand (but doesn't apply its effect, unless it is the "
                + "Princess, see page 8) and draws a new one. If the deck is empty and the player cannot "
                + "draw a card, that player draws the card that was removed at the start of the round. "
                + "If all other players are protected by the Handmaid, you must choose yourself.",
                5
        );

        return;
    }

    @Override
    public void PlayEffect() {
        System.out.printf("%s has been played by you.\n", this.name);
        return;
    }
}
