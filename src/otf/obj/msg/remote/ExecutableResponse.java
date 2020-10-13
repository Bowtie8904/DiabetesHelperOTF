package otf.obj.msg.remote;

/**
 * @author &#8904
 *
 */
public interface ExecutableResponse
{
    /**
     * Defines the behavior that will be executed by the part that receives this response.
     */
    public void execute();
}