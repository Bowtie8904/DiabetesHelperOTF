package otf;

import bt.gui.fx.core.instance.ApplicationStarted;
import bt.gui.fx.core.instance.ScreenInstanceDispatcher;
import bt.scheduler.Threads;
import otf.gui.ScreenManager;
import otf.model.DataModel;

/**
 * @author &#8904
 *
 */
public class Main
{
    public static void main(String[] args)
    {
        ScreenInstanceDispatcher.get().subscribeTo(ApplicationStarted.class, e ->
        {
            Threads.get().execute(() -> DataModel.get().loadData());
        });

        ScreenManager.main(args);
    }
}