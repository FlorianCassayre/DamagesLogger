package me.cassayre.florian.damageslogger.listeners;

import me.cassayre.florian.damageslogger.DamagesLogger;
import me.cassayre.florian.damageslogger.GameRecorder;
import me.cassayre.florian.damageslogger.types.record.PlayerDamagePlayerRecord;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamagePlayerListener implements Listener
{
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        final GameRecorder recorder = DamagesLogger.get().getCurrentRecorder();
        if(recorder == null)
            return;

        if(e.getEntity() instanceof Player)
        {
            final Player player = (Player) e.getEntity();
            final double damages = e.getFinalDamage();

            final boolean isLethal = player.getHealth() - damages <= 0;

            if(e.getDamager() instanceof Player) // PvP
            {
                final Player damager = (Player) e.getDamager();

                if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                {
                    final Material weapon = damager.getItemInHand().getType();
                    PlayerDamagePlayerRecord.WeaponType type;

                    switch(weapon)
                    {
                        case WOOD_SWORD: type = PlayerDamagePlayerRecord.WeaponType.SWORD_WOOD; break;
                        case GOLD_SWORD: type = PlayerDamagePlayerRecord.WeaponType.SWORD_GOLD; break;
                        case STONE_SWORD: type = PlayerDamagePlayerRecord.WeaponType.SWORD_STONE; break;
                        case IRON_SWORD: type = PlayerDamagePlayerRecord.WeaponType.SWORD_IRON; break;
                        case DIAMOND_SWORD: type = PlayerDamagePlayerRecord.WeaponType.SWORD_DIAMOND; break;
                        case WOOD_AXE: type = PlayerDamagePlayerRecord.WeaponType.AXE_WOOD; break;
                        case GOLD_AXE: type = PlayerDamagePlayerRecord.WeaponType.AXE_GOLD; break;
                        case STONE_AXE: type = PlayerDamagePlayerRecord.WeaponType.AXE_STONE; break;
                        case IRON_AXE: type = PlayerDamagePlayerRecord.WeaponType.AXE_IRON; break;
                        case DIAMOND_AXE: type = PlayerDamagePlayerRecord.WeaponType.AXE_DIAMOND; break;
                        default: type = PlayerDamagePlayerRecord.WeaponType.FISTS;
                    }

                    recorder.addPlayerDamagePlayerRecord(player, damager, damages, type, isLethal);
                }
                else if(e.getCause() == EntityDamageEvent.DamageCause.THORNS)
                {
                    final PlayerDamagePlayerRecord.WeaponType type = PlayerDamagePlayerRecord.WeaponType.THORNS;

                    recorder.addPlayerDamagePlayerRecord(player, damager, damages, type, isLethal);
                }


            }
            else if(e.getDamager() instanceof Arrow)
            {
                final Arrow arrow = (Arrow) e.getDamager();

                if(arrow.getShooter() instanceof Player)
                {
                    final Player damager = (Player) arrow.getShooter();

                    final PlayerDamagePlayerRecord.WeaponType type = PlayerDamagePlayerRecord.WeaponType.BOW;

                    recorder.addPlayerDamagePlayerRecord(player, damager, damages, type, isLethal);
                }
            }
            else if(e.getDamager() instanceof ThrownPotion)
            {
                final ThrownPotion potion = (ThrownPotion) e.getDamager();

                if(potion.getShooter() instanceof Player)
                {
                    final Player damager = (Player) potion.getShooter();

                    final PlayerDamagePlayerRecord.WeaponType type = PlayerDamagePlayerRecord.WeaponType.MAGIC;

                    recorder.addPlayerDamagePlayerRecord(player, damager, damages, type, isLethal);
                }
            }
        }
    }
}
