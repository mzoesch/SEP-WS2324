package appv2.core.view;

import javafx.scene.layout.Pane;


/**
 * <p> Holds useful information about a rendered screen.</p>
 */
public class GameScene {

    /**
     * <p>Unique identifier of this screen.</p>
     */
    private final String identifier;
    /**
     * <p>Fully rendered screen of this screen.</p>
     */
    private final Pane pane;
    /**
     * <p>Whether this screen should be destroyed after use.</p>
     */
    private final boolean killAfterUse;
    /**
     * <p>Unique identifier of the screen to fallback to if this screen is destroyed.</p>
     */
    private String fallbackIdentifier;

    /**
     * <p>Constructor.</p>
     *
     * @param identifier Unique identifier of this screen.
     * @param pane Fully rendered screen of this screen.
     * @param killAfterUse Whether this screen should be destroyed after use.
     * @param fallback Unique identifier of the screen to fall back to if this screen is destroyed.
     */
    public GameScene(String identifier, Pane pane, boolean killAfterUse, String fallback) {
        this.identifier = identifier;
        this.pane = pane;
        this.killAfterUse = killAfterUse;
        this.fallbackIdentifier = fallback;

        return;
    }

    // region Getters and Setters

    /**
     * @return Unique identifier of this object.
     * @see GameScene#identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @return Fully rendered screen of this object.
     * @see GameScene#pane
     */
    public Pane getPane() {
        return this.pane;
    }

    /**
     * @return Whether this screen should be destroyed after use.
     * @see GameScene#killAfterUse
     */
    public boolean killAfterUse() {
        return this.killAfterUse;
    }

    /**
     * <p>Sets the unique identifier of an already rendered screen as a fallback screen when this
     * screen is destroyed.</p>
     *
     * @param fallback Unique identifier of an already rendered screen.
     * @see GameScene#fallbackIdentifier
     */
    public void setFallback(String fallback) {
        this.fallbackIdentifier = fallback;
        return;
    }

    /**
     * <p>Gets the unique identifier of an already rendered screen as a fallback screen when this
     * screen is destroyed.</p>
     *
     * @return Unique identifier of an already rendered screen.
     * @see GameScene#fallbackIdentifier
     */
    public String getFallback() {
        return this.fallbackIdentifier;
    }

    // endregion Getters and Setters

}
