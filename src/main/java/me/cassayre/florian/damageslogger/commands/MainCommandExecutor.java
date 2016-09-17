package me.cassayre.florian.damageslogger.commands;

import me.cassayre.florian.damageslogger.DamagesLogger;
import me.cassayre.florian.damageslogger.GameRecorder;
import me.cassayre.florian.damageslogger.types.record.PlayerDamagePlayerRecord;
import me.cassayre.florian.damageslogger.types.record.PlayerHealingRecord;
import me.cassayre.florian.damageslogger.types.record.PlayerNaturalDamageRecord;
import me.cassayre.florian.damageslogger.types.record.core.ChangeRecord;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class MainCommandExecutor implements CommandExecutor
{
    private final static String[] HELP = {"?", "help"};
    private final static String START = "start";
    private final static String STOP = "stop";
    private final static String INFO = "info";

    private static List<String> HELP_MENU = new ArrayList<>();

    static
    {
        addHelpMessage(HELP[0], "Displays this message");
        addHelpMessage(START, "Starts the recording");
        addHelpMessage(STOP + " [file name]", "Stops the recording and saves it");
        addHelpMessage(INFO, "Displays information about the current recording");
    }

    private static void addHelpMessage(String cmd, String info)
    {
        HELP_MENU.add(ChatColor.YELLOW + "/dl " + ChatColor.GOLD + cmd + ChatColor.GREEN + " -> " + ChatColor.DARK_GREEN + info);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        DamagesLogger instance = DamagesLogger.get();

        if(args != null && args.length > 0)
        {
            if(Arrays.asList(HELP).contains(args[0]))
            {
                for(String str : HELP_MENU)
                    sender.sendMessage(str);

            }
            else if(args[0].equals(START))
            {
                if(instance.getCurrentRecorder() == null)
                {
                    sender.sendMessage(ChatColor.GREEN + "Recording has been started.");
                    instance.setCurrentRecorder(new GameRecorder(false, GameRecorder.RecordingMode.EVERYBODY, 20 * 60 * 1));
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "DamagesLogger is still recording. Use \"/dl stop\" to stop the recording.");
                }
            }
            else if(args[0].equals(STOP))
            {
                if(instance.getCurrentRecorder() != null)
                {
                    sender.sendMessage(ChatColor.GREEN + "Compiling data...");

                    String name = null;

                    if(args.length >= 2)
                        name = args[1];

                    try
                    {
                        String path = instance.getCurrentRecorder().exportToFile(name, false);
                        sender.sendMessage(ChatColor.GREEN + "Log successfully saved to '" + path + "'!");
                        instance.getCurrentRecorder().stopBackup();
                        instance.setCurrentRecorder(null);
                    }
                    catch(IOException ex)
                    {
                        sender.sendMessage(ChatColor.RED + "Error whilst saving data. You may find the stack trace in the console. The recorder was not stopped.");
                        ex.printStackTrace();
                    }
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "No damages recorder is currently running. Use \"/dl start\" to start a recording.");
                }
            }
            else if(args[0].equals(INFO))
            {
                if(instance.getCurrentRecorder() != null)
                {
                    sender.sendMessage(ChatColor.GOLD + "------------------------");

                    final String separator = ChatColor.WHITE + ", ";
                    final String endSeparator = ChatColor.WHITE + " and ";
                    final String end = ChatColor.WHITE + ".";

                    List<String> names = new ArrayList<>();
                    for(Player player : instance.getCurrentRecorder().getCached())
                    if(instance.getCurrentRecorder().isAlive(player))
                        names.add(ChatColor.GREEN + player.getName());
                    else
                        names.add(ChatColor.RED + player.getName());

                    StringBuilder builder = new StringBuilder();
                    for(int i = 0; i < names.size(); i++)
                    {
                        builder.append(names.get(i));
                        if(i == names.size() - 2)
                            builder.append(endSeparator);
                        else if(i < names.size() - 1)
                            builder.append(separator);
                    }
                    builder.append(end);

                    sender.sendMessage(ChatColor.YELLOW + "Players: " + builder.toString());

                    Map<UUID, List<ChangeRecord>> recordsMap = instance.getCurrentRecorder().getChangeRecords();
                    int pvp = 0, natural = 0, healing = 0;
                    for(List<ChangeRecord> list : recordsMap.values())
                    {
                        if(list == null)
                            continue;
                        for(ChangeRecord changeRecord : list)
                        {
                            if(changeRecord instanceof PlayerDamagePlayerRecord)
                                pvp++;
                            else if(changeRecord instanceof PlayerNaturalDamageRecord)
                                natural++;
                            else if(changeRecord instanceof PlayerHealingRecord)
                                healing++;
                        }
                    }

                    sender.sendMessage(ChatColor.YELLOW + "Record counts:");
                    sender.sendMessage(ChatColor.DARK_RED + " Players damaged by players: " + ChatColor.WHITE + pvp);
                    sender.sendMessage(ChatColor.RED + " Natural damages: " + ChatColor.WHITE + natural);
                    sender.sendMessage(ChatColor.DARK_GREEN + " Healed: " + ChatColor.WHITE + healing);
                    sender.sendMessage(ChatColor.YELLOW + "With backup: " + (instance.getCurrentRecorder().isBackup() ? (ChatColor.DARK_GREEN + "true") : ChatColor.RED + "false"));

                    sender.sendMessage(ChatColor.GOLD + "------------------------");
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "No damages recorder is currently running. Use \"/dl start\" to start a recording.");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Unknown argument. Enter \"dl ?\" for help.");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "No arguments specified. Enter \"dl ?\" for help.");
        }

        return true;
    }
}
