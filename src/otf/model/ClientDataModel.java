package otf.model;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bt.async.AsyncException;
import bt.async.Data;
import bt.remote.socket.Client;
import bt.remote.socket.data.DataProcessor;
import bt.remote.socket.evnt.ConnectionLost;
import bt.types.Singleton;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;
import otf.obj.BolusFactorEntity;
import otf.obj.FoodEntity;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.ModelLoadStarted;
import otf.obj.msg.ModelLoaded;
import otf.obj.msg.NewBloodSugarValue;
import otf.obj.msg.NewBolus;
import otf.obj.msg.remote.AddBloodSugarEntityRequest;
import otf.obj.msg.remote.AddBolusEntityRequest;
import otf.obj.msg.remote.AddFoodEntityRequest;
import otf.obj.msg.remote.DeleteFoodEntityRequest;
import otf.obj.msg.remote.ExecutableRequest;
import otf.obj.msg.remote.ExecutableResponse;
import otf.obj.msg.remote.GetBloodSugarValuesRequest;
import otf.obj.msg.remote.GetBolusFactorsRequest;
import otf.obj.msg.remote.GetCorrectionUnitsRequest;
import otf.obj.msg.remote.GetFoodEntitiesRequest;
import otf.obj.msg.remote.UpdateBolusFactorRequest;
import otf.obj.msg.remote.UpdateCorrectionUnitsRequest;

/**
 * @author &#8904
 *
 */
public class ClientDataModel implements DataProcessor
{
    private Client client;
    private List<BloodSugarValueEntity> bloodSugarValues;
    private List<FoodEntity> foodEntities;
    private BolusFactorEntity[] bolusFactors;
    private int correctionUnits;

    public static ClientDataModel get()
    {
        return Singleton.of(ClientDataModel.class);
    }

    public ClientDataModel()
    {
        this.bloodSugarValues = new ArrayList<>();
        this.foodEntities = new ArrayList<>();
    }

    public void setupClient(String host, int port)
    {
        try
        {
            this.client = new Client(host, port);
            this.client.setRequestProcessor(this);
            this.client.getEventDispatcher().subscribeTo(ConnectionLost.class, this::onConnectionLost);
            this.client.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onConnectionLost(ConnectionLost lost)
    {
        MessageDispatcher.get().dispatch(lost);
    }

    public void send(ExecutableRequest request)
    {
        try
        {
            Object response = this.client.send(request).get();

            if (response != null)
            {
                if (response instanceof ExecutableResponse)
                {
                    ((ExecutableResponse)response).execute();
                }
                else if (response instanceof Throwable)
                {
                    ((Throwable)response).printStackTrace();
                }
            }
        }
        catch (AsyncException | IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see bt.remote.socket.data.DataProcessor#process(bt.async.Data)
     */
    @Override
    public Object process(Data incoming)
    {
        Object response = null;
        Object request = incoming.get();

        if (request != null)
        {
            if (request instanceof ExecutableRequest)
            {
                response = ((ExecutableRequest)request).execute();
            }
            else if (request instanceof Throwable)
            {
                ((Throwable)request).printStackTrace();
            }
        }

        return response;
    }

    public void loadData()
    {
        MessageDispatcher.get().dispatch(new ModelLoadStarted(this));

        send(new GetBloodSugarValuesRequest());
        send(new GetBolusFactorsRequest());
        send(new GetFoodEntitiesRequest());
        send(new GetCorrectionUnitsRequest());

        MessageDispatcher.get().dispatch(new ModelLoaded(this));
    }

    /**
     * @return the bolusFactors
     */
    public BolusFactorEntity[] getBolusFactors()
    {
        return this.bolusFactors;
    }

    /**
     * @param bolusFactors
     *            the bolusFactors to set
     */
    public void setBolusFactors(BolusFactorEntity[] bolusFactors)
    {
        this.bolusFactors = bolusFactors;
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

    /**
     * @param bloodSugarValues
     *            the bloodSugarValues to set
     */
    public void setBloodSugarValues(List<BloodSugarValueEntity> bloodSugarValues)
    {
        this.bloodSugarValues = bloodSugarValues;
    }

    public List<FoodEntity> getFoodEntities()
    {
        return this.foodEntities;
    }

    /**
     * @param foodEntities
     *            the foodEntities to set
     */
    public void setFoodEntities(List<FoodEntity> foodEntities)
    {
        this.foodEntities = foodEntities;
    }

    public void addFoodEntity(FoodEntity entity)
    {
        this.foodEntities.add(entity);
    }

    public void insertFoodEntity(FoodEntity entity)
    {
        send(new AddFoodEntityRequest(entity));
    }

    public void removeFoodEntity(FoodEntity entity)
    {
        this.foodEntities.remove(entity);
    }

    public void deleteFoodEntity(FoodEntity entity)
    {
        send(new DeleteFoodEntityRequest(entity));
    }

    public void updateBolusFactor(BolusFactorEntity entity)
    {
        send(new UpdateBolusFactorRequest(entity));
    }

    public int getCorrectionUnits()
    {
        return this.correctionUnits;
    }

    /**
     * @param correctionUnits
     *            the correctionUnits to set
     */
    public void setCorrectionUnits(int correctionUnits)
    {
        this.correctionUnits = correctionUnits;
    }

    public void updateCorrectionUnits(int units)
    {
        send(new UpdateCorrectionUnitsRequest(units));
    }

    public void addBloodSugarValue(BloodSugarValueEntity entity)
    {
        this.bloodSugarValues.add(entity);
        Collections.sort(this.bloodSugarValues);
        MessageDispatcher.get().dispatch(new NewBloodSugarValue(entity));
    }

    public void insertBloodSugarValue(BloodSugarValueEntity entity)
    {
        if (entity.getId() == null)
        {
            send(new AddBloodSugarEntityRequest(entity));
        }
    }

    public void connectBloodSugarBolus(BloodSugarValueEntity bz, BolusEntity bo)
    {
        bz.setBolus(bo);
        MessageDispatcher.get().dispatch(new NewBolus(bz));
    }

    public void insertBloodSugarBolus(BloodSugarValueEntity bz, BolusEntity bo)
    {
        send(new AddBolusEntityRequest(bz, bo));
    }
}