package net.lnfinity.DamagesLogger.damages;

import net.lnfinity.DamagesLogger.player.DamageablePlayer;
import net.lnfinity.DamagesLogger.player.DamageablePlayer.Weapon;

public class PlayerDamage extends Damage {

	protected DamageablePlayer player;
	protected Weapon weapon;

	public PlayerDamage(int h, DamageablePlayer player, Weapon w) {
		super(DamageType.PLAYER, h);
		this.player = player;
		weapon = w;
	}

	public DamageablePlayer getPlayer() {
		return player;
	}

	public void setPlayer(DamageablePlayer player) {
		this.player = player;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

}
