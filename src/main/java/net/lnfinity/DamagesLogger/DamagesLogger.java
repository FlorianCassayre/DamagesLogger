package net.lnfinity.DamagesLogger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.lnfinity.DamagesLogger.damages.PlayerDamage;
import net.lnfinity.DamagesLogger.listeners.CommandListener;
import net.lnfinity.DamagesLogger.listeners.DamagesListener;
import net.lnfinity.DamagesLogger.listeners.SystemListener;
import net.lnfinity.DamagesLogger.player.DamageablePlayer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DamagesLogger extends JavaPlugin {

	private boolean record = false;
	private List<DamageablePlayer> players = new ArrayList<DamageablePlayer>();

	public DamagesLogger() {

	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new DamagesListener(this), this);
		getServer().getPluginManager().registerEvents(new SystemListener(this), this);

		this.getCommand("dl").setExecutor(new CommandListener(this));

		for (Player player : this.getServer().getOnlinePlayers()) {
			addSafePlayer(new DamageablePlayer(player));
		}
	}

	@Override
	public void onDisable() {

	}

	public boolean isRecord() {
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public void addSafePlayer(DamageablePlayer player) {
		if (!hasPlayer(this.getServer().getPlayer(player.getId()))) {
			players.add(player);
		}
	}

	public boolean hasPlayer(Player player) {
		if (getPlayer(player) != null) {
			return true;
		} else {
			return false;
		}
	}

	public DamageablePlayer getPlayer(Player player) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId() == player.getUniqueId()) {
				return players.get(i);
			}
		}
		return null;
	}

	public void exportToFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("logs.txt", "UTF-8");
			writer.println("<report>");
			for (int i = 0; i < players.size(); i++) {
				writer.println(" <player name='" + this.getServer().getPlayer(players.get(i).getId()).getName()
						+ "' hp='" + players.get(i).getHealth() + "'>");
				writer.println("  <damagestaken>");
				for (int t = 0; t < players.get(i).getDamagesTaken().size(); t++) {
					String attribute = "";
					if (players.get(i).getDamagesTaken().get(t) instanceof PlayerDamage) {
						attribute = " damager='"
								+ this.getServer()
										.getPlayer(
												((PlayerDamage) players.get(i).getDamagesTaken().get(t)).getPlayer()
														.getId()).getName() + "'";
					}
					writer.println("   <damage type='"
							+ players.get(i).getDamagesTaken().get(t).getDamage().toString().toLowerCase() + "' hp='"
							+ players.get(i).getDamagesTaken().get(t).getHearths() + "'" + attribute + " />");
				}
				writer.println("  </damagestaken>");
				writer.println(" </player>");
			}
			writer.println("</report>");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
