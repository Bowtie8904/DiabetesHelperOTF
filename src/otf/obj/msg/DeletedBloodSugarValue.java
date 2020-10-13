package otf.obj.msg;

import otf.model.ClientDataModel;
import otf.obj.BloodSugarValueEntity;

/**
 * @author &#8904
 *
 */
public class DeletedBloodSugarValue
{
    private BloodSugarValueEntity bz;
    private ClientDataModel model;

    public DeletedBloodSugarValue(BloodSugarValueEntity bz, ClientDataModel model)
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
    public ClientDataModel getModel()
    {
        return this.model;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(ClientDataModel model)
    {
        this.model = model;
    }
}