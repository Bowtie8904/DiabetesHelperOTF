package otf.model.db;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import bt.db.EmbeddedDatabase;
import bt.db.constants.Delete;
import bt.db.constants.Generated;
import bt.db.constants.SqlType;
import bt.db.listener.impl.IdentityListener;
import bt.db.statement.clause.Column;
import bt.db.statement.clause.ColumnEntry;
import bt.db.statement.clause.foreign.ColumnForeignKey;
import bt.log.Logger;
import otf.obj.BloodSugarValueEntity;
import otf.obj.BolusEntity;
import otf.obj.BolusFactorEntity;
import otf.obj.FoodEntity;

/**
 * @author &#8904
 *
 */
public class Database extends EmbeddedDatabase
{
    /**
     * @see bt.db.DatabaseAccess#createTables()
     */
    @Override
    protected void createTables()
    {
        create().table("Bolus")
                .column(new Column("boID", SqlType.LONG).primaryKey().generated(Generated.ALWAYS).asIdentity())
                .column(new Column("BE", SqlType.DOUBLE).defaultValue(0))
                .column(new Column("Factor", SqlType.DOUBLE).defaultValue(0))
                .column(new Column("BolusUnits", SqlType.DOUBLE).defaultValue(0))
                .column(new Column("CorrectionUnits", SqlType.DOUBLE).defaultValue(0))
                .onAlreadyExists((stmt, e) ->
                {
                    Logger.global().print("Table " + stmt.getName() + " already exists.");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());
                    return 0;
                })
                .onSuccess((stmt, e) ->
                {
                    Logger.global().print("Created table " + stmt.getName() + ".");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());
                })
                .execute();

        create().table("BloodSugarValue")
                .column(new Column("bzID", SqlType.LONG).primaryKey().generated(Generated.ALWAYS).asIdentity())
                .column(new Column("Timestamp", SqlType.LONG))
                .column(new Column("BZ", SqlType.INTEGER).defaultValue(0))
                .column(new Column("BolusID", SqlType.LONG).foreignKey(new ColumnForeignKey().references("Bolus", "boID").on(Delete.SET_NULL)))
                .onAlreadyExists((stmt, e) ->
                {
                    Logger.global().print("Table " + stmt.getName() + " already exists.");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());
                    return 0;
                })
                .onSuccess((stmt, e) ->
                {
                    Logger.global().print("Created table " + stmt.getName() + ".");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());
                })
                .execute();

        create().table("BolusFactor")
                .column(new Column("bfID", SqlType.LONG).primaryKey().generated(Generated.ALWAYS).asIdentity())
                .column(new Column("factor", SqlType.DOUBLE).defaultValue(0))
                .column(new Column("startTime", SqlType.LONG).defaultValue(0))
                .onAlreadyExists((stmt, e) ->
                {
                    Logger.global().print("Table " + stmt.getName() + " already exists.");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());
                    return 0;
                })
                .onSuccess((stmt, e) ->
                {
                    Logger.global().print("Created table " + stmt.getName() + ".");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());

                    insertBolusFactor(new BolusFactorEntity(0.5, LocalTime.of(0, 0).toSecondOfDay() * 1000));
                    insertBolusFactor(new BolusFactorEntity(1.5, LocalTime.of(11, 30).toSecondOfDay() * 1000));
                    insertBolusFactor(new BolusFactorEntity(1.0, LocalTime.of(17, 30).toSecondOfDay() * 1000));
                })
                .execute();

        create().table("Food")
                .column(new Column("foodId", SqlType.LONG).primaryKey().generated(Generated.ALWAYS).asIdentity())
                .column(new Column("Weight", SqlType.INTEGER))
                .column(new Column("Name", SqlType.VARCHAR).size(200))
                .column(new Column("Carbohydrates", SqlType.INTEGER))
                .onAlreadyExists((stmt, e) ->
                {
                    Logger.global().print("Table " + stmt.getName() + " already exists.");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());
                    return 0;
                })
                .onSuccess((stmt, e) ->
                {
                    Logger.global().print("Created table " + stmt.getName() + ".");
                    Logger.global().print("Execution time: " + stmt.getExecutionTime());
                })
                .execute();
    }

    public List<FoodEntity> selectFoodEntities()
    {
        List<FoodEntity> list = new ArrayList<>();

        select().from("Food")
                .onFail((stmt, e) ->
                {
                    Logger.global().print(e);
                    Logger.global().print(e.getSql());
                    return null;
                })
                .orderBy("Name").asc()
                .executeAsStream().stream().forEach(result ->
                {
                    var food = new FoodEntity();

                    try
                    {
                        food.setId(result.getLong("foodId"));
                        food.setName(result.getString("Name"));
                        food.setWeight(result.getInt("Weight"));
                        food.setCarbohydrates(result.getInt("Carbohydrates"));
                    }
                    catch (SQLException e)
                    {
                        Logger.global().print(e);
                    }

                    list.add(food);
                });

        return list;
    }

    public void insertFoodEntity(FoodEntity entity)
    {
        insert().into("Food")
                .set("Name", entity.getName())
                .set("Weight", entity.getWeight())
                .set("Carbohydrates", entity.getCarbohydrates())
                .onSuccess((stmt, i) ->
                {
                    entity.setId(IdentityListener.getLast("Food"));
                })
                .commit()
                .execute();
    }

    public void deleteFoodEntity(FoodEntity entity)
    {
        delete().from("Food")
                .where("foodId").equal(entity.getId())
                .commit()
                .execute();
    }

    public BolusFactorEntity[] selectBolusFactors()
    {
        List<BolusFactorEntity> factors = new ArrayList<>();

        select().from("BolusFactor")
                .onFail((stmt, e) ->
                {
                    Logger.global().print(e);
                    Logger.global().print(e.getSql());
                    return null;
                })
                .orderBy("bfid").asc()
                .executeAsStream().stream().forEach(result ->
                {
                    var bf = new BolusFactorEntity();

                    try
                    {
                        bf.setId(result.getLong("bfID"));
                        bf.setFactor(result.getDouble("factor"));
                        bf.setStartTime(result.getLong("startTime"));
                    }
                    catch (SQLException e)
                    {
                        Logger.global().print(e);
                    }

                    factors.add(bf);
                });

        return factors.toArray(BolusFactorEntity[]::new);
    }

    public void insertBolusFactor(BolusFactorEntity entity)
    {
        insert().into("BolusFactor")
                .set("startTime", entity.getStartTime())
                .set("factor", entity.getFactor())
                .onSuccess((stmt, i) ->
                {
                    entity.setId(IdentityListener.getLast("BolusFactor"));
                })
                .commit()
                .execute();
    }

    public void updateBolusFactor(BolusFactorEntity entity)
    {
        update("BolusFactor")
                             .set("startTime", entity.getStartTime())
                             .set("factor", entity.getFactor())
                             .where("bfID").equal(entity.getId())
                             .commit()
                             .execute();
    }

    public void insertBloodSugarValue(BloodSugarValueEntity entity)
    {
        insert().into("BloodSugarValue")
                .set("Timestamp", entity.getTimestamp())
                .set("BZ", entity.getBloodSugar())
                .onSuccess((stmt, i) ->
                {
                    entity.setId(IdentityListener.getLast("BloodSugarValue"));
                })
                .commit()
                .execute();
    }

    public void insertBolus(BolusEntity entity)
    {
        insert().into("Bolus")
                .set("BE", entity.getBe())
                .set("Factor", entity.getFactor())
                .set("BolusUnits", entity.getBolusUnits())
                .set("CorrectionUnits", entity.getCorrectionUnits())
                .onSuccess((stmt, i) ->
                {
                    entity.setId(IdentityListener.getLast("Bolus"));
                })
                .commit()
                .execute();
    }

    public void connectBloodSugarBolus(BloodSugarValueEntity bz, BolusEntity bo)
    {
        update("BloodSugarValue").set("BolusID", bo.getId())
                                 .where("bzID")
                                 .equal(bz.getId())
                                 .commit()
                                 .execute();
    }

    public List<BloodSugarValueEntity> selectBloodSugarValues()
    {
        List<BloodSugarValueEntity> list = new ArrayList<>();

        select().from("BloodSugarValue")
                .join("Bolus").left().on("BolusID").equal(new ColumnEntry("boID"))
                .onFail((stmt, e) ->
                {
                    Logger.global().print(e);
                    Logger.global().print(e.getSql());
                    return null;
                })
                .orderBy("Timestamp").desc()
                .executeAsStream().stream().forEach(result ->
                {
                    var bz = new BloodSugarValueEntity();

                    try
                    {
                        bz.setBloodSugar(result.getInt("bz"));
                        bz.setId(result.getLong("bzID"));
                        bz.setTimestamp(result.getLong("Timestamp"));

                        if (result.getLong("boID") != 0)
                        {
                            var bo = new BolusEntity();
                            bo.setId(result.getLong("boID"));
                            bo.setBe(result.getDouble("BE"));
                            bo.setBolusUnits(result.getDouble("BolusUnits"));
                            bo.setCorrectionUnits(result.getDouble("CorrectionUnits"));
                            bo.setFactor(result.getDouble("Factor"));

                            bz.setBolus(bo);
                        }
                    }
                    catch (SQLException e)
                    {
                        Logger.global().print(e);
                    }

                    list.add(bz);
                });

        return list;
    }
}