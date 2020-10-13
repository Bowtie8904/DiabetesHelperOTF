package otf.obj.msg.remote;

import java.util.List;

import otf.model.ClientDataModel;
import otf.obj.FoodEntity;

/**
 * @author &#8904
 *
 */
public class GetFoodEntitiesResponse implements ExecutableResponse
{
    private List<FoodEntity> foodEntities;

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        ClientDataModel.get().setFoodEntities(this.foodEntities);
    }

    /**
     * @return the foodEntities
     */
    public List<FoodEntity> getFoodEntities()
    {
        return this.foodEntities;
    }

    /**
     * @param foodEntities
     *            the foodEntities to set
     */
    public void setFoodEntities(List<FoodEntity> foodEntities)
    {
        this.foodEntities = foodEntities;
    }
}