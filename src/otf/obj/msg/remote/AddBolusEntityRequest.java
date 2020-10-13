package otf.obj.msg.remote;

import otf.model.ServerDataModel;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;

/**
 * @author &#8904
 *
 */
public class AddBolusEntityRequest implements ExecutableRequest
{
    private BloodSugarValueEntity bz;
    private BolusEntity bo;

    public AddBolusEntityRequest(BloodSugarValueEntity bz, BolusEntity bo)
    {
        this.bz = bz;
        this.bo = bo;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        AddBolusEntityResponse response = null;

        for (var b : ServerDataModel.get().getBloodSugarValues())
        {
            if (b.getId().equals(this.bz.getId()))
            {
                ServerDataModel.get().connectBloodSugarBolus(b, this.bo);
                response = new AddBolusEntityResponse(this.bz, this.bo);
            }
        }

        return response;
    }
}