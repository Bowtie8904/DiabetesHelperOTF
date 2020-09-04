package otf.gui;

import bt.gui.fx.core.FxScreenManager;
import javafx.stage.Stage;
import otf.gui.screens.BaseScreen;

/**
 * @author &#8904
 *
 */
public class ScreenManager extends FxScreenManager
{
    /**
     * @see bt.gui.fx.core.FxScreenManager#loadScreens()
     */
    @Override
    protected void loadScreens()
    {
    }

    /**
     * @see bt.gui.fx.core.FxScreenManager#startApplication()
     */
    @Override
    protected void startApplication()
    {
        setScreen(BaseScreen.class, new Stage());
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}