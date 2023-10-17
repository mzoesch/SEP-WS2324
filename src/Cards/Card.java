package Cards;

public abstract class Card {

    protected final String name;
    private final String backgroundStory;
    private final String effectDescription;

    private final int affection;

    public Card(String name, String backgroundStory, String effectDescription, int affection) {
        this.name = name;
        this.backgroundStory = backgroundStory;
        this.effectDescription = effectDescription;
        this.affection = affection;

        return;
    }

   public String GetName() {
        return this.name;
    }

    public String GetBackgroundStory() {
        return this.backgroundStory;
    }

    public String GetEffectDescription() {
        return this.effectDescription;
    }

    public int GetAffection() {
        return this.affection;
    }

    public String GetAsString() {
        return String.format("%s (Affection: %s)", this.name, this.affection);
    }

    public abstract void PlayEffect();

}
