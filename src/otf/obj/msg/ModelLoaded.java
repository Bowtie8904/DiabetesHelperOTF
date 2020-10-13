package otf.obj.msg;

import otf.model.ClientDataModel;

/**
 * @author &#8904
 *
 */
public class ModelLoaded
{
    private ClientDataModel model;

    public ModelLoaded(ClientDataModel model)
    {
        this.model = model;
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