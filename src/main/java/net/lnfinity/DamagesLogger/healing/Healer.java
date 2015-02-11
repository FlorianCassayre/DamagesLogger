package net.lnfinity.DamagesLogger.healing;

public class Healer {

	public enum HealingType {
		GOLDEN_APPLE, GOD_APPLE, HEALING_POTION;
	}

	private int hearths;
	private HealingType healingType;

	public Healer() {

	}

	public int getHearths() {
		return hearths;
	}

	public void setHearths(int hearths) {
		this.hearths = hearths;
	}

	public HealingType getHealingType() {
		return healingType;
	}

	public void setHealingType(HealingType healingType) {
		this.healingType = healingType;
	}

}