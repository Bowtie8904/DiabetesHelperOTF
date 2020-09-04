package otf.gui.screens;

import java.time.LocalTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;

import bt.gui.fx.core.annot.FxmlElement;
import bt.gui.fx.core.annot.handl.FxHandler;
import bt.gui.fx.core.annot.handl.chang.type.FxTextMustMatch;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnAction;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseEntered;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseExited;
import bt.gui.fx.util.ButtonHandling;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import otf.model.DataModel;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoaded;

/**
 * @author &#8904
 *
 */
public class ConfigScreen extends TabBase
{
    @FxmlElement
    private JFXTimePicker timePicker1;

    @FxmlElement
    private JFXTimePicker timePicker2;

    @FxmlElement
    private JFXTimePicker timePicker3;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.\\d*")
    private TextField factorTf1;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.\\d*")
    private TextField factorTf2;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.\\d*")
    private TextField factorTf3;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*")
    private TextField correctionTf;

    @FxmlElement
    @FxHandler(type = FxOnAction.class, method = "saveConfig", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    private JFXButton saveButton;

    private boolean modelLoaded;

    public void saveConfig()
    {

    }

    public void loadConfig()
    {
        if (this.modelLoaded)
        {
            var factors = DataModel.get().getBolusFactors();

            this.factorTf1.setText(factors[0].getFactor() + "");
            this.factorTf2.setText(factors[1].getFactor() + "");
            this.factorTf3.setText(factors[2].getFactor() + "");

            this.timePicker1.setValue(LocalTime.ofSecondOfDay(factors[0].getStartTime() / 1000));
            this.timePicker2.setValue(LocalTime.ofSecondOfDay(factors[1].getStartTime() / 1000));
            this.timePicker3.setValue(LocalTime.ofSecondOfDay(factors[2].getStartTime() / 1000));
        }
    }

    /**
     * @see otf.gui.screens.TabBase#getTabName()
     */
    @Override
    public String getTabName()
    {
        return "Konfiguration";
    }

    /**
     * @see otf.gui.screens.TabBase#onTabSelect()
     */
    @Override
    public void onTabSelect()
    {
        loadConfig();
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
        }));
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
    }
}