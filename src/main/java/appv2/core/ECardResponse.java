package appv2.core;


/**
 * <p>Represents the possible responses to a card being played.</p>
 */
public enum ECardResponse {
    /**
     * <p>Card was played successfully. The caller will need to figure
     * out how to update the affected players' hands accordingly.</p>
     */
    RC_OK,
    /**
     * <p>Card was not played successfully. The caller will need to revert
     * changed variables accordingly.</p>
     */
    RC_ERR,
    /**
     * <p>Card was played successfully. The player who played the card is
     * knocked out of the round. The caller won't need to do anything else.</p>
     */
    RC_OK_KNOCKED_OUT,
    /**
     * <p>Card was played successfully. The caller will have to call the callback
     * method on this card with a valid player name.</p>
     */
    RC_CHOOSE_ANY_PLAYER,
    /**
     * <p>Card was played successfully. The caller will have to call the callback
     * method on this card with a valid player name that is not themself.</p>
     */
    RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED,
    /**
     * <p>Card was played successfully. The caller will have to call the callback
     * method on this card with a valid player name and a valid integer.</p>
     */
    RC_CHOOSE_ANY_PLAYER_SELF_EXCLUDED_WITH_INTEGER,
}
