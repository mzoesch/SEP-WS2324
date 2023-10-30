package appv2.core.view;

import appv2.core.View;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * <p>Controller for the Rules Screen.</p>
 */
public class RulesController {

    @FXML private Text prologtext01;
    @FXML private Text prologtext02;
    @FXML private Text prologtext03;
    @FXML private Text prologtext04;
    @FXML private Text prologtext05;
    @FXML private Text prologtext06;
    @FXML private Text prologtext07;

    @FXML private Text objectofthegametext01;
    @FXML private Text objectofthegametext02;
    @FXML private Text objectofthegametext03;

    @FXML private Text gamecardstext;

    @FXML private Text tokensofaffectiontext;

    @FXML private Text setuptext01;
    @FXML private Text setuptext02;

    @FXML private Text howtoplaytext;

    @FXML private Text takingaturntext;

    @FXML private Text outoftheroundtext;

    @FXML private Text choosingaplayertext;

    @FXML private Text endofaroundtext;

    @FXML private Text gamenedtext;

    @FXML private Text princess01;
    @FXML private Text princess02;
    @FXML private Text princess03;

    @FXML private Text countess01;
    @FXML private Text countess02;
    @FXML private Text countess03;

    @FXML private Text king01;
    @FXML private Text king02;
    @FXML private Text king03;

    @FXML private Text prince01;
    @FXML private Text prince02;
    @FXML private Text prince03;

    @FXML private Text handmaid01;
    @FXML private Text handmaid02;
    @FXML private Text handmaid03;

    @FXML private Text baron01;
    @FXML private Text baron02;
    @FXML private Text baron03;

    @FXML private Text priest01;
    @FXML private Text priest02;
    @FXML private Text priest03;

    @FXML private Text guard01;
    @FXML private Text guard02;
    @FXML private Text guard03;

    /**
     * <p>Destroys the rules screen and displays the Main Menu.</p>
     *
     * @param event The event that triggered this method.
     */
    public void onContinueBtn(ActionEvent event) {
        View.renderExistingScreen(MasterController.MAIN_MENU);
        return;
    }

    /**
     * <p>Sets the wrapping width of all the text elements to the width of the scroll pane.</p>
     *
     * @param width The width of the window.
     */
    private void setWrappingWidthToText(double width) {
        int DEFAULT_PADDING = 96;

        this.prologtext01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prologtext02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prologtext03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prologtext04.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prologtext05.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prologtext06.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prologtext07.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.objectofthegametext01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.objectofthegametext02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.objectofthegametext03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.gamecardstext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.tokensofaffectiontext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.setuptext01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.setuptext02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.howtoplaytext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.takingaturntext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.outoftheroundtext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.choosingaplayertext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.endofaroundtext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.gamenedtext.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.princess01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.princess02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.princess03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.countess01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.countess02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.countess03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.king01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.king02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.king03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.prince01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prince02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.prince03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.handmaid01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.handmaid02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.handmaid03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.baron01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.baron02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.baron03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.priest01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.priest02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.priest03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        this.guard01.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.guard02.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);
        this.guard03.setWrappingWidth(((Stage) View.getScene().getWindow()).getWidth() - DEFAULT_PADDING);

        return;
    }

    /**
     * <p>Initializes the rules screen with a event listener to
     * the window to update the text in the scroll pane.</p>
     */
    @FXML
    protected void initialize() {
        this.setWrappingWidthToText(((Stage) View.getScene().getWindow()).getWidth());
        ((Stage) View.getScene().getWindow()).widthProperty().addListener((obs, oldVal, newVal) -> {
            this.setWrappingWidthToText((double) newVal);
        });

        return;
    }

}
