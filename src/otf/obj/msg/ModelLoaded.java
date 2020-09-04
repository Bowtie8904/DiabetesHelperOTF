package otf.obj.msg;

import otf.model.DataModel;

/**
 * @author &#8904
 *
 */
public class ModelLoaded
{
    private DataModel model;

    public ModelLoaded(DataModel model)
    {
        this.model = model;
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