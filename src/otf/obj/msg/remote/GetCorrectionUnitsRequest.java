package otf.obj.msg.remote;

import otf.model.ServerDataModel;

/**
 * @author &#8904
 *
 */
public class GetCorrectionUnitsRequest implements ExecutableRequest
{
    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        var units = ServerDataModel.get().getCorrectionUnits();
        var response = new GetCorrectionUnitsResponse();
        response.setCorrectionUnits(units);
        return response;
    }
}