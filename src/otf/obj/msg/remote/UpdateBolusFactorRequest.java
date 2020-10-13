package otf.obj.msg.remote;

import otf.model.ServerDataModel;
import otf.obj.BolusFactorEntity;

/**
 * @author &#8904
 *
 */
public class UpdateBolusFactorRequest implements ExecutableRequest
{
    private BolusFactorEntity bolus;

    public UpdateBolusFactorRequest(BolusFactorEntity bolus)
    {
        this.bolus = bolus;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        ServerDataModel.get().updateBolusFactor(this.bolus);

        for (var b : ServerDataModel.get().getBolusFactors())
        {
            if (b.getId().equals(this.bolus.getId()))
            {
                b.setFactor(this.bolus.getFactor());
                b.setStartTime(this.bolus.getStartTime());
            }
        }

        var response = new UpdateBolusFactorResponse(this.bolus);
        return response;
    }
}