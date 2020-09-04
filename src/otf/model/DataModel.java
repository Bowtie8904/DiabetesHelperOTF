package otf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bt.types.Singleton;
import otf.model.db.Database;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;
import otf.obj.BolusFactorEntity;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoadStarted;
import otf.obj.msg.ModelLoaded;
import otf.obj.msg.NewBloodSugarValue;

/**
 * @author &#8904
 *
 */
public class DataModel
{
    private Database db;
    private List<BloodSugarValueEntity> bloodSugarValues;
    private BolusFactorEntity[] bolusFactors;

    public static DataModel get()
    {
        return Singleton.of(DataModel.class);
    }

    public DataModel()
    {
        this.db = new Database();
        this.bloodSugarValues = new ArrayList<>();
    }

    public void loadData()
    {
        MessageDispatcher.get().dispatch(new ModelLoadStarted(this));
        this.bloodSugarValues = this.db.selectBloodSugarValues();
        this.bolusFactors = this.db.selectBolusFactors();
        MessageDispatcher.get().dispatch(new ModelLoaded(this));
    }

    /**
     * @return the bolusFactors
     */
    public BolusFactorEntity[] getBolusFactors()
    {
        return this.bolusFactors;
    }

    public List<BloodSugarValueEntity> getBloodSugarValues()
    {
        return this.bloodSugarValues;
    }

    public void addBloodSugarValue(BloodSugarValueEntity entity)
    {
        if (entity.getId() == null)
        {
            this.bloodSugarValues.add(entity);
            Collections.sort(this.bloodSugarValues);
            this.db.insertBloodSugarValue(entity);
            MessageDispatcher.get().dispatch(new NewBloodSugarValue(entity, this));
        }
    }

    public void connectBloodSugarBolus(BloodSugarValueEntity bz, BolusEntity bo)
    {
        bz.setBolus(bo);
        this.db.connectBloodSugarBolus(bz, bo);
    }
}