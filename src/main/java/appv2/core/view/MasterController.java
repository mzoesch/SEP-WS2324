package appv2.core.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;


// TODO: Add class for screens. Add ability to auto remove screens when they are no longer needed.
public class MasterController {

    public static final String WIN_TITLE = "Love Letter @mzoesch";

    public static final int PREF_HEIGHT = 1_280;
    public static final int PREF_WIDTH = 720;

    public static final String MAIN_MENU = "main";
    public static final String RULES = "rules";

    private final Scene master;
    private final HashMap<String, Pane> screens;

    public MasterController(Scene master) {
        this.master = master;
        this.screens = new HashMap<String, Pane>();

        return;
    }

    public void addScreen(String identifier, Pane pane) {
        this.screens.put(identifier, pane);
        return;
    }

    public void removeScreen(String identifier) {
        this.screens.remove(identifier);
        return;
    }

    public void activate(String identifier) {
        this.master.setRoot(this.screens.get(identifier));
        return;
    }

}
