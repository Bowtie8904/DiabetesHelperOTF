package otf.gui.screens;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import bt.gui.fx.core.annot.FxmlElement;
import bt.gui.fx.core.annot.handl.FxHandler;
import bt.gui.fx.core.annot.handl.chang.type.FxTextMustMatch;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnAction;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseEntered;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseExited;
import bt.gui.fx.core.annot.setup.FxTextApply;
import bt.gui.fx.util.ButtonHandling;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import otf.gui.components.PercentageSizedTableColumn;
import otf.model.DataModel;
import otf.model.text.TextDefinition;
import otf.model.text.Texts;
import otf.obj.BloodSugarValueEntity;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.NewBloodSugarValue;

/**
 * @author &#8904
 *
 */
public class AddBloodSugarScreen extends TabBase
{
    @FxmlElement
    @FxHandler(type = FxOnAction.class, method = "saveBloodSugar", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    @FxTextApply(textId = TextDefinition.SAVE)
    private JFXButton saveButton;

    @FxmlElement
    private JFXDatePicker datePicker;

    @FxmlElement
    private JFXTimePicker timePicker;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d{1,4}")
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "^[1-9]\\d*")
    private TextField bzTextField;

    @FxmlElement
    private TableView<BloodSugarValueEntity> table;

    @FxmlElement
    private BorderPane tablePane;

    /**
     * @see otf.gui.screens.TabBase#getTabName()
     */
    @Override
    public String getTabName()
    {
        return Texts.get().get(TextDefinition.NEW_BLOODSUGAR).toString();
    }

    public void saveBloodSugar()
    {
        var bz = new BloodSugarValueEntity();
        bz.setBloodSugar(Integer.parseInt(this.bzTextField.getText()));
        bz.setTimestamp(this.datePicker.getValue().toEpochSecond(this.timePicker.getValue(), OffsetDateTime.now().getOffset()) * 1000);
        DataModel.get().addBloodSugarValue(bz);

        this.datePicker.setValue(LocalDate.now());
        this.timePicker.setValue(LocalTime.now());
    }

    private void setupTable()
    {
        this.tablePane.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (AddBloodSugarScreen.this.table.maxWidthProperty().isBound())
                {
                    AddBloodSugarScreen.this.table.maxWidthProperty().unbind();
                }

                AddBloodSugarScreen.this.table.maxWidthProperty().bind(AddBloodSugarScreen.this.tablePane.widthProperty().multiply(0.3));
            }
        });

        PercentageSizedTableColumn timestampCol = new PercentageSizedTableColumn();
        timestampCol.setText(Texts.get().get(TextDefinition.TIME).toString());
        timestampCol.setPercentageWidth(50);

        timestampCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                var timestamp = new Timestamp(t.getValue().getTimestamp());
                return new ReadOnlyStringWrapper(timestamp.toString());
            }
        });

        PercentageSizedTableColumn<BloodSugarValueEntity, String> bzCol = new PercentageSizedTableColumn();
        bzCol.setText(Texts.get().get(TextDefinition.BLOODSUGAR).toString());
        bzCol.setPercentageWidth(50);

        bzCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getBloodSugar() + "");
            }
        });

        this.table.setPlaceholder(new Label(Texts.get().get(TextDefinition.NO_VALUES_FOUND).toString()));
        this.table.getColumns().addAll(timestampCol, bzCol);
    }

    /**
     * @see bt.gui.fx.core.FxScreen#prepareScreen()
     */
    @Override
    protected void prepareScreen()
    {
        this.bzTextField.setFont(Font.font("Courier", 30));
        setupTable();

        MessageDispatcher.get().subscribeTo(NewBloodSugarValue.class, ent ->
        {
            this.table.getItems().add(0, ent.getBz());
        });
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

    /**
     * @see otf.gui.screens.TabBase#onTabSelect()
     */
    @Override
    public void onTabSelect()
    {
        this.datePicker.setValue(LocalDate.now());
        this.timePicker.setValue(LocalTime.now());
        this.bzTextField.setText("120");
        this.table.getItems().clear();
    }

    /**
     * @see otf.gui.screens.TabBase#onTabDeselect()
     */
    @Override
    public void onTabDeselect()
    {
    }
}