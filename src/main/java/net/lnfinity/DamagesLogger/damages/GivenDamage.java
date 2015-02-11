package net.lnfinity.DamagesLogger.damages;

import net.lnfinity.DamagesLogger.player.DamageablePlayer;
import net.lnfinity.DamagesLogger.player.DamageablePlayer.Weapon;

public class GivenDamage extends Damage {

	protected DamageablePlayer target;
	protected Weapon weapon;

	public GivenDamage(int h, DamageablePlayer target, Weapon w) {
		super(DamageType.PLAYER, h);
		this.target = target;
		weapon = w;
	}

	public DamageablePlayer getTarget() {
		return target;
	}

	public void setTarget(DamageablePlayer player) {
		this.target = player;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

}
