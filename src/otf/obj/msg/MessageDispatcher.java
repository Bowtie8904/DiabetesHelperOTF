package otf.obj.msg;

import bt.runtime.evnt.Dispatcher;
import bt.types.Singleton;

/**
 * @author &#8904
 *
 */
public class MessageDispatcher extends Dispatcher
{
    public static MessageDispatcher get()
    {
        return Singleton.of(MessageDispatcher.class);
    }
}