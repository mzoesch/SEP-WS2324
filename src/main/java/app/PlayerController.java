package app;

import java.util.Scanner;
import java.util.ArrayList;

import app.cards.*;


/**
 * <p>Represents the will of the human player. All input and output for one player is handled here.<br />
 * Also holds relevant information about the player, such as his name, his affection, his cards, etc.</p>
 */
public class PlayerController {

    private final int playerID;
    private final String playerName;

    private int affection;

    /**
     * <p>The card in the player's hand (maybe null).</p>
     */
    ACard cardInHand;
    /**
     * <p>The card that was just picked up from the deck this round (maybe null).</p>
     */
    ACard pickedCardFromDeck;
    /**
     * <p>The pile of discarded cards of the player (maybe empty).</p>
     */
    private ArrayList<ACard> discardedCards;

    private boolean bPlayedTurn;
    private boolean bKnockedOut;
    /**
     * <p>If True, the player will be notified about his knockout at the beginning of his next turn.</p>
     */
    private boolean bSignalPlayerNextTurnAboutKnockout;
    private String messageForPlayerWhenPlayEffectWasForced;

    private boolean bProtectedByHandmaid;

    /**
     * <p>Valid commands for the player if he plays a turn.</p>
     */
    private static final Command[] ValidCommands = new Command[] {
        new Command("help", "h", "Shows this help message."),
        new Command("playCard", "pc", "Discards the card just picked from the deck."),
        new Command("playHand", "ph", "Discards the card in your hand."),
        new Command("showHand", "sh", "Shows both cards - if available - in your hand."),
        new Command("showScore", "ss", "Shows the current score (affection) of all players."),
        new Command("showExaminationCards", "ex", "Shows all cards that are currently open for examination (only usefully in two-player games)."),
        new Command("showDiscarded", "dis", "Shows your discarded cards."),
        new Command("showAllDiscarded", "disa", "Shows all discarded cards of all players."),
        new Command("showCardDetails", "d", "Shows the effects of your cards."),
        new Command("showAllCardDetails", "da", "Shows the effects of all cards."),
        new Command("endTurn", "e", "Ends your turn.")
    };

    /**
     * <p>Creates a new player.</p>
     *
     * @param playerID The ID of the player.
     * @param playerName The name of the player.
     */
    public PlayerController(int playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;

        this.affection = 0;

        this.cardInHand = null;
        this.discardedCards = null;

        this.bPlayedTurn = false;
        this.bKnockedOut = false;
        this.bSignalPlayerNextTurnAboutKnockout = false;
        this.messageForPlayerWhenPlayEffectWasForced = null;

        this.bProtectedByHandmaid = false;

        return;
    }

    // region Turn helper functions

    /**
     * <p>Prepares the Standard Output Pipeline with
     * necessary information in a turn for the player.</p>
     *
     * @param scanner Scanner object to use.
     */
    private void prepareStdForTurn(Scanner scanner) {
        App.flushStdOut();

        System.out.printf("%d) %s's turn.\n", this.playerID, this.playerName);
        System.out.print("Press enter to play your turn: ");
        scanner.nextLine();

        if (this.bProtectedByHandmaid)
            System.out.println("WARNING: You are no longer protected by the Handmaid. Watch out!");

        if (this.bKnockedOut && this.bSignalPlayerNextTurnAboutKnockout) {
            this.bSignalPlayerNextTurnAboutKnockout = false;

            if (this.messageForPlayerWhenPlayEffectWasForced != null
                    && !this.messageForPlayerWhenPlayEffectWasForced.isEmpty()) {
                System.out.print(this.messageForPlayerWhenPlayEffectWasForced);
                this.messageForPlayerWhenPlayEffectWasForced = null;
            }
            System.out.print("You were knocked out last turn.\n");
            System.out.print("You are not allowed to play this round anymore.\n");
            System.out.print("You may still type \"\\showAllDiscarded\" to see who knocked you out.\n");
            System.out.print("Press enter to continue: ");
            scanner.nextLine();

            return;
        }

        if (this.bKnockedOut)
            return;

        if (this.messageForPlayerWhenPlayEffectWasForced != null
                && !this.messageForPlayerWhenPlayEffectWasForced.isEmpty()) {
            System.out.print(this.messageForPlayerWhenPlayEffectWasForced);
            this.messageForPlayerWhenPlayEffectWasForced = null;
            System.out.print("Press enter to continue: ");
            scanner.nextLine();
        }

        System.out.printf("\n%s players are remaining:\n", GameInstance.getActiveGameMode().getRemainingPlayerCount());
        App.printArray_V2(GameInstance.getActiveGameMode().getRemainingPlayerNames(), ", ", "");

        System.out.println();
        System.out.printf("Your new card is %s.\n", this.pickedCardFromDeck.getAsString());
        System.out.printf("Your hand card is %s.\n", this.cardInHand.getAsString());
        System.out.printf("There are %d cards left in the deck and ", GameInstance.getActiveGameMode().getCurrentDeckSize());
        if (GameInstance.getActiveGameMode().getHiddenCard() == null)
            System.out.print("there is no hidden card.\n");
        else
            System.out.print("a hidden card is still available.\n");

        if (GameInstance.getActiveGameMode().getExaminingCards().length > 0) {
            System.out.println();
            System.out.printf("There are %d cards open for examination:\n",
                    GameInstance.getActiveGameMode().getExaminingCards().length);
            App.printArray_V2(GameInstance.getActiveGameMode().getExaminingCardsAsString(), ", ", "");
        }

        System.out.println();
        if (this.discardedCards.isEmpty())
            System.out.print("You currently don't have any card on your discarded pile.");
        else {
            System.out.printf("You have %d discarded cards:\n", this.discardedCards.size());
            App.printArray_V2(this.getDiscardedCardsAsString(), " -> ", "");
        }

        System.out.println();
        return;
    }

    private boolean isPlayerBlockedToPlayACard() {
        if (this.bKnockedOut) {
            System.out.print("You have been knocked out of this round.\n");
            System.out.print("You can only end your turn now.\n");

            return true;
        }

        if (this.bPlayedTurn) {
            System.out.print("You have already played a card this turn.\n");
            System.out.print("Type \"\\endTurn\" to end your turn.\n");

            return true;
        }

        return false;
    }

    private void reactToCardPlayed(int RC) {
        if (RC == ACard.RC_ERR)
            return;

        if (RC == ACard.RC_OK_HANDS_UPDATED) {
            this.bPlayedTurn = true;
            return;
        }

        if (RC == ACard.RC_OK_PLAYER_KNOCKED_OUT) {
            this.bPlayedTurn = true;
            this.setIsKnockedOut(true, false, null);

            System.out.println("You have been knocked out of this round.");
            System.out.println("You may only end your turn now.");

            return;
        }

        return;
    }

    /**
     * <p>Executes a command that is valid in a player's turn.</p>
     *
     * @param scanner Scanner object to inherit to other objects.
     * @param command Command to execute.
     * @return True if the player has ended his turn, false otherwise.
     */
    private boolean executeCommand(Scanner scanner, String command) {
        switch (command) {
            case "h":
            case "help": {
                for (Command cdm : PlayerController.ValidCommands)
                    System.out.printf("%s\n", cdm.toString());

                return  false;
            }

            case "pc":
            case "playCard": {
                if (this.isPlayerBlockedToPlayACard())
                    return false;

                int RC = this.pickedCardFromDeck.playEffect(scanner, this, true, false, null);

                if (RC == ACard.RC_OK) {
                    this.discardedCards.add(this.pickedCardFromDeck);
                    this.pickedCardFromDeck = null;

                    this.bPlayedTurn = true;
                    return false;
                }
                this.reactToCardPlayed(RC);

                return false;
            }

            case "ph":
            case "playHand": {
                if (this.isPlayerBlockedToPlayACard())
                    return false;

                int RC = this.cardInHand.playEffect(scanner, this, true, true, null);

                if (RC == ACard.RC_OK) {
                    this.discardedCards.add(this.cardInHand);
                    this.cardInHand = this.pickedCardFromDeck;
                    this.pickedCardFromDeck = null;

                    this.bPlayedTurn = true;
                    return false;
                }

                this.reactToCardPlayed(RC);
                return false;
            }

            case "sh":
            case "showHand": {
                System.out.printf("Card in hand   : %s.\n", this.cardInHand.getAsString());
                if (this.pickedCardFromDeck != null)
                    System.out.printf("Card from deck : %s.\n", this.pickedCardFromDeck.getAsString());
                else
                    System.out.print("Card from deck : NONE.\n");

                return false;
            }

            case "ss":
            case "showScore": {
                App.printArray_V2(GameInstance.getActiveGameMode().getAffectionOfAllPlayerAsString(), ", ", "");
                return false;
            }

            case "ex":
            case "showExaminationCards": {
                if (GameInstance.getActiveGameMode().getPlayerCount()
                        != GameMode.AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS) {
                    System.out.printf("This command is only available in a %d player game.\n",
                            GameMode.AMOUNT_OF_PLAYER_REQUIRED_FOR_EXAMINING_CARDS);
                    return false;
                }

                System.out.printf("There are %d cards open for examination:\n%s\n",
                        GameInstance.getActiveGameMode().getExaminingCards().length,
                        GameInstance.getActiveGameMode().getExaminingCardsAsOneString()
                );

                return false;
            }

            case "dis":
            case "showDiscarded": {
                if (this.discardedCards.isEmpty()) {
                    System.out.print("You currently don't have any card on your discarded pile.\n");
                    return false;
                }

                System.out.printf("You have %d discarded cards:\n", this.discardedCards.size());
                App.printArray_V2(this.getDiscardedCardsAsString(), " -> ", "");

                return false;
            }

            case "disa":
            case "showAllDiscarded": {
                for (int i = 0; i < GameInstance.getActiveGameMode().getPlayerCount(); i++) {
                    System.out.printf("%s's discarded cards: ",
                            GameInstance.getActiveGameMode().getPlayerControllerByID(i).getPlayerName());
                    App.printArray_V2(
                            GameInstance.getActiveGameMode().getPlayerControllerByID(i).getDiscardedCardsAsString(), " -> ", "");

                    continue;
                }

                return false;
            }

            case "d":
            case "showCardDetails": {
                System.out.printf("%s", this.cardInHand.getAsDetailedString());
                if (this.pickedCardFromDeck != null)
                    System.out.printf("\n%s", this.pickedCardFromDeck.getAsDetailedString());

                return false;
            }

            case "da":
            case "showAllCardDetails": {
                System.out.print("Card details:\n\n");
                System.out.printf("%s", new PrincessAnnette().getAsDetailedString());
                System.out.printf("\n%s", new CountessWilhelmina().getAsDetailedString());
                System.out.printf("\n%s", new KingArnaud().getAsDetailedString());
                System.out.printf("\n%s", new PrinceArnaud().getAsDetailedString());
                System.out.printf("\n%s", new HandmaidSusannah().getAsDetailedString());
                System.out.printf("\n%s", new BaronTalus().getAsDetailedString());
                System.out.printf("\n%s", new PriestTomas().getAsDetailedString());
                System.out.printf("\n%s", new GuardOdette().getAsDetailedString());

                return false;
            }

            case "e":
            case "endTurn": {
                if (this.bKnockedOut)
                    return true;

                if (this.bPlayedTurn)
                    return true;

                System.out.print("You have to play a card before you can end your turn.\n");
                return false;
            }

            default: {
                System.out.printf("Something went wrong with your command \"%s\".\n", command);
                return false;
            }
        }
    }

    // endregion Turn helper functions

    /**
     * <p>Loop for a player's turn.</p>
     *
     * @param scanner Scanner object to inherit to other objects.
     * @param pickedCardFromDeck The card that was just picked up from the deck
     *                           this round (is null if the player is knocked out).
     */
    public void playTurn(Scanner scanner, ACard pickedCardFromDeck) {
        this.pickedCardFromDeck = pickedCardFromDeck;
        this.prepareStdForTurn(scanner);

        this.bPlayedTurn = false;
        this.bProtectedByHandmaid = false;

        while (true) {
            String choice = App.waitForCommand_V2(scanner, PlayerController.ValidCommands);

            boolean bEndTurn = this.executeCommand(scanner, choice);
            if (bEndTurn)
                break;

            continue;
        }

        return;
    }

    // region Getters and Setters

    /**
     * @return The ID of the player.
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**
     * @return The name of the player.
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * @return The affection of the player.
     */
    public int getAffection() {
        return this.affection;
    }

    /**
     * Increases the affection of the player by one.
     */
    public void increaseAffection() {
        this.affection++;
        return;
    }

    /**
     * @return The card in the player's hand (maybe null).
     */
    public ACard getCardInHand() {
        return this.cardInHand;
    }

    /**
     * @param cardInHand The card in the player's hand (setting to null is safe).
     */
    public void setCardInHand(ACard cardInHand) {
        this.cardInHand = cardInHand;
        return;
    }

    /**
     * @return The card that was just picked up from the deck this round (maybe null).
     */
    public ACard getPickedCardFromDeck() {
        return this.pickedCardFromDeck;
    }

    /**
     * @param pickedCardFromDeck The card that was just picked up from the
     *                           deck this round (setting to null is safe).
     */
    public void setPickedCardFromDeck(ACard pickedCardFromDeck) {
        this.pickedCardFromDeck = pickedCardFromDeck;
        return;
    }

    /**
     * @return True if the player is knocked out, false otherwise.
     */
    public boolean isKnockedOut() {
        return this.bKnockedOut;
    }

    /**
     * <p>If bIsKnockedOut is True, we will automatically discard all the player's
     * cards as it is stated in the rules.</p>
     *
     * @param bIsKnockedOut True if the player is knocked out, false otherwise.
     * @param bSignalPlayerNextTurn True, if the player should be notified about his knockout
     *                              at the beginning of his next turn, false otherwise.
     * @param messageForPlayer The message that should be displayed to the player at the beginning
     *                         of his next turn (requires bSignalPlayerNextTurn to be True).
     */
    public void setIsKnockedOut(boolean bIsKnockedOut, boolean bSignalPlayerNextTurn, String messageForPlayer) {
        this.bKnockedOut = bIsKnockedOut;
        this.bSignalPlayerNextTurnAboutKnockout = bSignalPlayerNextTurn;
        this.messageForPlayerWhenPlayEffectWasForced = messageForPlayer;

        // As written in the rules, the player must discard his hand faced-up when he is knocked out.
        // The effect of the card is not applied.
        if (bKnockedOut) {
            if (this.cardInHand != null) {
                this.discardedCards.add(this.cardInHand);
                this.cardInHand = null;
            }

            if (this.pickedCardFromDeck != null) {
                this.discardedCards.add(this.pickedCardFromDeck);
                this.pickedCardFromDeck = null;
            }

            return;
        }

        return;
    }

    /**
     * @return If the player should be notified about his knockout at the beginning of his next turn.
     */
    public boolean isSignalPlayerNextTurnAboutKnockout() {
        return this.bSignalPlayerNextTurnAboutKnockout;
    }

    /**
     * @param messageForPlayerWhenPlayEffectWasForced The message that should be displayed to the player
     *                                                at the beginning of his next turn if his card changed
     *                                                (requires bSignalPlayerNextTurn to be True).
     * @see #setIsKnockedOut(boolean bIsKnockedOut, boolean bSignalPlayerNextTurn, String messageForPlayer)
     */
    public void setMessageForPlayerWhenPlayEffectWasForced(String messageForPlayerWhenPlayEffectWasForced) {
        this.messageForPlayerWhenPlayEffectWasForced = messageForPlayerWhenPlayEffectWasForced;
        return;
    }

    /**
     * @return True, if the player is untouchable by any other effect, false otherwise.
     */
    public boolean isProtectedByHandmaid() {
        return this.bProtectedByHandmaid;
    }

    /**
     * @param bProtectedByHandmaid True, if the player should be untouchable by any other effect, false otherwise.
     */
    public void setProtectedByHandmaid(boolean bProtectedByHandmaid) {
        this.bProtectedByHandmaid = bProtectedByHandmaid;
        return;
    }

    // endregion Getters and Setters

    // region Utility functions

    private String[] getDiscardedCardsAsString() {
        String[] discardedCardsAsString = new String[this.discardedCards.size()];
        for (int i = 0; i < this.discardedCards.size(); i++) {
            discardedCardsAsString[i] = this.discardedCards.get(i).getAsString();
            continue;
        }

        return discardedCardsAsString;
    }

    /**
     * @return The sum of all affection values of the cards in the player's discarded pile.
     * {@link GameMode#applyRoundWinBonusToPlayers()}
     */
    public int getSumOfAffectionInDiscardPile() {
        int sum = 0;
        for (ACard card : this.discardedCards)
            sum += card.getAffection();

        return sum;
    }

    /**
     * @return The top card of the player's discarded pile.
     */
    public ACard getLatestCardOfDiscardedPile() {
        if (this.discardedCards.isEmpty())
            throw new RuntimeException("Something went horrilby wrong.");
        return this.discardedCards.get(this.discardedCards.size() - 1);
    }

    /**
     * @return The affection of the top card of the player's discarded pile.
     */
    public int getAffectionOfLatestDiscardedCard() {
        if (this.discardedCards.isEmpty())
            return 0;
        return this.discardedCards.get(this.discardedCards.size() - 1).getAffection();
    }

    /**
     * <p>Resets all the player's variables for a new round.</p>
     */
    public void resetForNewRound() {
        this.cardInHand = null;
        this.pickedCardFromDeck = null;
        this.discardedCards = new ArrayList<ACard>();

        this.bPlayedTurn = false;
        this.bKnockedOut = false;
        this.bSignalPlayerNextTurnAboutKnockout = false;
        this.messageForPlayerWhenPlayEffectWasForced = null;

        this.bProtectedByHandmaid = false;

        return;
    }

    /**
     * <p>Return the current game mode. Must be valid throughout the entire application lifetime.</p>
     * @return The current game mode.
     */
    public GameMode getActiveGameMode() {
        return GameInstance.getActiveGameMode();
    }

    /**
     * @param card The card to add to the player's discarded pile.
     */
    public void addCardToDiscardedCards(ACard card) {
        this.discardedCards.add(card);
        return;
    }

    /**
     * <p>Overwrites the card in the player's hand with a new card from the deck.</p>
     * @see GameMode#drawCard()
     */
    public void overwriteCurrentHandCardWithNewDeckCard() {
        this.cardInHand = GameInstance.getActiveGameMode().drawCard();
        return;
    }

    /**
     * @return True if the player has the Countess Wilhelmina in his hand or
     * as his picked card from the deck, false otherwise.
     */
    public boolean hasCountessWilhelminaInHand() {
        return this.cardInHand instanceof CountessWilhelmina || this.pickedCardFromDeck instanceof CountessWilhelmina;
    }

    // endregion Utility functions

}
