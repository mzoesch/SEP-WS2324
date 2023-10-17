package Cards;

public class GuardOdette extends Card {

    public GuardOdette() {
        super(
                "Guard Odette",
                "Charged with seeing to the security of the royal family, Odette follows her orders with "
                + "persistence and diligence... even though her mentor is said to have drowned while fleeing "
                + "arrest for complicity in the Queen’s treason.",
                "When you discard the Guard, choose a player and name a number (other than 1). If that player "
                + "has that number in their hand, that player is knocked out of the round. If all other players "
                + "still in the round cannot be chosen (eg. due to Handmaid or Sycophant), this card is "
                + "discarded without effect.",
                1
        );

        return;
    }

    @Override
    public void PlayEffect() {
        System.out.printf("%s has been played by you.\n", this.name);
        return;
    }
}
