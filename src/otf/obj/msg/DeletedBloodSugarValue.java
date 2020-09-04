package otf.obj.msg;

import otf.model.DataModel;
import otf.obj.BloodSugarValueEntity;

/**
 * @author &#8904
 *
 */
public class DeletedBloodSugarValue
{
    private BloodSugarValueEntity bz;
    private DataModel model;

    public DeletedBloodSugarValue(BloodSugarValueEntity bz, DataModel model)
    {
        this.bz = bz;
        this.model = model;
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

    /**
     * @return the model
     */
    public DataModel getModel()
    {
        return this.model;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(DataModel model)
    {
        this.model = model;
    }
}