package otf.gui.screens;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.jfoenix.controls.JFXButton;

import bt.gui.fx.core.annot.FxmlElement;
import bt.gui.fx.core.annot.handl.FxHandler;
import bt.gui.fx.core.annot.handl.chang.type.FxStringChange;
import bt.gui.fx.core.annot.handl.chang.type.FxTextMustMatch;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnAction;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseEntered;
import bt.gui.fx.core.annot.handl.evnt.type.FxOnMouseExited;
import bt.gui.fx.util.ButtonHandling;
import bt.utils.string.StringUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import otf.gui.components.PercentageSizedTableColumn;
import otf.model.DataModel;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;
import otf.obj.msg.DeletedBloodSugarValue;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoadStarted;
import otf.obj.msg.ModelLoaded;
import otf.obj.msg.NewBloodSugarValue;
/**
 * @author &#8904
 *
 */
public class AddBolusScreen extends TabBase
{
    @FxmlElement
    private TableView<BloodSugarValueEntity> table;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*")
    private TextField bzTf;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.{0,1}\\d*")
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "calculateBolus", withParameters = false)
    private TextField beTf;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.\\d*")
    private TextField factorTf;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.{0,1}\\d*")
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "calculateTotalBolus", withParameters = false)
    private TextField bolusTf;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.{0,1}\\d*")
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "calculateTotalBolus", withParameters = false)
    private TextField correctionTf;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*\\.{0,1}\\d*")
    private TextField totalBolusTf;

    @FxmlElement
    @FxHandler(type = FxOnAction.class, method = "save", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    private JFXButton saveButton;

    @FxmlElement
    private BorderPane tablePane;

    private ToggleGroup group = new ToggleGroup();

    private Map<BloodSugarValueEntity, RadioButton> buttonMap;

    private BloodSugarValueEntity selectedEntity;

    public AddBolusScreen()
    {
        this.buttonMap = new HashMap<>();
    }

    private void addRadioButton(BloodSugarValueEntity entity, RadioButton button)
    {
        button.setToggleGroup(this.group);
        button.setOnAction((e) ->
        {
            selectEntity(entity);
        });

        this.buttonMap.put(entity, button);

        if (entity.equals(this.selectedEntity))
        {
            button.setSelected(true);
        }
    }

    private void selectEntity(BloodSugarValueEntity entity)
    {
        this.selectedEntity = entity;

        this.beTf.setDisable(false);
        this.bolusTf.setDisable(false);
        this.correctionTf.setDisable(false);
        this.saveButton.setDisable(false);

        this.bzTf.setText(entity.getBloodSugar() + "");
        this.factorTf.setText(DataModel.get().getCurrentBolusFactor().getFactor() + "");

        this.bolusTf.setText("0");
        this.totalBolusTf.setText("0");

        calculateBolus();
        calculateCorrection();
    }

    private void calculateCorrection()
    {
        double correction = 0.0;

        if (this.selectedEntity.getBloodSugar() >= 200)
        {
            int correctionUnits = DataModel.get().getCorretionUnits();
            int amount = 0;

            int corrected = this.selectedEntity.getBloodSugar();

            while (corrected > (120 + correctionUnits / 3.0))
            {
                amount ++ ;
                corrected -= correctionUnits / 2.0;
            }

            correction = amount / 2.0;
        }

        this.correctionTf.setText(correction + "");

        calculateTotalBolus();
    }

    private void calculateBolus()
    {
        double be = Double.parseDouble(StringUtils.leftPad(this.beTf.getText(), 1, "0"));
        double factor = Double.parseDouble(StringUtils.leftPad(this.factorTf.getText(), 1, "0"));

        this.bolusTf.setText((be * factor) + "");

        calculateTotalBolus();
    }

    private void calculateTotalBolus()
    {
        double bolus = Double.parseDouble(StringUtils.leftPad(this.bolusTf.getText(), 1, "0"));
        double correction = Double.parseDouble(StringUtils.leftPad(this.correctionTf.getText(), 1, "0"));

        this.totalBolusTf.setText((bolus + correction) + "");
    }

    private void setupTable()
    {
        this.tablePane.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (AddBolusScreen.this.table.maxWidthProperty().isBound())
                {
                    AddBolusScreen.this.table.maxWidthProperty().unbind();
                }

                AddBolusScreen.this.table.maxWidthProperty().bind(AddBolusScreen.this.tablePane.widthProperty().multiply(0.4));
            }
        });

        PercentageSizedTableColumn<BloodSugarValueEntity, RadioButton> selectColumn = new PercentageSizedTableColumn();
        selectColumn.setText("Auswahl");
        selectColumn.setPercentageWidth(20);

        selectColumn.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, RadioButton>, ObservableValue<RadioButton>>()
        {
            @Override
            public ObservableValue<RadioButton> call(CellDataFeatures<BloodSugarValueEntity, RadioButton> t)
            {
                var button = new RadioButton();
                addRadioButton(t.getValue(), button);
                return new ReadOnlyObjectWrapper(button);
            }
        });

        PercentageSizedTableColumn timestampCol = new PercentageSizedTableColumn();
        timestampCol.setText("Zeit");
        timestampCol.setPercentageWidth(40);

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
        bzCol.setText("Blutzucker");
        bzCol.setPercentageWidth(40);

        bzCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getBloodSugar() + "");
            }
        });

        this.table.setPlaceholder(new Label("Keine Werte gefunden"));
        this.table.getColumns().addAll(selectColumn, timestampCol, bzCol);
    }

    public void save()
    {
        var bolus = new BolusEntity();
        bolus.setBe(Double.parseDouble(StringUtils.leftPad(this.beTf.getText(), 1, "0")));
        bolus.setBolusUnits(Double.parseDouble(StringUtils.leftPad(this.bolusTf.getText(), 1, "0")));
        bolus.setCorrectionUnits(Double.parseDouble(StringUtils.leftPad(this.correctionTf.getText(), 1, "0")));
        bolus.setFactor(Double.parseDouble(StringUtils.leftPad(this.factorTf.getText(), 1, "0")));

        DataModel.get().addBolus(bolus);
        DataModel.get().connectBloodSugarBolus(this.selectedEntity, bolus);
    }

    /**
     * @see otf.gui.screens.TabBase#getTabName()
     */
    @Override
    public String getTabName()
    {
        return "Neuer Bolus";
    }

    /**
     * @see otf.gui.screens.TabBase#onTabSelect()
     */
    @Override
    public void onTabSelect()
    {
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
        setupTable();

        MessageDispatcher.get().subscribeTo(NewBloodSugarValue.class, ent -> addEntry(ent.getBz()));
        MessageDispatcher.get().subscribeTo(DeletedBloodSugarValue.class, ent -> removeEntry(ent.getBz()));
        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> refreshTableData());

        MessageDispatcher.get().subscribeTo(ModelLoadStarted.class, e -> Platform.runLater(() -> this.table.setPlaceholder(new Label("Lade Werte..."))));
        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> Platform.runLater(() -> this.table.setPlaceholder(new Label("Keine Werte gefunden"))));

        this.beTf.setDisable(true);
        this.bolusTf.setDisable(true);
        this.correctionTf.setDisable(true);
        this.saveButton.setDisable(true);

        this.beTf.setText("0");
    }

    private void refreshTableData()
    {
        this.table.getItems().setAll(DataModel.get().getBloodSugarValues());
    }

    private void removeEntry(BloodSugarValueEntity entry)
    {
        this.table.getItems().removeIf(ent ->
        {
            boolean result = false;

            if (ent.getId() != null && entry.getId() != null)
            {
                result = ent.getId().longValue() == entry.getId().longValue();
            }

            return result;
        });
    }

    private void addEntry(BloodSugarValueEntity entry)
    {
        this.table.getItems().add(entry);
        Collections.sort(this.table.getItems());
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