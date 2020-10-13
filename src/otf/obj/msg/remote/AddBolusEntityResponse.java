package otf.obj.msg.remote;

import otf.model.ClientDataModel;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;

/**
 * @author &#8904
 *
 */
public class AddBolusEntityResponse implements ExecutableResponse
{
    private BloodSugarValueEntity bz;
    private BolusEntity bo;

    public AddBolusEntityResponse(BloodSugarValueEntity bz, BolusEntity bo)
    {
        this.bz = bz;
        this.bo = bo;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        for (var b : ClientDataModel.get().getBloodSugarValues())
        {
            if (b.getId().equals(this.bz.getId()))
            {
                ClientDataModel.get().connectBloodSugarBolus(b, this.bo);
            }
        }
    }
}