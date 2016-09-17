package me.cassayre.florian.damageslogger.listeners;

import me.cassayre.florian.damageslogger.DamagesLogger;
import me.cassayre.florian.damageslogger.GameRecorder;
import me.cassayre.florian.damageslogger.types.record.PlayerNaturalDamageRecord;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageNaturalListener implements Listener
{
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e)
    {
        final GameRecorder recorder = DamagesLogger.get().getCurrentRecorder();
        if(recorder == null)
            return;

        if(e.getEntity() instanceof Player)
        {
            final Player player = (Player) e.getEntity();
            final double damages = e.getFinalDamage();

            final boolean isLethal = player.getHealth() - damages <= 0;

            final EntityDamageEvent.DamageCause cause = e.getCause();

            if(e instanceof EntityDamageByEntityEvent)
            {
                EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;

                final Entity damager = event.getDamager();

                PlayerNaturalDamageRecord.DamageType type = PlayerNaturalDamageRecord.DamageType.UNKNOWN;

                if(damager instanceof Zombie)
                {
                    final Zombie zombie = (Zombie) damager;

                    if(zombie instanceof PigZombie)
                    {
                        type = PlayerNaturalDamageRecord.DamageType.PIGMAN;
                    }
                    else
                    {
                        if(zombie.isVillager())
                            type = PlayerNaturalDamageRecord.DamageType.ZOMBIE_VILLAGER;
                        else
                            type = PlayerNaturalDamageRecord.DamageType.ZOMBIE;
                    }
                }
                else if(damager instanceof Skeleton)
                {
                    final Skeleton skeleton = (Skeleton) damager;

                    if(skeleton.getSkeletonType() == Skeleton.SkeletonType.NORMAL) // Might be possible in some special cases...
                        type = PlayerNaturalDamageRecord.DamageType.SKELETON;
                    else
                        type = PlayerNaturalDamageRecord.DamageType.WITHER_SKELETON;
                }
                else if(damager instanceof Arrow)
                {
                    final Arrow arrow = (Arrow) damager;

                    if(arrow.getShooter() instanceof Skeleton)
                        type = PlayerNaturalDamageRecord.DamageType.SKELETON;
                }
                else if(damager instanceof Spider)
                {
                    final Spider spider = (Spider) damager;

                    if(spider instanceof CaveSpider)
                        type = PlayerNaturalDamageRecord.DamageType.SPIDER_BLUE;
                    else
                        type = PlayerNaturalDamageRecord.DamageType.SPIDER;
                }
                else if(damager instanceof Creeper)
                {
                    type = PlayerNaturalDamageRecord.DamageType.CREEPER;
                }
                else if(damager instanceof Enderman)
                {
                    type = PlayerNaturalDamageRecord.DamageType.ENDERMAN;
                }
                else if(damager instanceof Slime)
                {
                    final Slime slime = (Slime) damager;

                    if(slime instanceof MagmaCube)
                        type = PlayerNaturalDamageRecord.DamageType.MAGMA_CUBE;
                    else
                        type = PlayerNaturalDamageRecord.DamageType.SLIME;
                }
                else if(damager instanceof Ghast)
                {
                    type = PlayerNaturalDamageRecord.DamageType.GHAST;
                }
                else if(damager instanceof Blaze)
                {
                    type = PlayerNaturalDamageRecord.DamageType.BLAZE;
                }
                else if(damager instanceof Fireball) // Fireballs
                {
                    final Fireball fireball = (Fireball) damager;

                    if(fireball.getShooter() instanceof Blaze)
                    {
                        type = PlayerNaturalDamageRecord.DamageType.BLAZE;
                    }
                    else if(fireball.getShooter() instanceof Ghast)
                    {
                        type = PlayerNaturalDamageRecord.DamageType.GHAST;
                    }
                }
                else if(damager instanceof Wolf)
                {
                    final Wolf wolf = (Wolf) damager;

                    if(wolf.isAngry())
                        type = PlayerNaturalDamageRecord.DamageType.WOLF_ANGRY;
                    else
                        type = PlayerNaturalDamageRecord.DamageType.WOLF; // Don't ask me how
                }
                else if(damager instanceof Silverfish)
                {
                    type = PlayerNaturalDamageRecord.DamageType.SILVERFISH;
                }
                else if(damager instanceof IronGolem)
                {
                    type = PlayerNaturalDamageRecord.DamageType.IRON_GOLEM;
                }
                else if(damager instanceof LightningStrike)
                {
                    type = PlayerNaturalDamageRecord.DamageType.THUNDERBOLT;
                }
                else if(damager instanceof EnderDragon)
                {
                    type = PlayerNaturalDamageRecord.DamageType.ENDER_DRAGON; // Let's just hope for it
                }
                else if(damager instanceof Wither)
                {
                    type = PlayerNaturalDamageRecord.DamageType.WITHER;
                }
                else if(damager instanceof TNTPrimed)
                {
                    type = PlayerNaturalDamageRecord.DamageType.TNT;
                }

                if(!(damager instanceof Player))
                    recorder.addPlayerNaturalDamageRecord(player, damages, type, isLethal);
            }
            else
            {
                PlayerNaturalDamageRecord.DamageType type = PlayerNaturalDamageRecord.DamageType.UNKNOWN;

                if(cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
                {
                    type = PlayerNaturalDamageRecord.DamageType.FIRE;
                }
                else if(cause == EntityDamageEvent.DamageCause.LAVA)
                {
                    type = PlayerNaturalDamageRecord.DamageType.LAVA;
                }
                else if(cause == EntityDamageEvent.DamageCause.CONTACT)
                {
                    type = PlayerNaturalDamageRecord.DamageType.CACTUS;
                }
                else if(cause == EntityDamageEvent.DamageCause.FALL)
                {
                    type = PlayerNaturalDamageRecord.DamageType.FALL;
                }
                else if(cause == EntityDamageEvent.DamageCause.SUFFOCATION || cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) // Separate FALLING_BLOCK?
                {
                    type = PlayerNaturalDamageRecord.DamageType.SUFFOCATION;
                }
                else if(cause == EntityDamageEvent.DamageCause.DROWNING)
                {
                    type = PlayerNaturalDamageRecord.DamageType.DROWNING;
                }
                else if(cause == EntityDamageEvent.DamageCause.STARVATION)
                {
                    type = PlayerNaturalDamageRecord.DamageType.STARVATION;
                }
                else if(cause == EntityDamageEvent.DamageCause.WITHER)
                {
                    // TODO Check for latest damage
                }
                else if(cause == EntityDamageEvent.DamageCause.POISON)
                {
                    type = PlayerNaturalDamageRecord.DamageType.UNKNOWN; // FIXME
                }

                recorder.addPlayerNaturalDamageRecord(player, damages, type, isLethal);
            }
        }
    }
}
