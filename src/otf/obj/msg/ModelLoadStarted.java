package otf.obj.msg;

import otf.model.DataModel;

/**
 * @author &#8904
 *
 */
public class ModelLoadStarted
{
    private DataModel model;

    public ModelLoadStarted(DataModel model)
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