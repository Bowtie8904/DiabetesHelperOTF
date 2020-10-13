package otf;

import bt.console.input.CommandParser;
import bt.console.input.FlagCommand;
import bt.console.input.ValueCommand;
import bt.gui.fx.core.instance.ApplicationStarted;
import bt.gui.fx.core.instance.ScreenInstanceDispatcher;
import bt.log.Logger;
import bt.scheduler.Threads;
import otf.gui.ScreenManager;
import otf.model.ClientDataModel;
import otf.model.ServerDataModel;
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

        var parser = new CommandParser("-");

        var clientCmd = new FlagCommand("client", "c").usage("-client")
                                                      .description("Flag to set application to client mode.");

        var serverCmd = new FlagCommand("server", "s").usage("-server")
                                                      .description("Flag to set application to server mode.");

        var hostCmd = new ValueCommand("host").usage("-host <hostname>")
                                              .description("[Optional] Sets the hostname to connect to. Ommit to connect to localhost.");

        var portCmd = new ValueCommand("port").usage("-port <portnumber>")
                                              .description("Sets the port to connect to.");

        parser.register(clientCmd);
        parser.register(hostCmd);
        parser.register(portCmd);
        parser.register(serverCmd);
        parser.registerDefaultHelpCommand("help", "h");
        parser.parse(args);

        if (parser.getFlag("client"))
        {
            Texts.get().register(new TextDefinition());
            Texts.get().setLanguage("DE");
            Texts.get().load("all");

            ScreenInstanceDispatcher.get().subscribeTo(ApplicationStarted.class, e ->
            {
                Threads.get().execute(() ->
                {
                    ClientDataModel.get().setupClient(parser.getValue("host"), Integer.parseInt(parser.getValue("port")));
                    ClientDataModel.get().loadData();
                });
            });

            ScreenManager.main(args);
        }
        else if (parser.getFlag("server"))
        {
            ServerDataModel.get().loadData();
            ServerDataModel.get().setupServer(Integer.parseInt(parser.getValue("port")));
        }
    }
}