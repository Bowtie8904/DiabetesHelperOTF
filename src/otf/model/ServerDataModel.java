package otf.model;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bt.async.AsyncException;
import bt.async.Data;
import bt.log.Logger;
import bt.remote.socket.Client;
import bt.remote.socket.Server;
import bt.remote.socket.data.DataProcessor;
import bt.remote.socket.evnt.NewClientConnection;
import bt.scheduler.Threads;
import bt.types.Singleton;
import otf.model.db.Database;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;
import otf.obj.BolusFactorEntity;
import otf.obj.FoodEntity;
import otf.obj.msg.MessageDispatcher;
import otf.obj.msg.NewBloodSugarValue;
import otf.obj.msg.NewBolus;
import otf.obj.msg.remote.ExecutableRequest;
import otf.obj.msg.remote.ExecutableResponse;

/**
 * @author &#8904
 *
 */
public class ServerDataModel implements DataProcessor
{
    private Database db;
    private Server server;
    private List<BloodSugarValueEntity> bloodSugarValues;
    private List<FoodEntity> foodEntities;
    private BolusFactorEntity[] bolusFactors;
    private int correctionUnits;

    public static ServerDataModel get()
    {
        return Singleton.of(ServerDataModel.class);
    }

    public ServerDataModel()
    {
        this.db = new Database();

        try
        {
            this.db.setupQueryServer("[SQL] Diabetes Helper OTF", 9000);
        }
        catch (IOException e)
        {
            Logger.global().print(e);
        }

        this.bloodSugarValues = new ArrayList<>();
        this.foodEntities = new ArrayList<>();
    }

    public void setupServer(int port)
    {
        try
        {
            this.server = new Server(port);
            this.server.setName("Diabetes Helper OTF");
            this.server.setupMultiCastDiscovering();
            this.server.getEventDispatcher().subscribeTo(NewClientConnection.class, this::newClient);
            this.server.start();
        }
        catch (IOException e)
        {
            Logger.global().print(e);
        }
    }

    protected void newClient(NewClientConnection msg)
    {
        msg.getClient().setRequestProcessor(this);
    }

    public void send(ExecutableRequest request)
    {
        for (var client : this.server.getClients())
        {
            Threads.get().executeCached(() ->
            {
                send(client, request);
            });
        }
    }

    protected void send(Client client, ExecutableRequest request)
    {
        try
        {
            Object response = client.send(request).get();

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

    public int getCorrectionUnits()
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
            MessageDispatcher.get().dispatch(new NewBloodSugarValue(entity));
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
        MessageDispatcher.get().dispatch(new NewBolus(bz));
    }
}