package app.cards;

import app.App;
import app.PlayerController;

import java.util.Arrays;
import java.util.Scanner;


public class BaronTalus extends Card {

    public static final String NAME = "Baron Talus";
    private static final int CARD_AFFECTION = 3;

    public BaronTalus() {
        super(
            BaronTalus.NAME,
            "The scion of an esteemed house that has long been a close ally of the royal family, "
                + "Baron Talus has a quiet and gentle demeanor that conceals a man used to being obeyed. "
                + "His suggestions are often treated as if they came from the King himself.",
            "When you discard the Baron, choose another player still in the round. You and that player "
                + "secretly compare your hands. The player with the lower number is knocked out of the round. "
                + "In case of a tie, nothing happens.",
                BaronTalus.CARD_AFFECTION
        );

        return;
    }

    @Override
    public int playEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Boolean bIsHandCard, String messageForPlayerWhenForced
    ) {
        if (bPlayedManually) {
            System.out.printf("%s has been played by you.\n", this.name);

            PlayerController[] targetablePCs = Card.getAllRemainingPlayersTargetableByCardEffects(PC);
            if (targetablePCs.length == 0) {
                System.out.print(
                        "There are no other players to compare hands with or they are protected by Handmaids.\n");
                System.out.print("The effect is cancelled.\n");
                return Card.RC_OK;
            }

            String choice = App.waitForInputStringWithValidation_V2(
                    scanner,
                    Arrays.stream(targetablePCs).map(PlayerController::getPlayerName).toArray(String[]::new),
                    "Choose a player to compare hands with "
                        + "(the player with the lower card will be knocked out of the round)"
                );

            PlayerController targetPC = PC.getActiveGameMode().getPlayerControllerByName(choice);
            Card ownCardToCompare = null;
            if (bIsHandCard)
                ownCardToCompare = PC.getPickedCardFromDeck();
            else
                ownCardToCompare = PC.getCardInHand();

            System.out.printf("You have chosen to compare hands with %s.\n", choice);
            System.out.printf("Your hand: %s | Targets hand: %s\n",
                    ownCardToCompare.getAsString(), targetPC.getCardInHand().getAsString());

            if (ownCardToCompare.getAffection() == targetPC.getCardInHand().getAffection()) {
                System.out.print("You have the same card affection. Nothing happens.\n");
                return Card.RC_OK;
            }

            if (ownCardToCompare.getAffection() > targetPC.getCardInHand().getAffection()) {
                System.out.printf("You have won the comparison. %s is knocked out of the round.\n", choice);
                targetPC.setIsKnockedOut(true, true, "A Baron was played. You lost the comparison.\n");
                return Card.RC_OK;
            }

            if (ownCardToCompare.getAffection() < targetPC.getCardInHand().getAffection()) {
                System.out.print("You have lost the comparison.\n");
                return Card.RC_OK_PLAYER_KNOCKED_OUT;
            }

            return Card.RC_ERR;
        }

        if (PC.getProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.getPlayerName());
            return Card.RC_ERR;
        }

        PC.setMessageForPlayerWhenPlayEffectWasForced(messageForPlayerWhenForced);
        return Card.RC_OK;
    }
}
