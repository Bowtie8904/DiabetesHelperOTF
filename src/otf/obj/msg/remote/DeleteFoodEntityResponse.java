package otf.obj.msg.remote;

import otf.model.ClientDataModel;
import otf.obj.FoodEntity;

/**
 * @author &#8904
 *
 */
public class DeleteFoodEntityResponse implements ExecutableResponse
{
    private FoodEntity food;

    public DeleteFoodEntityResponse(FoodEntity food)
    {
        this.food = food;
    }

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        ClientDataModel.get().removeFoodEntity(this.food);
    }
}