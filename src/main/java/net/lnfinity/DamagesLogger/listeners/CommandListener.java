package net.lnfinity.DamagesLogger.listeners;

import net.lnfinity.DamagesLogger.DamagesLogger;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListener implements CommandExecutor {

	private DamagesLogger p;
	
	public CommandListener(DamagesLogger plugin) {
		p = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
		if(args[0].equals("start")) {
			p.setRecord(true);
			sender.sendMessage(ChatColor.GREEN + "Recording players damages");
		} else if(args[0].equals("stop")) {
			p.setRecord(false);
			sender.sendMessage(ChatColor.RED + "Stopping the recording");
			p.exportToFile();
		}
		return true;
	}

}
