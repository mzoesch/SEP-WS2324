package App.Cards;

import App.PlayerController;
import App.App;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


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
    public int PlayEffect(
            Scanner scanner,
            PlayerController PC,
            boolean bPlayedManually,
            Card pickedCardFromDeck,
            String MessageForPlayerWhenForced
    ) {
        if (bPlayedManually) {
            System.out.printf("%s has been played by you.\n", this.name);

            ArrayList<PlayerController> remainingPCs = PC.GetActiveGameMode().GetRemainingPlayers();
            ArrayList<PlayerController> targetablePCs = new ArrayList<PlayerController>();
            for (PlayerController remainingPC : remainingPCs) {
                if (Objects.equals(PC.GetPlayerName(), remainingPC.GetPlayerName()))
                    continue;
                if (remainingPC.GetProtectedByHandmaid())
                    continue;

                targetablePCs.add(remainingPC);
                continue;
            }

            if (targetablePCs.isEmpty()) {
                System.out.print(
                    "There are no other players to compare hands with or they are protected by Handmaids.\n");
                System.out.print("The effect is cancelled.\n");
                return 0;
            }

            String choice =
                    App.WaitForInput(
                            scanner,
                            targetablePCs.stream().map(PlayerController::GetPlayerName).toArray(String[]::new)
                    );

            PlayerController targetPC = PC.GetActiveGameMode().GetPlayerControllerFromName(choice);
            Card targetHandCard = targetPC.GetCardInHand();
            Card ownHandCardToCompare = null;
            if (Objects.equals(this.GetName(), PC.GetCardInHand().GetName()))
                ownHandCardToCompare = pickedCardFromDeck;
            else
                ownHandCardToCompare = PC.GetCardInHand();

            System.out.printf("You have chosen to compare hands with %s.\n", choice);
            System.out.printf("Your hand: %s | Targets hand: %s\n",
                    ownHandCardToCompare.GetAsString(), targetHandCard.GetAsString());

            if (ownHandCardToCompare.GetAffection() == targetHandCard.GetAffection()) {
                System.out.print("You have the same card affection. Nothing happens.\n");
                return 0;
            }

            if (ownHandCardToCompare.GetAffection() > targetHandCard.GetAffection()) {
                System.out.printf("You have won the comparison. %s is knocked out of the round.\n", choice);
                targetPC.SetIsKnockedOut(true, true);
                targetPC.SetMessageForPlayerWhenPlayEffectWasForced(
                        "A Baron was played. You lost the comparison.\n");
                return 0;
            }

            if (ownHandCardToCompare.GetAffection() < targetHandCard.GetAffection()) {
                System.out.print("You have lost the comparison. You are knocked out of the round.\n");
                PC.SetIsKnockedOut(true, false);

                PC.AddToDiscardedCards(this);
                PC.SetCardInHand(ownHandCardToCompare);

                return 0;
            }

            return 0;
        }

        if (PC.GetProtectedByHandmaid()) {
            System.out.printf("%s is protected by the Handmaid.\n", PC.GetPlayerName());
            return 1;
        }

        PC.SetMessageForPlayerWhenPlayEffectWasForced(MessageForPlayerWhenForced);
        return 0;
    }
}
