package me.cassayre.florian.damageslogger.types.record.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;

public abstract class HeartChangeRecord extends ChangeRecord
{
    protected double points;
    protected boolean isLethal;

    public HeartChangeRecord(Player player, double points)
    {
        this(player, points, false);
    }

    public HeartChangeRecord(Player player, double points, boolean isLethal)
    {
        super(player);

        this.points = points;
        this.isLethal = isLethal;

        if(isLethal)
        {
            endDate = System.currentTimeMillis();
            updateDate = endDate;
        }
    }

    public HeartChangeRecord(Player player, long startDate, long endDate, double points, boolean isLethal)
    {
        super(player, startDate, endDate);

        this.points = points;
        this.isLethal = isLethal;
    }

    public double getPoints()
    {
        return points;
    }

    public void addPoints(double points)
    {
        addPoints(points, false);
    }

    public void addPoints(double points, boolean isLethal)
    {
        this.points += points;

        if(isLethal)
            setEndDateNow();
        else
            update();
    }

    public boolean isLethal()
    {
        return isLethal;
    }

    @Override
    public JsonObject toJson()
    {
        JsonObject object = super.toJson();

        object.add("points", new JsonPrimitive(points));
        object.add("isLethal", new JsonPrimitive(isLethal));

        return object;
    }
}
