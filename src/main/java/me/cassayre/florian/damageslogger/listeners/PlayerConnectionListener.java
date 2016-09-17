package me.cassayre.florian.damageslogger.listeners;

import me.cassayre.florian.damageslogger.DamagesLogger;
import me.cassayre.florian.damageslogger.GameRecorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        final GameRecorder recorder = DamagesLogger.get().getCurrentRecorder();
        if(recorder == null)
            return;

        recorder.playerJoin(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        final GameRecorder recorder = DamagesLogger.get().getCurrentRecorder();
        if(recorder == null)
            return;

        recorder.playerQuit(e.getPlayer());
    }
}
