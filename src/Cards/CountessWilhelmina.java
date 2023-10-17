package Cards;

public class CountessWilhelmina extends Card {

    public CountessWilhelmina() {
        super(
            "Countess Wilhelmina",
            "Always on the prowl for a handsome man or juicy gossip, Wilhelmina’s age and noble blood make her "
                + "one of Princess Annette's friends. While she has great influence over the Princess, she makes "
                + "herself scarce whenever the King or Prince are around.",
            "Unlike other cards, which take eect when discarded, the text on the Countess applies while she "
                + "is in your hand. In fact, the only time it doesn't apply is when you discard her.\nIf you ever "
                + "have the Countess and either the King or Prince in your hand, you must discard the Countess. "
                + "You do not have to reveal the other card in your hand. Of course, you can also discard the "
                + "Countess even if you do not have a royal family member in your hand. The Countess likes to play "
                + "mind games...",
                7
        );

        return;
    }

    @Override
    public void PlayEffect() {
        System.out.printf("%s has been played by you.\n", this.name);
        return;
    }
}
