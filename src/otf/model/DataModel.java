package otf.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bt.types.Singleton;
import otf.model.db.Database;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;
import otf.obj.BolusFactorEntity;
import otf.obj.FoodEntity;
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
    private List<FoodEntity> foodEntities;
    private BolusFactorEntity[] bolusFactors;
    private int correctionUnits;

    public static DataModel get()
    {
        return Singleton.of(DataModel.class);
    }

    public DataModel()
    {
        this.db = new Database();
        this.bloodSugarValues = new ArrayList<>();
        this.foodEntities = new ArrayList<>();
    }

    public void loadData()
    {
        MessageDispatcher.get().dispatch(new ModelLoadStarted(this));

        this.bloodSugarValues = this.db.selectBloodSugarValues();
        this.bolusFactors = this.db.selectBolusFactors();
        this.foodEntities = this.db.selectFoodEntities();

        String correctionString = this.db.getProperty("CorrectionUnits");

        if (correctionString == null)
        {
            correctionString = "30";
            this.db.setProperty("CorrectionUnits", correctionString);
        }

        this.correctionUnits = Integer.parseInt(correctionString);

        MessageDispatcher.get().dispatch(new ModelLoaded(this));
    }

    /**
     * @return the bolusFactors
     */
    public BolusFactorEntity[] getBolusFactors()
    {
        return this.bolusFactors;
    }

    public BolusFactorEntity getCurrentBolusFactor()
    {
        BolusFactorEntity current = null;

        long currentTime = LocalTime.now().toSecondOfDay() * 1000;

        for (var ent : this.bolusFactors)
        {
            if (ent.getStartTime() < currentTime)
            {
                current = ent;
            }
        }

        return current;
    }

    public List<BloodSugarValueEntity> getBloodSugarValues()
    {
        return this.bloodSugarValues;
    }

    public List<FoodEntity> getFoodEntities()
    {
        return this.foodEntities;
    }

    public void addFoodEntity(FoodEntity entity)
    {
        this.foodEntities.add(entity);
        this.db.insertFoodEntity(entity);
    }

    public void deleteFoodEntity(FoodEntity entity)
    {
        this.foodEntities.remove(entity);
        this.db.deleteFoodEntity(entity);
    }

    public void updateBolusFactor(BolusFactorEntity entity)
    {
        this.db.updateBolusFactor(entity);
    }

    public int getCorretionUnits()
    {
        return this.correctionUnits;
    }

    public void setCorrectionUnits(int units)
    {
        this.correctionUnits = units;
        this.db.setProperty("CorrectionUnits", units + "");
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

    public void addBolus(BolusEntity entity)
    {
        if (entity.getId() == null)
        {
            this.db.insertBolus(entity);
        }
    }

    public void connectBloodSugarBolus(BloodSugarValueEntity bz, BolusEntity bo)
    {
        bz.setBolus(bo);
        this.db.connectBloodSugarBolus(bz, bo);
    }
}