package otf.obj.msg.remote;

import java.util.List;

import otf.model.ClientDataModel;
import otf.obj.BloodSugarValueEntity;

/**
 * @author &#8904
 *
 */
public class GetBloodSugarValuesResponse implements ExecutableResponse
{
    private List<BloodSugarValueEntity> bloodSugarValues;

    /**
     * @see otf.obj.msg.remote.ExecutableResponse#execute()
     */
    @Override
    public void execute()
    {
        ClientDataModel.get().setBloodSugarValues(this.bloodSugarValues);
    }

    /**
     * @return the bloodSugarValues
     */
    public List<BloodSugarValueEntity> getBloodSugarValues()
    {
        return this.bloodSugarValues;
    }

    /**
     * @param bloodSugarValues
     *            the bloodSugarValues to set
     */
    public void setBloodSugarValues(List<BloodSugarValueEntity> bloodSugarValues)
    {
        this.bloodSugarValues = bloodSugarValues;
    }
}