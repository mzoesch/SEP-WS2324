package Cards;

public class PriestTomas extends Card {

    public PriestTomas() {
        super(
            "Priest Tomas",
            "Open, honest, and uplifting, Father Tomas always seeks out the opportunity to do good. "
                + "With the arrest of the Queen, he is often seen about the palace, acting as confessor, "
                + "counselor, and friend.",
            "When you discard the Priest, you can look at another player’s hand. Do not reveal the "
                + "hand to any other players",
                2
        );

        return;
    }

    @Override
    public void PlayEffect() {
        System.out.printf("%s has been played by you.\n", this.name);
        return;
    }
}
