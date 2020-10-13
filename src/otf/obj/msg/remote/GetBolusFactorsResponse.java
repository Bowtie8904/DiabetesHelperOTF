package otf.obj.msg.remote;

import otf.model.ClientDataModel;
import otf.obj.BolusFactorEntity;

/**
 * @author &#8904
 *
 */
public class GetBolusFactorsResponse implements ExecutableResponse
{
    private BolusFactorEntity[] bolusFactors;

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        ClientDataModel.get().setBolusFactors(this.bolusFactors);
    }

    /**
     * @return the bolusFactors
     */
    public BolusFactorEntity[] getBolusFactors()
    {
        return this.bolusFactors;
    }

    /**
     * @param bolusFactors
     *            the bolusFactors to set
     */
    public void setBolusFactors(BolusFactorEntity[] bolusFactors)
    {
        this.bolusFactors = bolusFactors;
    }
}