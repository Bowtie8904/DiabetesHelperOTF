package otf.obj.msg.remote;

import otf.model.ServerDataModel;

/**
 * @author &#8904
 *
 */
public class UpdateCorrectionUnitsRequest implements ExecutableRequest
{
    private int units;

    public UpdateCorrectionUnitsRequest(int units)
    {
        this.units = units;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        ServerDataModel.get().setCorrectionUnits(this.units);
        var response = new UpdateCorrectionUnitsResponse(this.units);
        return response;
    }
}