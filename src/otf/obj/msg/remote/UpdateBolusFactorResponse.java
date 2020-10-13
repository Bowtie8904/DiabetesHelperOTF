package otf.obj.msg.remote;

import otf.model.ClientDataModel;
import otf.obj.BolusFactorEntity;

/**
 * @author &#8904
 *
 */
public class UpdateBolusFactorResponse implements ExecutableResponse
{
    private BolusFactorEntity bolus;

    public UpdateBolusFactorResponse(BolusFactorEntity bolus)
    {
        this.bolus = bolus;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        for (var b : ClientDataModel.get().getBolusFactors())
        {
            if (b.getId().equals(this.bolus.getId()))
            {
                b.setFactor(this.bolus.getFactor());
                b.setStartTime(this.bolus.getStartTime());
            }
        }
    }
}