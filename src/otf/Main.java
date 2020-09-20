package otf;

import bt.gui.fx.core.instance.ApplicationStarted;
import bt.gui.fx.core.instance.ScreenInstanceDispatcher;
import bt.log.Logger;
import bt.scheduler.Threads;
import otf.gui.ScreenManager;
import otf.model.DataModel;
import otf.model.db.Database;
import otf.model.text.TextDefinition;
import otf.model.text.Texts;

/**
 * @author &#8904
 *
 */
public class Main
{
    public static void main(String[] args)
    {
        Logger.global().hookSystemErr();
        Logger.global().hookSystemOut();
        Database.log = Logger.global();

        Texts.get().register(new TextDefinition());
        Texts.get().setLanguage("DE");
        Texts.get().load("all");

        ScreenInstanceDispatcher.get().subscribeTo(ApplicationStarted.class, e ->
        {
            Threads.get().execute(() -> DataModel.get().loadData());
        });

        ScreenManager.main(args);
    }
}