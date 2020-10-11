package otf.gui.screens;

import java.sql.Timestamp;
import java.util.Collections;

import bt.gui.fx.core.annot.FxmlElement;
import bt.utils.NumberUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import otf.gui.components.PercentageSizedTableColumn;
import otf.model.DataModel;
import otf.model.text.TextDefinition;
import otf.model.text.Texts;
import otf.obj.BloodSugarValueEntity;
import otf.obj.msg.DeletedBloodSugarValue;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoadStarted;
import otf.obj.msg.ModelLoaded;
import otf.obj.msg.NewBloodSugarValue;
import otf.obj.msg.NewBolus;

/**
 * @author &#8904
 *
 */
public class OverviewScreen extends TabBase
{
    @FxmlElement
    private TableView<BloodSugarValueEntity> table;

    /**
     * @see bt.gui.fx.core.FxScreen#prepareScreen()
     */
    @Override
    protected void prepareScreen()
    {
        setupTable();

        MessageDispatcher.get().subscribeTo(NewBloodSugarValue.class, ent -> addEntry(ent.getBz()));
        MessageDispatcher.get().subscribeTo(DeletedBloodSugarValue.class, ent -> removeEntry(ent.getBz()));
        MessageDispatcher.get().subscribeTo(NewBolus.class, e -> refreshTableData());
        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> refreshTableData());

        MessageDispatcher.get().subscribeTo(ModelLoadStarted.class, e -> Platform.runLater(() -> this.table.setPlaceholder(new Label(Texts.get().get(TextDefinition.LOADING_VALUES).toString()))));
        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> Platform.runLater(() -> this.table.setPlaceholder(new Label(Texts.get().get(TextDefinition.NO_VALUES_FOUND).toString()))));
    }

    private void refreshTableData()
    {
        this.table.getItems().setAll(DataModel.get().getBloodSugarValues());
    }

    private void removeEntry(int index)
    {
        removeEntry(this.table.getItems().get(index));
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

    private void setupTable()
    {
        PercentageSizedTableColumn<BloodSugarValueEntity, String> timestampCol = new PercentageSizedTableColumn();
        timestampCol.setText(Texts.get().get(TextDefinition.TIME).toString());
        timestampCol.setPercentageWidth(13);

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
        bzCol.setPercentageWidth(13);

        bzCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getBloodSugar() + "");
            }
        });

        bzCol.setCellFactory(column ->
        {
            return new TableCell<>()
            {
                @Override
                protected void updateItem(String text, boolean empty)
                {
                    if (text != null)
                    {
                        int value = Integer.parseInt(text);
                        int r = 0;

                        if (value > 120)
                        {
                            r = NumberUtils.clamp((value - 120) * 2, 0, 255);
                        }

                        int g = 180 - (value - 100);
                        int b = 0;

                        if (value < 120)
                        {
                            b = NumberUtils.clamp((value - 120) * -4, 0, 255);
                        }

                        setStyle("-fx-background-color: rgb(" + r + "," + g + ", " + b + ");");
                        setText(text);
                    }
                }
            };
        });

        PercentageSizedTableColumn<BloodSugarValueEntity, String> beCol = new PercentageSizedTableColumn();
        beCol.setText(Texts.get().get(TextDefinition.BE).toString());
        beCol.setPercentageWidth(13);

        beCol.setCellValueFactory(t ->
        {
                var bolus = t.getValue().getBolus();

                String text = "-";

                if (bolus != null)
                {
                    text = bolus.getBe() > 0 ? bolus.getBe() + "" : "-";
                }

                return new ReadOnlyStringWrapper(text);
            }
        );

        PercentageSizedTableColumn<BloodSugarValueEntity, String> factorCol = new PercentageSizedTableColumn();
        factorCol.setText(Texts.get().get(TextDefinition.FACTOR).toString());
        factorCol.setPercentageWidth(13);

        factorCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                var bolus = t.getValue().getBolus();

                String text = "-";

                if (bolus != null)
                {
                    text = bolus.getFactor() + "";
                }

                return new ReadOnlyStringWrapper(text);
            }
        });

        PercentageSizedTableColumn<BloodSugarValueEntity, String> totalBolusCol = new PercentageSizedTableColumn();
        totalBolusCol.setText(Texts.get().get(TextDefinition.TOTAL_BOLUS).toString());
        totalBolusCol.setPercentageWidth(13);

        totalBolusCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                var bolus = t.getValue().getBolus();

                String text = "-";

                if (bolus != null)
                {
                    text = bolus.getBolusUnits() + bolus.getCorrectionUnits() + "";
                }

                return new ReadOnlyStringWrapper(text);
            }
        });

        PercentageSizedTableColumn<BloodSugarValueEntity, String> bolusCol = new PercentageSizedTableColumn();
        bolusCol.setText(Texts.get().get(TextDefinition.BOLUS).toString());
        bolusCol.setPercentageWidth(13);

        bolusCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                var bolus = t.getValue().getBolus();

                String text = "-";

                if (bolus != null)
                {
                    text = bolus.getBolusUnits() + "";
                }

                return new ReadOnlyStringWrapper(text);
            }
        });

        PercentageSizedTableColumn<BloodSugarValueEntity, String> correctionCol = new PercentageSizedTableColumn();
        correctionCol.setText(Texts.get().get(TextDefinition.CORRECTION).toString());
        correctionCol.setPercentageWidth(13);

        correctionCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                var bolus = t.getValue().getBolus();

                String text = "-";

                if (bolus != null)
                {
                    text = bolus.getCorrectionUnits() + "";
                }

                return new ReadOnlyStringWrapper(text);
            }
        });

        this.table.setPlaceholder(new Label(Texts.get().get(TextDefinition.NO_VALUES_FOUND).toString()));
        this.table.getColumns().addAll(timestampCol, bzCol, beCol, factorCol, totalBolusCol, bolusCol, correctionCol);
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
     * @see otf.gui.screens.TabBase#getTabName()
     */
    @Override
    public String getTabName()
    {
        return Texts.get().get(TextDefinition.OVERVIEW).toString();
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
}