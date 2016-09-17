package me.cassayre.florian.damageslogger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.cassayre.florian.damageslogger.types.record.PlayerHealingRecord;
import me.cassayre.florian.damageslogger.types.record.PlayerDamagePlayerRecord;
import me.cassayre.florian.damageslogger.types.record.PlayerNaturalDamageRecord;
import me.cassayre.florian.damageslogger.types.record.core.ChangeRecord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class GameRecorder
{
    public enum RecordingMode
    {
        EVERYBODY,
        CURRENTLY_LOGGED_IN_ONLY
    }

    /**
     * Used in case the player disconnects.
     */
    private Map<UUID, Player> playersCache = new HashMap<>();

    /**
     * Used to record all changes that occur during the game.
     * victim.uuid -> list(damages_taken)
     */
    private Map<UUID, List<ChangeRecord>> changeRecords = new HashMap<>();

    /**
     * Contains all players currently alive.
     */
    private Set<UUID> alive = new HashSet<>();

    private final boolean diesOnDisconnect;
    private final RecordingMode recordingMode;
    private final boolean isBackup;
    private final int backupInterval;

    private boolean warningSent = false;

    private BukkitTask bukkitTask = null;

    private Map<UUID, PlayerHealingRecord.HealingType> lastHealingType = new HashMap<>();

    private final long COOLDOWN_PVP = 10L * 1000;
    private final long COOLDOWN_NATURAL = 10L * 1000;
    private final long COOLDOWN_HEALING = 3L * 1000;

    public GameRecorder()
    {
        this(false, RecordingMode.EVERYBODY, 0);
    }

    public GameRecorder(boolean diesOnDisconnect, RecordingMode recordingMode, int backupInterval)
    {
        this.diesOnDisconnect = diesOnDisconnect;
        this.recordingMode = recordingMode;
        this.isBackup = backupInterval > 0;
        this.backupInterval = backupInterval;

        for(Player player : Bukkit.getOnlinePlayers())
        {
            addPlayer(player);
        }

        bukkitTask = DamagesLogger.get().getServer().getScheduler().runTaskTimerAsynchronously(DamagesLogger.get(), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    exportToFile(null, true);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();

                    if(!warningSent)
                    {
                        warningSent = true;

                        for(Player player : DamagesLogger.get().getServer().getOnlinePlayers())
                        {
                            if(player.isOp())
                                player.sendMessage(ChatColor.DARK_RED + "Warning! " + ChatColor.RED + "The backup file couldn't be saved, please see the console. This message won't be sent for further failure(s).");
                        }
                    }
                }
            }
        }, backupInterval, backupInterval);
    }

    private void addPlayer(Player player)
    {
        if(playersCache.containsKey(player.getUniqueId()))
            throw new IllegalArgumentException();

        alive.add(player.getUniqueId());
        playersCache.put(player.getUniqueId(), player);
        changeRecords.put(player.getUniqueId(), new ArrayList<ChangeRecord>());
    }

    public void playerJoin(Player player)
    {
        if(recordingMode == RecordingMode.EVERYBODY)
            addPlayer(player);
    }

    public void playerQuit(Player player)
    {
        if(diesOnDisconnect)
        {
            addPlayerNaturalDamageRecord(player, player.getMaxHealth() - player.getHealth(), PlayerNaturalDamageRecord.DamageType.UNKNOWN, true);
            alive.remove(player.getUniqueId());
        }
    }

    public boolean isRecording(Player player)
    {
        return alive.contains(player.getUniqueId());
    }

    public void addPlayerDamagePlayerRecord(Player player, Player damager, double damages, PlayerDamagePlayerRecord.WeaponType weaponType, boolean isLethal)
    {
        if(!isRecording(player))
            return;

        if(isLethal)
            alive.remove(player.getUniqueId());

        final List<ChangeRecord> list = changeRecords.get(player.getUniqueId());

        if(!list.isEmpty()) // Has already damaged
        {
            ChangeRecord record = list.get(list.size() - 1);

            if(record instanceof PlayerDamagePlayerRecord)
            {
                PlayerDamagePlayerRecord pvpRecord = (PlayerDamagePlayerRecord) record;

                if(!pvpRecord.isLethal() && pvpRecord.getDamager().getUniqueId().equals(damager.getUniqueId()) && pvpRecord.getWeaponType() == weaponType)
                {
                    if(System.currentTimeMillis() - record.getUpdateDate() <= COOLDOWN_PVP)
                    {
                        pvpRecord.addPoints(damages, isLethal);

                        return;
                    }
                }
            }

            record.setEndDateLastUpdate();
        }
        // Player hasn't damaged yet

        PlayerDamagePlayerRecord record = new PlayerDamagePlayerRecord(player, damages, weaponType, damager, isLethal);

        list.add(record);
    }

    public void addPlayerNaturalDamageRecord(Player player, double damages, PlayerNaturalDamageRecord.DamageType damageType, boolean isLethal)
    {
        if(!isRecording(player))
            return;

        if(isLethal)
            alive.remove(player.getUniqueId());

        final List<ChangeRecord> list = changeRecords.get(player.getUniqueId());

        if(!list.isEmpty()) // Has already damaged
        {
            ChangeRecord record = list.get(list.size() - 1);

            if(record instanceof PlayerNaturalDamageRecord)
            {
                PlayerNaturalDamageRecord naturalRecord = (PlayerNaturalDamageRecord) record;

                if(!naturalRecord.isLethal() && naturalRecord.getDamageType() == damageType)
                {
                    if(System.currentTimeMillis() - record.getUpdateDate() <= COOLDOWN_NATURAL)
                    {
                        naturalRecord.addPoints(damages, isLethal);

                        return;
                    }
                }
            }

            record.setEndDateLastUpdate();
        }
        // Player hasn't damaged yet

        PlayerNaturalDamageRecord record = new PlayerNaturalDamageRecord(player, damages, damageType, isLethal);

        list.add(record);
    }

    public void addPlayerHealingRecord(Player player, double points)
    {
        if(!isRecording(player))
            return;

        PlayerHealingRecord.HealingType healingType = lastHealingType.get(player.getUniqueId());
        if(healingType == null)
            healingType = PlayerHealingRecord.HealingType.UNKNOWN;

        final List<ChangeRecord> list = changeRecords.get(player.getUniqueId());

        if(!list.isEmpty()) // Has already damaged
        {
            ChangeRecord record = list.get(list.size() - 1);

            if(record instanceof PlayerHealingRecord)
            {
                PlayerHealingRecord healingRecord = (PlayerHealingRecord) record;

                if(healingRecord.getHealingType() == healingType || (healingRecord.getHealingType() == PlayerHealingRecord.HealingType.GOLDEN_APPLE && healingType == PlayerHealingRecord.HealingType.HEALING_POTION))
                {
                    if(System.currentTimeMillis() - record.getUpdateDate() <= COOLDOWN_HEALING)
                    {
                        healingRecord.addPoints(points);

                        return;
                    }
                }
            }

            record.setEndDateLastUpdate();
        }
        // Player hasn't damaged yet

        PlayerHealingRecord record = new PlayerHealingRecord(player, points, healingType);

        list.add(record);
    }

    public void setLastHealingType(Player player, PlayerHealingRecord.HealingType healingType)
    {
        lastHealingType.put(player.getUniqueId(), healingType);
    }

    private ChangeRecord getLastChange(Player player, Class<? extends ChangeRecord> clazz)
    {
        final List<ChangeRecord> list = changeRecords.get(player.getUniqueId());

        for(int i = list.size() - 1; i >= 0; i--)
        {
            ChangeRecord record = list.get(i);
            if(clazz.isInstance(record))
                return record;
        }

        return null;
    }

    public List<Player> getCached()
    {
        return new ArrayList<>(playersCache.values());
    }

    public boolean isAlive(Player player)
    {
        return alive.contains(player.getUniqueId());
    }

    public Map<UUID, List<ChangeRecord>> getChangeRecords()
    {
        return new HashMap<>(changeRecords);
    }

    public boolean isBackup()
    {
        return isBackup;
    }

    public void stopBackup()
    {
        if(bukkitTask != null)
            bukkitTask.cancel();
    }

    public JsonObject toJson()
    {
        JsonObject object = new JsonObject();

        object.add("date", new JsonPrimitive(System.currentTimeMillis()));

        object.add("diesOnDisconnect", new JsonPrimitive(diesOnDisconnect));
        object.add("recordingMode", new JsonPrimitive(recordingMode.toString()));
        object.add("cooldown_pvp", new JsonPrimitive(COOLDOWN_PVP));
        object.add("cooldown_natural", new JsonPrimitive(COOLDOWN_NATURAL));
        object.add("cooldown_heal", new JsonPrimitive(COOLDOWN_HEALING));

        object.add("players", new JsonPrimitive(playersCache.size()));

        int size = 0;
        for(List<ChangeRecord> records : changeRecords.values())
            for(ChangeRecord record : records)
                size++;

        object.add("records_count", new JsonPrimitive(size));

        JsonArray array = new JsonArray();

        for(List<ChangeRecord> records : changeRecords.values())
        {
            for(ChangeRecord record : records)
            {
                array.add(record.toJson());
            }
        }

        object.add("records", array);

        return object;
    }

    public String exportToFile(String name, boolean backup) throws IOException // TODO hardcoded
    {
        PrintWriter writer;

        if(name == null)
        {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String formattedDate = sdf.format(date);
            name = "data_" + formattedDate;
        }

        File file = new File("tracker/" + name + ".json");
        File folder = new File("tracker");
        if(!folder.exists() && !folder.isDirectory())
        {
            if(!folder.mkdir())
            throw new IOException("Couldn't create folder " + folder.getName());
        }

        if(backup)
        {
            folder = new File("tracker/backup");
            if(!folder.exists() && !folder.isDirectory())
            {
                if(!folder.mkdir())
                    throw new IOException("Couldn't create folder " + folder.getName());
            }

            file = new File("tracker/backup/" + name + ".json");
        }

        if(!file.createNewFile())
            throw new IOException("Couldn't create file " + file.getName());

        writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");

        writer.print(toJson().toString());
        writer.flush();

        writer.close();

        return file.getAbsolutePath();
    }
}
