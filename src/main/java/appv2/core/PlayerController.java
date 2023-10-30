package appv2.core;

import java.util.ArrayList;

import appv2.cards.ACard;


/**
 * <p>Represents the will of the human player. All input and output for one player is handled here.<br />
 * Also holds relevant information about the player, such as his name, his affection, his cards, etc.</p>
 */
public class PlayerController {

    /**
     * <p>The index of the player in the Game Mode Player Controller Array.</p>
     * @see appv2.core.GameMode#getPlayerControllerByID(int)
     */
    private final int playerID;
    private final String playerName;

    /**
     * <p>Number of affection tokens the player has.<br />
     * A specific number of affection tokens is required to win the game.</p>
     *
     * @see GameMode#getAmountOfTokensOfAffectionToWin()
     */
    private int affectionTokens;

    /**
     * <p>The card in the player's hand (maybe null).</p>
     */
    ACard handCard;
    /**
     * <p>The card that was just picked up from the deck this round (maybe null).</p>
     */
    ACard tableCard;
    /**
     * <p>The pile of discarded cards of the player (maybe empty).</p>
     */
    private final ArrayList<ACard> discardedCardsPile;

    /**
     * <p>Whether the player has already played a card this turn or not.</p>
     */
    private boolean bPlayedCard;
    /**
     * <p>Whether the player is currently playing a card or not.<br />
     * This is true if the player already played a card and
     * is currently choosing an other player for their card side effect.</p>
     */
    private boolean bIsPlaying;
    /**
     * <p>Whether the player is protected by the handmaid or not.</p>
     * @see appv2.cards.HandmaidSusannah
     */
    private boolean bIsProtected;
    /**
     * <p>Whether the player is knocked out of the round or not.</p>
     */
    private boolean bKnockedOut;

    /**
     * @deprecated We don't need this anymore.
     *             We can just use the messageForPlayerNextTurn variable and check whether it is empty or not.
     * @see #messageForPlayerNextTurn
     */
    private boolean bSignalPlayerNextTurn;
    /**
     * <p>Message to be displayed to the player at the start of his next turn.</p>
     */
    private String messageForPlayerNextTurn;

    public PlayerController(int playerID, String playerName) {
        super();

        this.playerID = playerID;
        this.playerName = playerName;

        this.affectionTokens = 0;
        this.discardedCardsPile = new ArrayList<ACard>();

        this.bPlayedCard = false;
        this.bIsPlaying = false;
        this.bIsProtected = false;
        this.bKnockedOut = false;

        return;
    }

    /**
     * <p>Clears players history from last round and lets them draw a new card.</p>
     */
    public void prepareForNextRound() {
        this.handCard = null;
        this.tableCard = null;
        this.discardedCardsPile.clear();

        this.bPlayedCard = false;
        this.bIsPlaying = false;

        this.bKnockedOut = false;
        this.bSignalPlayerNextTurn = false;
        this.messageForPlayerNextTurn = "";

        this.handCard = GameState.getActiveGameMode().drawCard();

        return;
    }

    /**
     * <p>Clears players history from last turn and lets them draw a new card.</p>
     */
    public void prepareForNextTurn() {
        this.bPlayedCard = false;
        this.bIsPlaying = false;

        if (this.bIsProtected)
            this.messageForPlayerNextTurn = "You are no longer protected.";
        this.bIsProtected = false;

        if (this.isKnockedOut()) {
            this.bPlayedCard = true;
            this.bIsPlaying = false;

            return;
        }

        this.tableCard = GameState.getActiveGameMode().drawCard();
        return;
    }

    /**
     * @param bHandCard Whether the player wants to play the card in his hand or the card on the table.
     * @param stdoutPipeline The pipeline to write the stdout of the card to.
     * @param stderrPipeline The pipeline to write the stderr of the card to.
     * @return The response state of the card played.
     * @throws RuntimeException If a return code is no recognized.
     * @see appv2.core.ECardResponse
     */
    public ECardResponse playCard(
            boolean bHandCard,
            StringBuilder stdoutPipeline,
            StringBuilder stderrPipeline
    ) throws RuntimeException {
        if (this.bPlayedCard || this.bIsPlaying)
            throw new RuntimeException("Player has already played a card this turn.");
        this.bIsPlaying = true;

        int RC = bHandCard
                ? this.handCard.playCard(this, true, null, stdoutPipeline, stderrPipeline)
                : this.tableCard.playCard(this, true, null, stdoutPipeline, stderrPipeline);

        if (RC == ACard.RC_OK) {
            if (bHandCard) {
                this.discardedCardsPile.add(this.handCard);
                this.handCard = this.tableCard;
            } else
                this.discardedCardsPile.add(this.tableCard);
            this.tableCard = null;

            this.bIsPlaying = false;
            this.bPlayedCard = true;

            return ECardResponse.RC_OK;
        }

        if (RC == ACard.RC_OK_PLAYER_KNOCKED_OUT) {
            this.bIsPlaying = false;
            this.bPlayedCard = true;

            return ECardResponse.RC_OK_KNOCKED_OUT;
        }

        if (RC == ACard.RC_ERR) {
            this.bIsPlaying = false;
            this.bPlayedCard = false;

            return ECardResponse.RC_ERR;
        }

        if (RC == ACard.RC_CHOOSE_ANY_PLAYER) {
            this.bIsPlaying = true;
            this.bPlayedCard = false;

            return ECardResponse.RC_CHOOSE_ANY_PLAYER;
        }

        if (RC == ACard.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED) {
            this.bIsPlaying = true;
            this.bPlayedCard = false;

            return ECardResponse.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED;
        }

        if (RC == ACard.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER) {
            this.bIsPlaying = true;
            this.bPlayedCard = false;

            return ECardResponse.RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER;
        }

        throw new RuntimeException("Unhandled return code from ACard.playCard().");
    }

    /**
     * <p>Increases the number of affection tokens the player has by one.</p>
     */
    public void increaseAffection() {
        this.affectionTokens++;
        return;
    }

    // region Getters and setters

    /**
     * @return The index of the player in the Game Mode Player Controller Array.
     * @see appv2.core.GameMode#getPlayerControllerByID(int)
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
     * @return The card in the player's hand (maybe null).
     */
    public ACard getHandCard() {
        return handCard;
    }

    /**
     * @return The card that was just picked up from the deck this round (maybe null).
     */
    public ACard getTableCard() {
        return tableCard;
    }

    /**
     * @return The pile of discarded cards of the player (maybe empty).
     */
    public ACard[] getDiscardedCardsPile() {
        return this.discardedCardsPile.toArray(new ACard[0]);
    }

    /**
     * @return Whether the player has already played a card in this turn or not.
     */
    public boolean hasPlayedCard() {
        return this.bPlayedCard;
    }

    /**
     * @return Whether the player is currently protected by the handmaid or not.
     * @see appv2.cards.HandmaidSusannah
     */
    public boolean isProtected() {
        return this.bIsProtected;
    }

    /**
     * @return Whether the player is knocked out of the round or not.
     */
    public boolean isKnockedOut() {
        return this.bKnockedOut;
    }

    /**
     * @return Gets the affection of the latest discarded card.
     */
    public int getAffectionOfLatestDiscardedCard() {
        return this.discardedCardsPile.get(this.discardedCardsPile.size() - 1).getAffection();
    }

    /**
     * @return Gets the sum of affection of all discarded cards.
     *         Used to determine the winner of the game in case of a tie.
     * @see GameMode#applyRoundWinBonusToPlayers()
     */
    public int getSumOfAffectionInDiscardPile() {
        return this.discardedCardsPile.stream()
                .mapToInt(ACard::getAffection)
                .sum();
    }

    /**
     * @return The number of affection tokens the player
     *         has (they are needed to win the game).
     */
    public int getAffectionTokens() {
        return this.affectionTokens;
    }

    /**
     * <p>Implements the default behavior of the player when he is knocked out of the round.<br />
     * This also includes discarding his hand and table card to his discarded cards pile.<p>
     * <p><b>Important:</b> A player should always be knocked out with this method.</p>
     *
     * @param bKnockedOut Whether the player is knocked out of the round or not.
     * @param bSignalPlayerNextTurn Whether the player should be signaled at the start of his next turn.
     * @param message The message to be displayed to the player at the start of his next turn.
     * @see #bSignalPlayerNextTurn
     */
    public void setKnockedOut(boolean bKnockedOut, boolean bSignalPlayerNextTurn, String message) {
        this.bKnockedOut = bKnockedOut;
        this.bSignalPlayerNextTurn = bSignalPlayerNextTurn;
        this.messageForPlayerNextTurn = message;

        // As written in the rules, the player must discard his hand faced-up when he is knocked out.
        // The effect of the card is not applied.
        if (bKnockedOut) {
            if (this.handCard != null) {
                this.discardedCardsPile.add(this.handCard);
                this.handCard = null;
            }

            if (this.tableCard != null) {
                this.discardedCardsPile.add(this.tableCard);
                this.tableCard = null;
            }

            return;
        }

        return;
    }

    /**
     * @return The message to be displayed to the player at the start of his next turn.
     */
    public String getMessageForPlayerNextTurn() {
        return this.messageForPlayerNextTurn;
    }

    /**
     * @param messageForPlayerNextTurn The message to be displayed to the player
     *                                 at the start of his next turn.
     */
    public void setMessageForPlayerNextTurn(String messageForPlayerNextTurn) {
        this.messageForPlayerNextTurn = messageForPlayerNextTurn;
        return;
    }

    /**
     * @return True, if the player has the Countess Wilhelmina in his hands.
     * @see appv2.cards.CountessWilhelmina
     */
    public boolean hasCountessWilhelmina() {
        return this.handCard != null && this.handCard instanceof appv2.cards.CountessWilhelmina
                || this.tableCard != null && this.tableCard instanceof appv2.cards.CountessWilhelmina;
    }

    /**
     * @param bPlayedCard Whether the player has already played a card in this turn or not.
     */
    public void setPlayedCard(boolean bPlayedCard) {
        this.bPlayedCard = bPlayedCard;
        return;
    }

    /**
     * @param bIsPlaying Whether the player is currently playing a card or not.
     * @see #bIsPlaying
     */
    public void setIsPlaying(boolean bIsPlaying) {
        this.bIsPlaying = bIsPlaying;
        return;
    }

    /**
     * @return Whether the player is currently playing a card or not.
     * @see #bIsPlaying
     */
    public boolean isPlaying() {
        return this.bIsPlaying;
    }

    /**
     * <p>Adds a card to the players discarded pile.</p>
     * <p><b>Warning:</b> You should always call the cards playEffect method beforehand.</p>
     *
     * @param card The card to add to the discarded cards pile.
     * @see appv2.cards.PrincessAnnette
     * @see appv2.cards.ACard#playCard(PlayerController, boolean, String, StringBuilder, StringBuilder)
     */
    public void addToDiscardedCardsPile(ACard card) {
        this.discardedCardsPile.add(card);
        return;
    }

    /**
     * @param card The card to set as the player's hand card.
     */
    public void setHandCard(ACard card) {
        this.handCard = card;
        return;
    }

    /**
     * @param card The card to set as the player's table card.
     */
    public void setTableCard(ACard card) {
        this.tableCard = card;
        return;
    }

    /**
     * @param bProtected Whether the player is currently protected by the handmaid or not.
     */
    public void setIsProtected(boolean bProtected) {
        this.bIsProtected = bProtected;
        return;
    }

    // endregion Getters and setters

}
