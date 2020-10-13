package otf.obj.msg.remote;

import otf.model.ClientDataModel;

/**
 * @author &#8904
 *
 */
public class UpdateCorrectionUnitsResponse implements ExecutableResponse
{
    private int units;

    public UpdateCorrectionUnitsResponse(int units)
    {
        this.units = units;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        ClientDataModel.get().setCorrectionUnits(this.units);
    }
}