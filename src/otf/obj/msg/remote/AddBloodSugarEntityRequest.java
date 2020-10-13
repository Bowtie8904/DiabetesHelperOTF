package otf.obj.msg.remote;

import otf.model.ServerDataModel;
import otf.obj.BloodSugarValueEntity;

/**
 * @author &#8904
 *
 */
public class AddBloodSugarEntityRequest implements ExecutableRequest
{
    private BloodSugarValueEntity bz;

    public AddBloodSugarEntityRequest(BloodSugarValueEntity bz)
    {
        this.bz = bz;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        ServerDataModel.get().addBloodSugarValue(this.bz);
        var response = new AddBloodSugarEntityResponse(this.bz);
        return response;
    }
}