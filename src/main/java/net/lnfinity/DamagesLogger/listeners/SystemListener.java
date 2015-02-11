package net.lnfinity.DamagesLogger.listeners;

import net.lnfinity.DamagesLogger.DamagesLogger;
import net.lnfinity.DamagesLogger.player.DamageablePlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SystemListener implements Listener {

	private DamagesLogger p;
	
	public SystemListener(DamagesLogger plugin) {
		p = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		p.addSafePlayer(new DamageablePlayer(e.getPlayer()));
	}

}
