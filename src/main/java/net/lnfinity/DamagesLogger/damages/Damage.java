package net.lnfinity.DamagesLogger.damages;

public class Damage {

	public enum DamageType {
		FALL, ZOMBIE, SKELETON, CREEPER, SPIDER, CAVE_SPIDER, ENDERMAN, SILVERFISH, FIRE, SUFFOCATION, PLAYER, OTHER, DEATH;
	}

	protected DamageType damage = DamageType.OTHER;
	protected int hearths;
	protected boolean toDeath = false;

	public Damage(DamageType type, int h) {
		damage = type;
		hearths = h;
	}

	public Damage(DamageType type, int h, boolean death) {
		damage = type;
		hearths = h;
		toDeath = death;
	}

	public DamageType getDamage() {
		return damage;
	}

	public void setDamage(DamageType damage) {
		this.damage = damage;
	}

	public int getHearths() {
		return hearths;
	}

	public void setHearths(int hearths) {
		this.hearths = hearths;
	}
	
	public void addHearths(int hearths) {
		this.hearths += hearths;
	}

	public boolean isToDeath() {
		return toDeath;
	}

	public void setToDeath(boolean toDeath) {
		this.toDeath = toDeath;
	}

}
