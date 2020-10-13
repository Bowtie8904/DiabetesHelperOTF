package otf.obj;

import java.io.Serializable;

/**
 * @author &#8904
 *
 */
public class BolusEntity implements Serializable
{
    private Long id;
    private double be;
    private double factor;
    private double correctionUnits;
    private double bolusUnits;

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
     * @return the be
     */
    public double getBe()
    {
        return this.be;
    }

    /**
     * @param be
     *            the be to set
     */
    public void setBe(double be)
    {
        this.be = be;
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
     * @return the correctionUnits
     */
    public double getCorrectionUnits()
    {
        return this.correctionUnits;
    }

    /**
     * @param correctionUnits
     *            the correctionUnits to set
     */
    public void setCorrectionUnits(double correctionUnits)
    {
        this.correctionUnits = correctionUnits;
    }

    /**
     * @return the totalUnits
     */
    public double getBolusUnits()
    {
        return this.bolusUnits;
    }

    /**
     * @param totalUnits
     *            the totalUnits to set
     */
    public void setBolusUnits(double bolusUnits)
    {
        this.bolusUnits = bolusUnits;
    }
}