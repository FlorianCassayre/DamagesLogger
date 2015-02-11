package net.lnfinity.DamagesLogger.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.lnfinity.DamagesLogger.damages.Damage;
import net.lnfinity.DamagesLogger.damages.GivenDamage;

import org.bukkit.entity.Player;

public class DamageablePlayer {

	public enum Weapon {
		WOODEN_SWORD, GOLDEN_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD, BOW, OTHER;
	}

	private List<Damage> damagesTaken = new ArrayList<Damage>();
	private List<GivenDamage> damagesGiven = new ArrayList<GivenDamage>();
	private int health;
	private boolean dead = false;
	private UUID id;

	public DamageablePlayer(Player player) {
		id = player.getUniqueId();
	}

	public UUID getId() {
		return id;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public List<Damage> getDamagesTaken() {
		return damagesTaken;
	}

	public void addDamagesTaken(Damage d) {
		damagesTaken.add(d);
	}

	public Damage getLastDamageTaken() {
		if (damagesTaken.size() > 0) {
			return damagesTaken.get(damagesTaken.size() - 1);
		}
		return null;
	}

	public void setLastDamageTaken(Damage d) {
		if (damagesTaken.size() > 0) {
			damagesTaken.set(damagesTaken.size() - 1, d);
		}
	}

	public List<GivenDamage> getDamagesGiven() {
		return damagesGiven;
	}

	public void addDamagesGiven(GivenDamage d) {
		damagesGiven.add(d);
	}

	public Damage getLastDamageGiven() {
		if (damagesTaken.size() > 0) {
			return damagesTaken.get(damagesTaken.size() - 1);
		}
		return null;
	}

	public void setLastDamageGiven(Damage d) {
		if (damagesTaken.size() > 0) {
			damagesTaken.set(damagesTaken.size() - 1, d);
		}
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

}
