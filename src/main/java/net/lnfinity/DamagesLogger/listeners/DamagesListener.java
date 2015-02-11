package net.lnfinity.DamagesLogger.listeners;

import net.lnfinity.DamagesLogger.DamagesLogger;
import net.lnfinity.DamagesLogger.damages.Damage;
import net.lnfinity.DamagesLogger.damages.Damage.DamageType;
import net.lnfinity.DamagesLogger.damages.GivenDamage;
import net.lnfinity.DamagesLogger.damages.PlayerDamage;
import net.lnfinity.DamagesLogger.player.DamageablePlayer;
import net.lnfinity.DamagesLogger.player.DamageablePlayer.Weapon;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// TODO Optimize these conditions

public class DamagesListener implements Listener {

	private DamagesLogger p;

	public DamagesListener(DamagesLogger plugin) {
		this.p = plugin;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && p.isRecord()) {
			DamageablePlayer player = p.getPlayer((Player) e.getEntity());
			if (e.getCause() == DamageCause.FALL) {
				player.addDamagesTaken(new Damage(DamageType.FALL, (int) e.getDamage()));
			} else if (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK
					|| e.getCause() == DamageCause.LAVA) {
				if (player.getLastDamageTaken() != null && player.getLastDamageTaken().getDamage() == DamageType.FIRE) {
					player.getLastDamageTaken().setHearths(
							player.getLastDamageTaken().getHearths() + (int) e.getDamage());
				} else {
					player.addDamagesTaken(new Damage(DamageType.FIRE, (int) e.getDamage()));
				}
			} else if (e.getCause() == DamageCause.SUFFOCATION) {
				if (player.getLastDamageTaken() != null
						&& player.getLastDamageTaken().getDamage() == DamageType.SUFFOCATION) {
					player.getLastDamageTaken().setHearths(
							player.getLastDamageTaken().getHearths() + (int) e.getDamage());
				} else {
					player.addDamagesTaken(new Damage(DamageType.SUFFOCATION, (int) e.getDamage()));
				}
			}
			player.setHealth((int) ((Damageable) e.getEntity()).getHealth());
		}

	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(!p.isRecord()) {
			return;
		}
		if (e.getEntity() instanceof Player) {
			DamageablePlayer player = p.getPlayer((Player) e.getEntity());
			if (e.getDamager() instanceof Player) {
				DamageablePlayer damager = p.getPlayer((Player) e.getDamager());
				Player damagerPlayer = (Player) e.getDamager();
				Weapon weapon;
				if (damagerPlayer.getItemInHand().getType() == Material.WOOD_SWORD) {
					weapon = Weapon.WOODEN_SWORD;
				} else if (damagerPlayer.getItemInHand().getType() == Material.STONE_SWORD) {
					weapon = Weapon.STONE_SWORD;
				} else if (damagerPlayer.getItemInHand().getType() == Material.GOLD_SWORD) {
					weapon = Weapon.GOLDEN_SWORD;
				} else if (damagerPlayer.getItemInHand().getType() == Material.IRON_SWORD) {
					weapon = Weapon.IRON_SWORD;
				} else if (damagerPlayer.getItemInHand().getType() == Material.DIAMOND_SWORD) {
					weapon = Weapon.DIAMOND_SWORD;
				} else {
					weapon = Weapon.OTHER;
				}

				// Damages taken
				if (player.getLastDamageTaken() != null && player.getLastDamageTaken() instanceof PlayerDamage
						&& ((PlayerDamage) player.getLastDamageTaken()).getPlayer().equals(damager)
						&& ((PlayerDamage) player.getLastDamageTaken()).getWeapon() == weapon) {
					player.getLastDamageTaken().setHearths(
							player.getLastDamageTaken().getHearths() + (int) e.getDamage());
				} else {
					player.addDamagesTaken(new PlayerDamage((int) e.getDamage(), damager, weapon));
				}

				// Damages given
				if (damager.getLastDamageGiven() != null && damager.getLastDamageGiven() instanceof GivenDamage
						&& ((GivenDamage) damager.getLastDamageGiven()).getTarget().equals(player)
						&& ((GivenDamage) damager.getLastDamageGiven()).getWeapon() == weapon) {
					damager.getLastDamageGiven().setHearths(
							damager.getLastDamageGiven().getHearths() + (int) e.getDamage());
				} else {
					damager.addDamagesGiven(new GivenDamage((int) e.getDamage(), player, weapon));
				}

				player.setHealth((int) ((Damageable) e.getEntity()).getHealth());
			} else if(e.getDamager() instanceof Zombie) {
				if(player.getLastDamageTaken() != null && player.getLastDamageTaken().getDamage() != DamageType.ZOMBIE) {
					player.addDamagesTaken(new Damage(DamageType.ZOMBIE, (int) e.getDamage()));
				} else {
					player.getLastDamageTaken().addHearths((int) e.getDamage());
				}
			} else if(e.getDamager() instanceof Skeleton) {
				if(player.getLastDamageTaken() != null && player.getLastDamageTaken().getDamage() != DamageType.SKELETON) {
					player.addDamagesTaken(new Damage(DamageType.SKELETON, (int) e.getDamage()));
				} else {
					player.getLastDamageTaken().addHearths((int) e.getDamage());
				}
			} else if(e.getDamager() instanceof Creeper) {
				if(player.getLastDamageTaken() != null && player.getLastDamageTaken().getDamage() != DamageType.CREEPER) {
					player.addDamagesTaken(new Damage(DamageType.CREEPER, (int) e.getDamage()));
				} else {
					player.getLastDamageTaken().addHearths((int) e.getDamage());
				}
			} else if(e.getDamager() instanceof Spider) {
				if(player.getLastDamageTaken() != null && player.getLastDamageTaken().getDamage() != DamageType.SPIDER) {
					player.addDamagesTaken(new Damage(DamageType.SPIDER, (int) e.getDamage()));
				} else {
					player.getLastDamageTaken().addHearths((int) e.getDamage());
				}
			}
		}
	}
}
