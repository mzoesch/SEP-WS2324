package App.Cards;

import App.PlayerController;

import java.util.Scanner;


public class PrincessAnnette extends Card {

    public PrincessAnnette() {
        super(
            "Princess Annette",
            "Hampered only by the naïveté of youth, Princess Annette is elegant, charming, and beautiful. "
                + "Obviously, you want the princess to carry your letter. However, she is self-conscious "
                + "about matters of the heart, and if confronted, will toss your letter "
                + "in the fire and deny looking at any correspondence.",
            "If you discard the Princess—no matter how or why—she has tossed your letter into the fire. "
                + "You are immediately knocked out of the round. If the Princess was discarded by a card "
                + "effect, any remaining effects of that card do not apply (you do not draw a card from the "
                + "Prince, for example). Effects tied to being knocked out the round still apply (eg. "
                + "Constable, Jester), however.",
                8
        );

        return;
    }

    /**
     * Princess Annette can be played at any given time. <p>
     * We do not need to check if the card is playable. <p>
     */
    @Override
    public int PlayEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Card pickedCardFromDeck,
            String MessageForPlayerWhenForced
    ) {
        if (bPlayedManually) {
            System.out.printf("%s has been played by you.\n", this.name);
            PC.SetIsKnockedOut(true, false);

            return 3;
        }

        if (PC.GetProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.GetPlayerName());
            return 1;
        }

        PC.SetIsKnockedOut(true, true);
        return 3;
    }
}
