package otf.obj.msg.remote;

import otf.model.ClientDataModel;
import otf.obj.BloodSugarValueEntity;

/**
 * @author &#8904
 *
 */
public class AddBloodSugarEntityResponse implements ExecutableResponse
{
    private BloodSugarValueEntity bz;

    public AddBloodSugarEntityResponse(BloodSugarValueEntity bz)
    {
        this.bz = bz;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        ClientDataModel.get().addBloodSugarValue(this.bz);
    }
}