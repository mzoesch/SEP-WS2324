package Cards;

public class KingArnaud extends Card{

    public KingArnaud() {
        super(
            "King Arnaud IV",
            "The undisputed ruler of Tempest... for the moment. Because of his role in the arrest "
                + "of Queen Marianna, he does not rate as highly with Princess Annette as a father "
                + "should. He hopes to work himself back into her graces.",
            "When you discard King Arnaud IV, trade the card in your hand with the card held by another "
                + "player of your choice. You cannot trade with a player who is out of the round.",
                6
        );

        return;
    }

    @Override
    public void PlayEffect() {
        System.out.printf("%s has been played by you.\n", this.name);
        return;
    }
}
