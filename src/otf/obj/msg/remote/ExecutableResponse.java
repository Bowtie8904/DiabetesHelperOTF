package otf.obj.msg.remote;

import java.io.Serializable;

/**
 * @author &#8904
 *
 */
public interface ExecutableResponse extends Serializable
{
    /**
     * Defines the behavior that will be executed by the part that receives this response.
     */
    public void execute();
}