package me.cassayre.florian.damageslogger.types.record;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.cassayre.florian.damageslogger.types.ImagesClass;
import me.cassayre.florian.damageslogger.types.record.core.HeartChangeRecord;
import org.bukkit.entity.Player;

public class PlayerDamagePlayerRecord extends HeartChangeRecord
{
    public enum WeaponType
    {
        SWORD_WOOD("sword-wood"),
        SWORD_GOLD("sword-gold"),
        SWORD_STONE("sword-stone"),
        SWORD_IRON("sword-iron"),
        SWORD_DIAMOND("sword-diamond"),
        AXE_WOOD("axe-wood"),
        AXE_GOLD("axe-gold"),
        AXE_STONE("axe-stone"),
        AXE_IRON("axe-iron"),
        AXE_DIAMOND("axe-diamond"),
        BOW("bow"),
        MAGIC("magic"),
        FISTS(""),
        THORNS("cactus");

        private final static ImagesClass CSS_CLASS_IMAGE = ImagesClass.WEAPON;

        private final String CSS_CLASS_WEAPON;

        private WeaponType(String cssClassWeapon)
        {
            CSS_CLASS_WEAPON = cssClassWeapon;
        }

        public String getCSSClassImage()
        {
            return CSS_CLASS_IMAGE.getCSSClass();
        }

        public String getCSSClassWeapon()
        {
            return CSS_CLASS_WEAPON;
        }
    }

    private final WeaponType weaponType;
    private final Player damager;

    public PlayerDamagePlayerRecord(Player player, double points, WeaponType weaponType, Player damager)
    {
        this(player, points, weaponType, damager, false);
    }

    public PlayerDamagePlayerRecord(Player player, double points, WeaponType weaponType, Player damager, boolean isLethal)
    {
        super(player, points, isLethal);

        this.weaponType = weaponType;
        this.damager = damager;
    }

    public PlayerDamagePlayerRecord(Player player, long startDate, long endDate, double points, WeaponType weaponType, Player damager, boolean isLethal)
    {
        super(player, startDate, endDate, points, isLethal);

        this.weaponType = weaponType;
        this.damager = damager;
    }

    public WeaponType getWeaponType()
    {
        return weaponType;
    }

    public Player getDamager()
    {
        return damager;
    }

    @Override
    public JsonObject toJson()
    {
        JsonObject object = super.toJson();

        object.add("type", new JsonPrimitive("pvp"));

        JsonObject d = new JsonObject();
        d.add("uuid", new JsonPrimitive(damager.getUniqueId().toString()));
        d.add("name", new JsonPrimitive(damager.getName()));

        object.add("damager", d);

        object.add("weapon", new JsonPrimitive(weaponType.toString()));

        JsonObject icon = new JsonObject();
        icon.add("image", new JsonPrimitive(weaponType.getCSSClassImage()));
        icon.add("id", new JsonPrimitive(weaponType.getCSSClassWeapon()));

        object.add("icon", icon);

        return object;
    }
}
