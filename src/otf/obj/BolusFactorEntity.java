package otf.obj;

import java.io.Serializable;

/**
 * @author &#8904
 *
 */
public class BolusFactorEntity implements Serializable
{
    private Long id;
    private double factor;
    private long startTime;

    /**
     * @param factor
     * @param startTime
     */
    public BolusFactorEntity(double factor, long startTime)
    {
        this.factor = factor;
        this.startTime = startTime;
    }

    public BolusFactorEntity()
    {
    }

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
     * @return the factor
     */
    public double getFactor()
    {
        return this.factor;
    }

    /**
     * @param factor
     *            the factor to set
     */
    public void setFactor(double factor)
    {
        this.factor = factor;
    }

    /**
     * @return the startTime
     */
    public long getStartTime()
    {
        return this.startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }
}