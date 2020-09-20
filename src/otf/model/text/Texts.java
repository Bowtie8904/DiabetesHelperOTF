package otf.model.text;

import bt.io.text.impl.BaseTextLoader;
import bt.types.Singleton;

/**
 * @author &#8904
 *
 */
public class Texts extends BaseTextLoader
{
    public static Texts get()
    {
        return Singleton.of(Texts.class);
    }
}