package otf.gui.screens;

import java.sql.Timestamp;
import java.util.Collections;

import bt.gui.fx.core.annot.FxmlElement;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import otf.gui.components.PercentageSizedTableColumn;
import otf.model.DataModel;
import otf.obj.BloodSugarValueEntity;
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
    private BorderPane tablePane;

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

                AddBolusScreen.this.table.maxWidthProperty().bind(AddBolusScreen.this.tablePane.widthProperty().multiply(0.3));
            }
        });

        PercentageSizedTableColumn timestampCol = new PercentageSizedTableColumn();
        timestampCol.setText("Zeit");
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

        PercentageSizedTableColumn<BloodSugarValueEntity> bzCol = new PercentageSizedTableColumn();
        bzCol.setText("Blutzucker");
        bzCol.setPercentageWidth(50);

        bzCol.setCellValueFactory(new Callback<CellDataFeatures<BloodSugarValueEntity, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<BloodSugarValueEntity, String> t)
            {
                return new ReadOnlyStringWrapper(t.getValue().getBloodSugar() + "");
            }
        });

        this.table.setPlaceholder(new Label("Keine Werte gefunden"));
        this.table.getColumns().addAll(timestampCol, bzCol);
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