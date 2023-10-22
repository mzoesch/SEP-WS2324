package appv2.cards;

public abstract class ACard {

    protected final String name;
    private final String backgroundStory;
    private final String effectDescription;

    private final int affection;

    public ACard(String name, String backgroundStory, String effectDescription, int affection) {
        this.name = name;
        this.backgroundStory = backgroundStory;
        this.effectDescription = effectDescription;
        this.affection = affection;

        return;
    }

    public String getName() {
        return this.name;
    }

    public String getBackgroundStory() {
        return this.backgroundStory;
    }

    public String getEffectDescription() {
        return this.effectDescription;
    }

    public int getAffection() {
        return this.affection;
    }

    public String getAsString() {
        return String.format("%s [%s]", this.name, this.affection);
    }

    public abstract int playCard();

}
