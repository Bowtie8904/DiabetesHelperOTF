package otf.obj.msg.remote;

import otf.model.ServerDataModel;
import otf.obj.FoodEntity;

/**
 * @author &#8904
 *
 */
public class AddFoodEntityRequest implements ExecutableRequest
{
    private FoodEntity food;

    public AddFoodEntityRequest(FoodEntity food)
    {
        this.food = food;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableRequest#execute()
     */
    @Override
    public ExecutableResponse execute()
    {
        ServerDataModel.get().addFoodEntity(this.food);
        var response = new AddFoodEntityResponse(this.food);
        return response;
    }
}