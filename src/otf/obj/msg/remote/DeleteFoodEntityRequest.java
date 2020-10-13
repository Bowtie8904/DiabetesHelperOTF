package otf.obj.msg.remote;

import otf.model.ServerDataModel;
import otf.obj.FoodEntity;

/**
 * @author &#8904
 *
 */
public class DeleteFoodEntityRequest implements ExecutableRequest
{
    private FoodEntity food;

    public DeleteFoodEntityRequest(FoodEntity food)
    {
        this.food = food;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        ServerDataModel.get().deleteFoodEntity(this.food);
        var response = new DeleteFoodEntityResponse(this.food);
        return response;
    }
}