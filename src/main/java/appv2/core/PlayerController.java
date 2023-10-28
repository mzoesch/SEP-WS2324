package appv2.core;

import java.util.ArrayList;

import appv2.cards.ACard;


public class PlayerController {

    private final int playerID;
    private final String playerName;

    private int affectionTokens;

    ACard handCard;
    ACard tableCard;
    private final ArrayList<ACard> discardedCardsPile;

    private boolean bPlayedCard;
    private boolean bIsPlaying;
    private boolean bIsProtected;
    private boolean bKnockedOut;

    private boolean bSignalPlayerNextTurn;
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

        this.handCard = GameState.getActiveGameMode().drawCard();

        return;
    }

    public void prepareForNextTurn() {
        this.bPlayedCard = false;
        this.bIsPlaying = false;

        if (this.bIsProtected)
            this.messageForPlayerNextTurn = "You are no longer protected.";
        this.bIsProtected = false;

        this.tableCard = GameState.getActiveGameMode().drawCard();
        return;
    }

    public ECardResponse playCard(boolean bHandCard, StringBuilder stdoutPipeline, StringBuilder stderrPipeline) {
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

        throw new RuntimeException("Unhandled return code from ACard.playCard().");
    }

    public void increaseAffection() {
        this.affectionTokens++;
        return;
    }

    // region Getters and setters

    public int getPlayerID() {
        return this.playerID;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public ACard getHandCard() {
        return handCard;
    }

    public ACard getTableCard() {
        return tableCard;
    }

    public ACard[] getDiscardedCardsPile() {
        return this.discardedCardsPile.toArray(new ACard[0]);
    }

    public boolean hasPlayedCard() {
        return this.bPlayedCard;
    }

    public boolean isProtected() {
        return this.bIsProtected;
    }

    public boolean isKnockedOut() {
        return this.bKnockedOut;
    }

    public int getAffectionOfLatestDiscardedCard() {
        return this.discardedCardsPile.get(this.discardedCardsPile.size() - 1).getAffection();
    }

    public int getSumOfAffectionInDiscardPile() {
        return this.discardedCardsPile.stream()
                .mapToInt(ACard::getAffection)
                .sum();
    }

    public int getAffectionTokens() {
        return this.affectionTokens;
    }

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

    public String getMessageForPlayerNextTurn() {
        return this.messageForPlayerNextTurn;
    }

    public void setMessageForPlayerNextTurn(String messageForPlayerNextTurn) {
        this.messageForPlayerNextTurn = messageForPlayerNextTurn;
        return;
    }

    public boolean hasCountessWilhelmina() {
        return this.handCard != null && this.handCard instanceof appv2.cards.CountessWilhelmina
                || this.tableCard != null && this.tableCard instanceof appv2.cards.CountessWilhelmina;
    }

    public void setPlayedCard(boolean bPlayedCard) {
        this.bPlayedCard = bPlayedCard;
        return;
    }

    public void setIsPlaying(boolean bIsPlaying) {
        this.bIsPlaying = bIsPlaying;
        return;
    }

    public boolean isPlaying() {
        return this.bIsPlaying;
    }

    public void addToDiscardedCardsPile(ACard card) {
        this.discardedCardsPile.add(card);
        return;
    }

    public void setHandCard(ACard card) {
        this.handCard = card;
        return;
    }

    public void setTableCard(ACard card) {
        this.tableCard = card;
        return;
    }

    public void setIsProtected(boolean bProtected) {
        this.bIsProtected = bProtected;
        return;
    }

    // endregion Getters and setters

}
