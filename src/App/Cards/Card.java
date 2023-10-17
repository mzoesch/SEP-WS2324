package App.Cards;

import App.PlayerController;

import java.util.Scanner;


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

    public String GetAsDetailedString() {
        return String.format("%s\n%s\n%s\n",
                this.GetAsString(), this.backgroundStory, this.effectDescription);
    }

    /**
     * Return codes: <p>
     *     0: The card was played successfully. <p>
     *     1: The card was not played successfully. <p>
     *     2: The card was played successfully and players hands have been already updated. <p>
     *     3: The card was played successfully and the player has been knocked out. <p>
     */
    public abstract int PlayEffect(Scanner scanner, PlayerController PC, boolean bPlayedManually, Card pickedCardFromDeck, String MessageForPlayerWhenForced);

}
