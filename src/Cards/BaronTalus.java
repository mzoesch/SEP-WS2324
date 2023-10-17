package Cards;

public class BaronTalus extends Card {

    public BaronTalus() {
        super(
            "Baron Talus",
            "The scion of an esteemed house that has long been a close ally of the royal family, "
                + "Baron Talus has a quiet and gentle demeanor that conceals a man used to being obeyed. "
                + "His suggestions are often treated as if they came from the King himself.",
            "When you discard the Baron, choose another player still in the round. You and that player "
                + "secretly compare your hands. The player with the lower number is knocked out of the round. "
                + "In case of a tie, nothing happens.",
                3
        );

        return;
    }

    @Override
    public void PlayEffect() {
        System.out.printf("%s has been played by you.\n", this.name);
        return;
    }
}
