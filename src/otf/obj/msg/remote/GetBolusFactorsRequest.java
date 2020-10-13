package otf.obj.msg.remote;

import otf.model.ServerDataModel;

/**
 * @author &#8904
 *
 */
public class GetBolusFactorsRequest implements ExecutableRequest
{
    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        var factors = ServerDataModel.get().getBolusFactors();
        var response = new GetBolusFactorsResponse();
        response.setBolusFactors(factors);
        return response;
    }
}