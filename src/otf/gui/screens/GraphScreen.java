package otf.gui.screens;

import bt.gui.fx.core.annot.FxmlElement;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import otf.model.ClientDataModel;
import otf.obj.BloodSugarValueEntity;
import otf.obj.msg.DeletedBloodSugarValue;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoaded;
import otf.obj.msg.NewBloodSugarValue;

/**
 * @author &#8904
 *
 */
public class GraphScreen extends TabBase
{
    @FxmlElement
    private BorderPane pane;

    @FxmlElement
    private LineChart<Number, Number> graph;

    /**
     * @see otf.gui.screens.TabBase#getTabName()
     */
    @Override
    public String getTabName()
    {
        return "Graph";
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
        MessageDispatcher.get().subscribeTo(NewBloodSugarValue.class, ent -> addEntry(ent.getBz()));
        MessageDispatcher.get().subscribeTo(DeletedBloodSugarValue.class, ent -> removeEntry(ent.getBz()));

        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> Platform.runLater(() ->
        {
            XYChart.Series series = new XYChart.Series();
            int i = 0;

            for (var bz : ClientDataModel.get().getBloodSugarValues())
            {
                series.getData().add(new XYChart.Data(bz.getTimestamp() + "", bz.getBloodSugar()));
                i ++ ;
                if (i == 100)
                {
                    break;
                }
            }

            this.graph.getData().add(series);
        }));
    }

    private void removeEntry(BloodSugarValueEntity entry)
    {
    }

    private void addEntry(BloodSugarValueEntity entry)
    {
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