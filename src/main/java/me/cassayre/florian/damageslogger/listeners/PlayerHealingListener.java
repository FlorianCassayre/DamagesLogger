package me.cassayre.florian.damageslogger.listeners;

import me.cassayre.florian.damageslogger.DamagesLogger;
import me.cassayre.florian.damageslogger.GameRecorder;
import me.cassayre.florian.damageslogger.types.record.PlayerHealingRecord;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerHealingListener implements Listener
{
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e)
    {
        final GameRecorder recorder = DamagesLogger.get().getCurrentRecorder();
        if(recorder == null)
            return;

        if(e.getEntity() instanceof Player)
        {
            final Player player = (Player) e.getEntity();
            final double regain = e.getAmount();

            EntityRegainHealthEvent.RegainReason reason = e.getRegainReason();

            PlayerHealingRecord.HealingType type = PlayerHealingRecord.HealingType.UNKNOWN;

            recorder.addPlayerHealingRecord(player, regain);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e)
    {
        final GameRecorder recorder = DamagesLogger.get().getCurrentRecorder();
        if(recorder == null)
            return;

        final Material material = e.getItem().getType();
        final boolean isNotchApple = e.getItem().getDurability() == 1;

        final PlayerHealingRecord.HealingType healingType;

        if(material == Material.GOLDEN_APPLE)
        {
            if(isNotchApple)
                healingType = PlayerHealingRecord.HealingType.NOTCH_APPLE;
            else
                healingType = PlayerHealingRecord.HealingType.GOLDEN_APPLE;
        }
        else if(material == Material.POTION)
        {
            healingType = PlayerHealingRecord.HealingType.HEALING_POTION;
        }
        else
        {
            healingType = PlayerHealingRecord.HealingType.UNKNOWN;
        }

        recorder.setLastHealingType(e.getPlayer(), healingType);
    }
}
