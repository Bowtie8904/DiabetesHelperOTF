package otf.gui.screens;

import java.time.LocalTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;

import bt.gui.fx.core.annot.FxmlElement;
import bt.gui.fx.core.annot.handl.FxHandler;
import bt.gui.fx.core.annot.handl.chang.type.FxObjectChange;
import bt.gui.fx.core.annot.handl.chang.type.FxStringChange;
import bt.gui.fx.core.annot.handl.chang.type.FxTextMustMatch;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnAction;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseEntered;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseExited;
import bt.gui.fx.core.annot.setup.FxTextApply;
import bt.gui.fx.util.ButtonHandling;
import bt.remote.socket.evnt.PingUpdate;
import bt.utils.StringUtils;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import otf.model.ClientDataModel;
import otf.model.text.TextDefinition;
import otf.model.text.Texts;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoaded;

/**
 * @author &#8904
 *
 */
public class ConfigScreen extends TabBase
{
    @FxmlElement
    @FxHandler(type = FxObjectChange.class, property = "valueProperty", method = "enableButton", withParameters = false)
    private JFXTimePicker timePicker1;

    @FxmlElement
    @FxHandler(type = FxObjectChange.class, property = "valueProperty", method = "enableButton", withParameters = false)
    private JFXTimePicker timePicker2;

    @FxmlElement
    @FxHandler(type = FxObjectChange.class, property = "valueProperty", method = "enableButton", withParameters = false)
    private JFXTimePicker timePicker3;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.\\d*")
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "enableButton", withParameters = false)
    private TextField factorTf1;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.\\d*")
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "enableButton", withParameters = false)
    private TextField factorTf2;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.\\d*")
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "enableButton", withParameters = false)
    private TextField factorTf3;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*")
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "enableButton", withParameters = false)
    private TextField correctionTf;

    @FxmlElement
    @FxHandler(type = FxOnAction.class, method = "saveConfig", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    @FxTextApply(textId = TextDefinition.SAVE)
    private JFXButton saveButton;

    @FxmlElement
    private Label successLabel;

    @FxmlElement
    @FxTextApply(textId = TextDefinition.LABEL_MORNING)
    private Label morningLabel;

    @FxmlElement
    @FxTextApply(textId = TextDefinition.LABEL_BEGINNING)
    private Label morningStartLabel;

    @FxmlElement
    @FxTextApply(textId = TextDefinition.LABEL_NOON)
    private Label noonLabel;

    @FxmlElement
    @FxTextApply(textId = TextDefinition.LABEL_BEGINNING)
    private Label noonStartLabel;

    @FxmlElement
    @FxTextApply(textId = TextDefinition.LABEL_EVENING)
    private Label eveningLabel;

    @FxmlElement
    @FxTextApply(textId = TextDefinition.LABEL_BEGINNING)
    private Label eveningStartLabel;

    @FxmlElement
    @FxTextApply(textId = TextDefinition.CORRECTION)
    private Label correctionLabel;

    @FxmlElement
    private Label pingLabel;

    private boolean modelLoaded;

    public void saveConfig()
    {
        this.saveButton.setDisable(true);
        var factors = ClientDataModel.get().getBolusFactors();

        factors[0].setFactor(Double.parseDouble(this.factorTf1.getText()));
        factors[0].setStartTime(this.timePicker1.getValue().toSecondOfDay() * 1000);
        ClientDataModel.get().updateBolusFactor(factors[0]);

        factors[1].setFactor(Double.parseDouble(this.factorTf2.getText()));
        factors[1].setStartTime(this.timePicker2.getValue().toSecondOfDay() * 1000);
        ClientDataModel.get().updateBolusFactor(factors[1]);

        factors[2].setFactor(Double.parseDouble(this.factorTf3.getText()));
        factors[2].setStartTime(this.timePicker3.getValue().toSecondOfDay() * 1000);
        ClientDataModel.get().updateBolusFactor(factors[2]);

        ClientDataModel.get().updateCorrectionUnits(Integer.parseInt(this.correctionTf.getText()));

        this.successLabel.setText(Texts.get().get(TextDefinition.SAVE_SUCCESS).toString());
    }

    public void loadConfig()
    {
        if (this.modelLoaded)
        {
            var factors = ClientDataModel.get().getBolusFactors();

            this.factorTf1.setText(factors[0].getFactor() + "");
            this.factorTf2.setText(factors[1].getFactor() + "");
            this.factorTf3.setText(factors[2].getFactor() + "");

            this.timePicker1.setValue(LocalTime.ofSecondOfDay(factors[0].getStartTime() / 1000));
            this.timePicker2.setValue(LocalTime.ofSecondOfDay(factors[1].getStartTime() / 1000));
            this.timePicker3.setValue(LocalTime.ofSecondOfDay(factors[2].getStartTime() / 1000));

            this.correctionTf.setText(ClientDataModel.get().getCorrectionUnits() + "");
        }
    }

    private void enableButton()
    {
        this.saveButton.setDisable(false);
    }

    /**
     * @see otf.gui.screens.TabBase#getTabName()
     */
    @Override
    public String getTabName()
    {
        return Texts.get().get(TextDefinition.CONFIGURATION).toString();
    }

    /**
     * @see otf.gui.screens.TabBase#onTabSelect()
     */
    @Override
    public void onTabSelect()
    {
        loadConfig();
        this.successLabel.setText("");
        this.saveButton.setDisable(true);
    }

    /**
     * @see otf.gui.screens.TabBase#onTabDeselect()
     */
    @Override
    public void onTabDeselect()
    {
    }

    /**
     * @see bt.gui.fx.core.FxScreen#prepareScreen()
     */
    @Override
    protected void prepareScreen()
    {
        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> Platform.runLater(() ->
        {
            this.modelLoaded = true;
            loadConfig();
            this.saveButton.setDisable(false);
        }));

        this.successLabel.setTextFill(Color.web("#2d9c33"));
        this.saveButton.setDisable(false);
    }

    /**
     * @see bt.gui.fx.core.FxScreen#prepareStage(javafx.stage.Stage)
     */
    @Override
    protected void prepareStage(Stage stage)
    {
    }

    /**
     * @see bt.gui.fx.core.FxScreen#prepareScene(javafx.scene.Scene)
     */
    @Override
    protected void prepareScene(Scene scene)
    {
        MessageDispatcher.get().subscribeTo(PingUpdate.class, e ->
        {
            if (this.pingLabel != null)
            {
                Platform.runLater(() -> this.pingLabel.setText("Ping: " + StringUtils.leftPad(e.getPing() + "", 5, " ") + " ms"));
            }
        });
    }
}