package otf.gui.screens;

import bt.gui.fx.core.FxMultiScreen;
import bt.gui.fx.core.annot.FxmlElement;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoadStarted;
import otf.obj.msg.ModelLoaded;

/**
 * @author &#8904
 *
 */
public class BaseScreen extends FxMultiScreen
{
    @FxmlElement
    private TabPane tabPane;

    /**
     * @see bt.gui.fx.core.FxMultiScreen#loadScreens()
     */
    @Override
    protected void loadScreens()
    {
        addTabScreens(OverviewScreen.class);
        addTabScreens(AddBloodSugarScreen.class);
        addTabScreens(AddBolusScreen.class);
        addTabScreens(ConfigScreen.class);
    }

    public <T extends TabBase> void addTabScreens(Class<T>... screenTypes)
    {
        super.addScreens(screenTypes);

        for (Class<T> type : screenTypes)
        {
            setupTab(type);
        }
    }

    private void setupTab(Class<? extends TabBase> type)
    {
        TabBase screen = (TabBase)getScreen(type);
        screen.load();
        Tab tab = new Tab();
        tab.setText(screen.getTabName());
        tab.setOnSelectionChanged(e ->
        {
            if (tab.isSelected())
            {
                screen.onTabSelect();
            }
            else
            {
                screen.onTabDeselect();
            }
        });

        screen.setTablPane(this.tabPane);
        this.tabPane.getTabs().add(tab);
        tab.setContent(screen.getRoot());
    }

    /**
     * @see bt.gui.fx.core.FxMultiScreen#setupStage(javafx.stage.Stage)
     */
    @Override
    protected void setupStage(Stage stage)
    {
        setShouldMaximize(true);
        setIcons("/blooddrop.png");
        stage.setTitle("Diabetes Helper (OTF)");

        MessageDispatcher.get().subscribeTo(ModelLoadStarted.class, e -> Platform.runLater(() -> stage.setTitle("Diabetes Helper (OTF)     Loading data...")));
        MessageDispatcher.get().subscribeTo(ModelLoaded.class, e -> Platform.runLater(() -> stage.setTitle("Diabetes Helper (OTF)")));
    }

    /**
     * @see bt.gui.fx.core.FxMultiScreen#setupScene(javafx.scene.Scene)
     */
    @Override
    protected void setupScene(Scene scene)
    {
    }

    /**
     * @see bt.gui.fx.core.FxMultiScreen#finishSetup()
     */
    @Override
    protected void finishSetup()
    {
    }

}