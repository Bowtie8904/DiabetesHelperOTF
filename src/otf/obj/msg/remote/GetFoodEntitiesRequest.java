package otf.obj.msg.remote;

import otf.model.ServerDataModel;

/**
 * @author &#8904
 *
 */
public class GetFoodEntitiesRequest implements ExecutableRequest
{
    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        var food = ServerDataModel.get().getFoodEntities();
        var response = new GetFoodEntitiesResponse();
        response.setFoodEntities(food);
        return response;
    }
}