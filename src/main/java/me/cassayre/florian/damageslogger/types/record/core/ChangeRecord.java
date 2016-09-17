package me.cassayre.florian.damageslogger.types.record.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;

public abstract class ChangeRecord
{
    protected final Player player;
    protected long startDate, endDate, updateDate;

    public ChangeRecord(Player player)
    {
        this.player = player;

        this.startDate = System.currentTimeMillis();
        updateDate = startDate;
    }

    public ChangeRecord(Player player, long startDate, long endDate)
    {
        this.player = player;

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Player getPlayer()
    {
        return player;
    }

    public long getStartDate()
    {
        return startDate;
    }

    public long getEndDate()
    {
        if(endDate == 0)
            throw new IllegalStateException("setEndDate() was not called!");

        return endDate;
    }

    public boolean isEnded()
    {
        return endDate != 0;
    }

    public void setEndDateNow()
    {
        if(isEnded())
            throw new IllegalStateException("setEndDate() as already been called!");

        endDate = System.currentTimeMillis();
        updateDate = endDate;
    }

    public void setEndDateLastUpdate()
    {
        if(isEnded())
            throw new IllegalStateException("setEndDate() as already been called!");

        endDate = updateDate;
    }

    public long getUpdateDate()
    {
        return updateDate;
    }

    protected void update()
    {
        if(isEnded())
            throw new IllegalStateException("setEndDate() as already been called!");

        updateDate = System.currentTimeMillis();
    }

    public JsonObject toJson()
    {
        JsonObject object = new JsonObject();

        JsonObject p = new JsonObject();
        p.add("uuid", new JsonPrimitive(player.getUniqueId().toString()));
        p.add("name", new JsonPrimitive(player.getName()));

        object.add("player", p);

        JsonObject dates = new JsonObject();
        dates.add("start", new JsonPrimitive(startDate));
        dates.add("end", new JsonPrimitive(endDate));

        object.add("dates", dates);

        return object;
    }
}
