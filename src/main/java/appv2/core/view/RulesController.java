package appv2.core.view;

import appv2.core.View;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class RulesController {

    @FXML Text prologtext01;
    @FXML Text prologtext02;
    @FXML Text prologtext03;
    @FXML Text prologtext04;
    @FXML Text prologtext05;
    @FXML Text prologtext06;
    @FXML Text prologtext07;

    @FXML Text objectofthegametext01;
    @FXML Text objectofthegametext02;
    @FXML Text objectofthegametext03;

    @FXML Text gamecardstext;

    @FXML Text tokensofaffectiontext;

    @FXML Text setuptext01;
    @FXML Text setuptext02;

    @FXML Text howtoplaytext;

    @FXML Text takingaturntext;

    @FXML Text outoftheroundtext;

    @FXML Text choosingaplayertext;

    @FXML Text endofaroundtext;

    @FXML Text gamenedtext;

    @FXML Text princess01;
    @FXML Text princess02;
    @FXML Text princess03;

    @FXML Text countess01;
    @FXML Text countess02;
    @FXML Text countess03;

    @FXML Text king01;
    @FXML Text king02;
    @FXML Text king03;

    @FXML Text prince01;
    @FXML Text prince02;
    @FXML Text prince03;

    @FXML Text handmaid01;
    @FXML Text handmaid02;
    @FXML Text handmaid03;

    @FXML Text baron01;
    @FXML Text baron02;
    @FXML Text baron03;

    @FXML Text priest01;
    @FXML Text priest02;
    @FXML Text priest03;

    @FXML Text guard01;
    @FXML Text guard02;
    @FXML Text guard03;

    public void onContinueBtn(ActionEvent event) {
        View.renderExistingScreen(MasterController.MAIN_MENU);
        return;
    }

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

    @FXML
    protected void initialize() {
        this.setWrappingWidthToText(((Stage) View.getScene().getWindow()).getWidth());
        ((Stage) View.getScene().getWindow()).widthProperty().addListener((obs, oldVal, newVal) -> {
            this.setWrappingWidthToText((double) newVal);
        });

        return;
    }

}
