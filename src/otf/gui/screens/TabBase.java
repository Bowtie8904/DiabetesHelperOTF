package otf.gui.screens;

import bt.gui.fx.core.FxScreen;
import javafx.scene.control.TabPane;

/**
 * @author &#8904
 *
 */
public abstract class TabBase extends FxScreen
{
    protected TabPane tabPane;

    public abstract String getTabName();

    public abstract void onTabSelect();

    public abstract void onTabDeselect();

    public void setTablPane(TabPane tabPane)
    {
        this.tabPane = tabPane;
    }
}