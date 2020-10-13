package otf.obj.msg.remote;

import otf.model.ServerDataModel;

/**
 * @author &#8904
 *
 */
public class GetBloodSugarValuesRequest implements ExecutableRequest
{
    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        var list = ServerDataModel.get().getBloodSugarValues();
        var response = new GetBloodSugarValuesResponse();
        response.setBloodSugarValues(list);
        return response;
    }
}