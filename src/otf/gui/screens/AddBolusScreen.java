package otf.gui.screens;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
import otf.obj.FoodEntity;
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
    private TableView<BloodSugarValueEntity> bzTable;

    @FxmlElement
    private TableView<FoodEntity> allFoodTable;

    @FxmlElement
    private TableView<FoodEntity> selectedFoodTable;

    @FxmlElement
    private TextField foodNameTf;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*")
    private TextField weightTf;

    @FxmlElement
    @FxHandler(type = FxTextMustMatch.class, property = "textProperty", value = "\\d*")
    private TextField foodBeTf;

    @FxmlElement
    @FxHandler(type = FxStringChange.class, property = "textProperty", method = "searchFood", withParameters = false)
    private TextField searchTf;

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
    @FxHandler(type = FxOnAction.class, method = "saveBolus", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    private JFXButton saveBolusButton;

    @FxmlElement
    @FxHandler(type = FxOnAction.class, method = "saveFood", withParameters = false)
    @FxHandler(type = FxOnMouseEntered.class, methodClass = ButtonHandling.class, method = "onMouseEnter", withParameters = false, passField = true)
    @FxHandler(type = FxOnMouseExited.class, methodClass = ButtonHandling.class, method = "onMouseExit", withParameters = false, passField = true)
    private JFXButton saveFoodButton;

    @FxmlElement
    private BorderPane tablePane;

    private ToggleGroup group = new ToggleGroup();

    private Map<BloodSugarValueEntity, RadioButton> radioButtonMap;
    private Map<FoodEntity, Integer> foodAmount;

    private BloodSugarValueEntity selectedEntity;

    public AddBolusScreen()
    {
        this.radioButtonMap = new HashMap<>();
        this.foodAmount = new HashMap<>();
    }

    private void searchFood()
    {
        if (this.searchTf.getText().trim().isEmpty())
        {
            this.allFoodTable.getItems().setAll(DataModel.get().getFoodEntities());
            Collections.sort(this.allFoodTable.getItems(), Comparator.comparing(FoodEntity::getName));
        }
        else
        {
            var filteredList = DataModel.get().getFoodEntities()
                                        .stream()
                                        .filter(food -> food.getName().toLowerCase().contains(this.searchTf.getText().toLowerCase()))
                                        .collect(Collectors.toList());

            this.allFoodTable.getItems().setAll(filteredList);
            Collections.sort(this.allFoodTable.getItems(), Comparator.comparing(FoodEntity::getName));
        }
    }

    private void addRadioButton(BloodSugarValueEntity entity, RadioButton button)
    {
        button.setToggleGroup(this.group);
        button.setOnAction((e) ->
        {
            selectEntity(entity);
        });

        this.radioButtonMap.put(entity, button);

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
        this.saveBolusButton.setDisable(false);

        this.bzTf.setText(entity.getBloodSugar() + "");
        this.factorTf.setText(DataModel.get().getCurrentBolusFactor().getFactor() + "");

        this.bolusTf.setText("0");
        this.totalBolusTf.setText("0");

        calculateBolus();
        calculateCorrection();
    }

    private void addAddButton(FoodEntity entity, JFXButton button)
    {
        button.setOnAction((e) ->
        {
            if (!this.selectedFoodTable.getItems().contains(entity))
            {
                this.selectedFoodTable.getItems().add(entity);
                this.foodAmount.put(entity, 1);
                calculateBe();
            }
        });

        button.setOnMouseEntered(e -> ButtonHandling.onMouseEnter(button));
        button.setOnMouseExited(e -> ButtonHandling.onMouseExit(button));
        button.setStyle("-fx-background-color: #58f55b");
    }

    private void addDeleteButton(FoodEntity entity, JFXButton button)
    {
        button.setOnAction((e) ->
        {
            this.allFoodTable.getItems().remove(entity);
            DataModel.get().deleteFoodEntity(entity);
        });

        button.setOnMouseEntered(e -> ButtonHandling.onMouseEnter(button));
        button.setOnMouseExited(e -> ButtonHandling.onMouseExit(button));
        button.setStyle("-fx-background-color: #f56042");
    }

    private void addRemoveButton(FoodEntity entity, JFXButton button)
    {
        button.setOnAction((e) ->
        {
            this.selectedFoodTable.getItems().remove(entity);
            calculateBe();
        });

        button.setOnMouseEntered(e -> ButtonHandling.onMouseEnter(button));
        button.setOnMouseExited(e -> ButtonHandling.onMouseExit(button));
        button.setStyle("-fx-background-color: #f56042");
    }

    private void calculateBe()
    {
        int carbohydrates = 0;

        for (var food : this.selectedFoodTable.getItems())
        {
            carbohydrates += food.getCarbohydrates() * this.foodAmount.get(food);
        }

        this.beTf.setText(Math.round((carbohydrates / 12.0) * 2) / 2.0 + "");
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

    private void setupBzTable()
    {
        this.tablePane.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (AddBolusScreen.this.bzTable.maxWidthProperty().isBound())
                {
                    AddBolusScreen.this.bzTable.maxWidthProperty().unbind();
                }

                AddBolusScreen.this.bzTable.maxWidthProperty().bind(AddBolusScreen.this.tablePane.widthProperty().multiply(0.4));
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

        this.bzTable.setPlaceholder(new Label("Keine Werte gefunden"));
        this.bzTable.getColumns().addAll(selectColumn, timestampCol, bzCol);
    }

    private void setupAllFoodTable()
    {
        PercentageSizedTableColumn<FoodEntity, String> nameCol = new PercentageSizedTableColumn();
        nameCol.setText("Name");
        nameCol.setPercentageWidth(20);
        nameCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<FoodEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getName());
            }
        });

        PercentageSizedTableColumn<FoodEntity, String> weightCol = new PercentageSizedTableColumn();
        weightCol.setText("Gewicht (g)");
        weightCol.setPercentageWidth(20);

        weightCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<FoodEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getWeight() + "");
            }
        });

        PercentageSizedTableColumn<FoodEntity, String> carboCol = new PercentageSizedTableColumn();
        carboCol.setText("Kohlenhydrate (g)");
        carboCol.setPercentageWidth(20);

        carboCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<FoodEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getCarbohydrates() + "");
            }
        });

        PercentageSizedTableColumn<FoodEntity, JFXButton> addCol = new PercentageSizedTableColumn();
        addCol.setText("");
        addCol.setPercentageWidth(20);

        addCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, JFXButton>, ObservableValue<JFXButton>>()
        {
            @Override
            public ObservableValue<JFXButton> call(CellDataFeatures<FoodEntity, JFXButton> t)
            {
                var button = new JFXButton("Hinzufügen");
                addAddButton(t.getValue(), button);
                return new ReadOnlyObjectWrapper(button);
            }
        });

        PercentageSizedTableColumn<FoodEntity, JFXButton> delCol = new PercentageSizedTableColumn();
        delCol.setText("");
        delCol.setPercentageWidth(20);

        delCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, JFXButton>, ObservableValue<JFXButton>>()
        {
            @Override
            public ObservableValue<JFXButton> call(CellDataFeatures<FoodEntity, JFXButton> t)
            {
                var button = new JFXButton("Löschen");
                addDeleteButton(t.getValue(), button);
                return new ReadOnlyObjectWrapper(button);
            }
        });

        this.allFoodTable.setPlaceholder(new Label("Keine Werte gefunden"));
        this.allFoodTable.getColumns().addAll(nameCol, weightCol, carboCol, addCol, delCol);
    }

    private void setupSelectedFoodTable()
    {
        PercentageSizedTableColumn<FoodEntity, String> nameCol = new PercentageSizedTableColumn();
        nameCol.setText("Name");
        nameCol.setPercentageWidth(20);
        nameCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<FoodEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getName());
            }
        });

        PercentageSizedTableColumn<FoodEntity, String> weightCol = new PercentageSizedTableColumn();
        weightCol.setText("Gewicht (g)");
        weightCol.setPercentageWidth(20);

        weightCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<FoodEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getWeight() + "");
            }
        });

        PercentageSizedTableColumn<FoodEntity, String> carboCol = new PercentageSizedTableColumn();
        carboCol.setText("Kohlenhydrate (g)");
        carboCol.setPercentageWidth(20);

        carboCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<FoodEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getCarbohydrates() + "");
            }
        });

        PercentageSizedTableColumn<FoodEntity, Spinner> amountCol = new PercentageSizedTableColumn();
        amountCol.setText("Menge");
        amountCol.setPercentageWidth(20);

        amountCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, Spinner>, ObservableValue<Spinner>>()
        {
            @Override
            public ObservableValue<Spinner> call(CellDataFeatures<FoodEntity, Spinner> t)
            {
                var spinner = new Spinner<Integer>();
                spinner.setEditable(true);

                var fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999, AddBolusScreen.this.foodAmount.get(t.getValue()));

                fact.valueProperty().addListener(e ->
                {
                    AddBolusScreen.this.foodAmount.put(t.getValue(), spinner.getValue());
                    calculateBe();
                });

                spinner.setValueFactory(fact);
                return new ReadOnlyObjectWrapper(spinner);
            }
        });

        PercentageSizedTableColumn<FoodEntity, JFXButton> delCol = new PercentageSizedTableColumn();
        delCol.setText("");
        delCol.setPercentageWidth(20);

        delCol.setCellValueFactory(new Callback<CellDataFeatures<FoodEntity, JFXButton>, ObservableValue<JFXButton>>()
        {
            @Override
            public ObservableValue<JFXButton> call(CellDataFeatures<FoodEntity, JFXButton> t)
            {
                var button = new JFXButton("Entfernen");
                addRemoveButton(t.getValue(), button);
                return new ReadOnlyObjectWrapper(button);
            }
        });

        this.selectedFoodTable.setPlaceholder(new Label("Keine Werte gefunden"));
        this.selectedFoodTable.getColumns().addAll(nameCol, weightCol, carboCol, amountCol, delCol);
    }

    public void saveBolus()
    {
        var bolus = new BolusEntity();
        bolus.setBe(Double.parseDouble(StringUtils.leftPad(this.beTf.getText(), 1, "0")));
        bolus.setBolusUnits(Double.parseDouble(StringUtils.leftPad(this.bolusTf.getText(), 1, "0")));
        bolus.setCorrectionUnits(Double.parseDouble(StringUtils.leftPad(this.correctionTf.getText(), 1, "0")));
        bolus.setFactor(Double.parseDouble(StringUtils.leftPad(this.factorTf.getText(), 1, "0")));

        DataModel.get().addBolus(bolus);
        DataModel.get().connectBloodSugarBolus(this.selectedEntity, bolus);

        this.beTf.setText("0");
        this.correctionTf.setText("0");
        this.bolusTf.setText("0");

        this.selectedFoodTable.getItems().clear();

        refreshBzTableData();
    }

    public void saveFood()
    {
        var food = new FoodEntity();
        food.setName(this.foodNameTf.getText());
        food.setWeight(Integer.parseInt(StringUtils.leftPad(this.weightTf.getText(), 1, "0")));
        food.setCarbohydrates(Integer.parseInt(StringUtils.leftPad(this.foodBeTf.getText(), 1, "0")));

        DataModel.get().addFoodEntity(food);
        this.allFoodTable.getItems().setAll(DataModel.get().getFoodEntities());
        Collections.sort(this.allFoodTable.getItems(), Comparator.comparing(FoodEntity::getName));

        this.foodBeTf.setText("0");
        this.weightTf.setText("0");
        this.foodNameTf.setText("");
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
        setupBzTable();
        setupAllFoodTable();
        setupSelectedFoodTable();

        MessageDispatcher.get().subscribeTo(NewBloodSugarValue.class, ent -> addEntry(ent.getBz()));
        MessageDispatcher.get().subscribeTo(DeletedBloodSugarValue.class, ent -> removeEntry(ent.getBz()));

        MessageDispatcher.get().subscribeTo(ModelLoadStarted.class, e -> Platform.runLater(() ->
        {
            this.bzTable.setPlaceholder(new Label("Lade Werte..."));
            this.allFoodTable.setPlaceholder(new Label("Lade Werte..."));
            this.selectedFoodTable.setPlaceholder(new Label("Keine Werte gefunden"));
        }));

        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> Platform.runLater(() ->
        {
            refreshBzTableData();
            this.allFoodTable.getItems().setAll(DataModel.get().getFoodEntities());
            this.bzTable.setPlaceholder(new Label("Keine Werte gefunden"));
            this.allFoodTable.setPlaceholder(new Label("Keine Werte gefunden"));
            this.saveFoodButton.setDisable(false);
            this.searchTf.setDisable(false);
        }));

        this.beTf.setDisable(true);
        this.bolusTf.setDisable(true);
        this.correctionTf.setDisable(true);
        this.saveBolusButton.setDisable(true);

        this.beTf.setText("0");
        this.foodBeTf.setText("0");
        this.weightTf.setText("0");

        this.saveFoodButton.setDisable(true);
        this.searchTf.setDisable(true);
    }

    private void refreshBzTableData()
    {
        this.bzTable.getItems().setAll(DataModel.get().getBloodSugarValues()
                                                .stream()
                                                .filter(bz -> bz.getBolus() == null)
                                                .collect(Collectors.toList()));
    }

    private void removeEntry(BloodSugarValueEntity entry)
    {
        this.bzTable.getItems().removeIf(ent ->
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
        this.bzTable.getItems().add(entry);
        Collections.sort(this.bzTable.getItems());
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