package otf.obj.msg.remote;

import otf.model.ClientDataModel;

/**
 * @author &#8904
 *
 */
public class GetCorrectionUnitsResponse implements ExecutableResponse
{
    private int correctionUnits;

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        ClientDataModel.get().setCorrectionUnits(this.correctionUnits);
    }

    /**
     * @return the correctionUnits
     */
    public int getCorrectionUnits()
    {
        return this.correctionUnits;
    }

    /**
     * @param correctionUnits
     *            the correctionUnits to set
     */
    public void setCorrectionUnits(int correctionUnits)
    {
        this.correctionUnits = correctionUnits;
    }
}