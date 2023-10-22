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
    private boolean bProtected;
    private boolean bKnockedOut;

    public PlayerController(int playerID, String playerName) {
        super();

        this.playerID = playerID;
        this.playerName = playerName;

        this.affectionTokens = 0;
        this.discardedCardsPile = new ArrayList<ACard>();

        this.bPlayedCard = false;
        this.bProtected = false;
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

        this.handCard = GameState.getActiveGameMode().drawCard();

        return;
    }

    public void prepareForNextTurn() {
        this.bPlayedCard = false;

        this.tableCard = GameState.getActiveGameMode().drawCard();
        return;
    }

    public ECardResponse playCard(boolean bHandCard) {
        if (this.bPlayedCard)
            throw new RuntimeException("Player has already played a card this turn.");
        this.bPlayedCard = true;

        if (bHandCard) {
            this.discardedCardsPile.add(this.handCard);
            this.handCard = this.tableCard;
        } else {
            this.discardedCardsPile.add(this.tableCard);
        }
        this.tableCard = null;

        return ECardResponse.OK;
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
        return this.bProtected;
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

    // endregion Getters and setters

}
