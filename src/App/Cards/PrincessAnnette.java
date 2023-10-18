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

    @Override
    public int playEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Boolean bIsHandCard,
            String messageForPlayerWhenForced
    ) {
        if (bPlayedManually) {
            System.out.printf("%s has been played by you.\n", this.name);
            return Card.RC_OK_PLAYER_KNOCKED_OUT;
        }

        if (PC.getProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.getPlayerName());
            return Card.RC_ERR;
        }

        PC.setIsKnockedOut(true, true, "While you were away, someone discarded your Princess Annette Card.");
        return Card.RC_OK_PLAYER_KNOCKED_OUT;
    }
}
