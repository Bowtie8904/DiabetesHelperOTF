package otf.obj.msg;

import otf.obj.BloodSugarValueEntity;

/**
 * @author &#8904
 *
 */
public class NewBolus
{
    private BloodSugarValueEntity bz;

    public NewBolus(BloodSugarValueEntity bz)
    {
        this.bz = bz;
    }

    /**
     * @return the bz
     */
    public BloodSugarValueEntity getBz()
    {
        return this.bz;
    }

    /**
     * @param bz
     *            the bz to set
     */
    public void setBz(BloodSugarValueEntity bz)
    {
        this.bz = bz;
    }
}