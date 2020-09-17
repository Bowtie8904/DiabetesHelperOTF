package otf.obj;

/**
 * @author &#8904
 *
 */
public class FoodEntity
{
    private Long id;
    private int weight;
    private int carbohydrates;
    private String name;

    /**
     * @return the id
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the weight
     */
    public int getWeight()
    {
        return this.weight;
    }

    /**
     * @param weight
     *            the weight to set
     */
    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    /**
     * @return the carbohydrates
     */
    public int getCarbohydrates()
    {
        return this.carbohydrates;
    }

    /**
     * @param carbohydrates
     *            the carbohydrates to set
     */
    public void setCarbohydrates(int carbohydrates)
    {
        this.carbohydrates = carbohydrates;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
}