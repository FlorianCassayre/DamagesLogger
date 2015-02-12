package net.lnfinity.DamagesLogger.listeners;

import java.io.IOException;

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
		if(sender.isOp()) {
		if(args[0].equals("start")) {
			p.setRecord(true);
			sender.sendMessage(ChatColor.GREEN + "Recording players damages");
		} else if(args[0].equals("stop")) {
			p.setRecord(false);
			sender.sendMessage(ChatColor.RED + "Stopping the recording");
			sender.sendMessage(ChatColor.GRAY + "Saving the file...");
			try {
				sender.sendMessage(ChatColor.GRAY + "File saved ! " + ChatColor.WHITE + "(" + ChatColor.UNDERLINE + p.exportToFile() + ChatColor.RESET + ")" + ChatColor.GRAY);
			} catch (IOException e) {
				sender.sendMessage(ChatColor.RED + "Error while saving the file. Stack trace on logger.");
				e.printStackTrace();
			}
		}
		return true;
		} else {
			return false;
		}
	}

}
