package me.cassayre.florian.damageslogger;

import me.cassayre.florian.damageslogger.commands.MainCommandExecutor;
import me.cassayre.florian.damageslogger.listeners.PlayerConnectionListener;
import me.cassayre.florian.damageslogger.listeners.PlayerDamageNaturalListener;
import me.cassayre.florian.damageslogger.listeners.PlayerDamagePlayerListener;
import me.cassayre.florian.damageslogger.listeners.PlayerHealingListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DamagesLogger extends JavaPlugin
{
    private static DamagesLogger instance = null;

    private GameRecorder currentRecorder = null;

    @Override
    public void onEnable()
    {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamageNaturalListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamagePlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHealingListener(), this);

        this.getServer().getPluginCommand("damageslogger").setExecutor(new MainCommandExecutor());
    }

    @Override
    public void onDisable()
    {

    }

    public static DamagesLogger get()
    {
        if(instance == null)
            throw new IllegalStateException();

        return instance;
    }

    public GameRecorder getCurrentRecorder()
    {
        return currentRecorder;
    }

    public void setCurrentRecorder(GameRecorder currentRecorder)
    {
        this.currentRecorder = currentRecorder;
    }
}
