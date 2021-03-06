package otf.obj.msg.remote;

import java.io.Serializable;

/**
 * @author &#8904
 *
 */
public interface ExecutableRequest extends Serializable
{
    /**
     * Defines the behavior that will be executed by the part that receives this request.
     *
     * @return A response that should be executed by the other party or null to not send a response.
     */
    public ExecutableResponse execute();
}