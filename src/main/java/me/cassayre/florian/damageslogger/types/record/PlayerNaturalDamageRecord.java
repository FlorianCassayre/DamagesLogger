package me.cassayre.florian.damageslogger.types.record;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.cassayre.florian.damageslogger.types.ImagesClass;
import me.cassayre.florian.damageslogger.types.record.core.HeartChangeRecord;
import org.bukkit.entity.Player;

public class PlayerNaturalDamageRecord extends HeartChangeRecord
{
    public enum DamageType
    {
        ZOMBIE("zombie", ImagesClass.MOB),
        SKELETON("skeleton", ImagesClass.MOB),
        PIGMAN("pigman", ImagesClass.MOB),
        SPIDER("spider", ImagesClass.MOB),
        SPIDER_BLUE("spider-blue", ImagesClass.MOB),
        CREEPER("creeper", ImagesClass.MOB),
        ENDERMAN("enderman", ImagesClass.MOB),
        SLIME("slime", ImagesClass.MOB),
        GHAST("ghast", ImagesClass.MOB),
        MAGMA_CUBE("magmacube", ImagesClass.MOB),
        BLAZE("blaze", ImagesClass.MOB),
        WOLF("wolf", ImagesClass.MOB),
        WOLF_ANGRY("wolf-angry", ImagesClass.MOB),
        SILVERFISH("silverfish", ImagesClass.MOB),
        IRON_GOLEM("irongolem", ImagesClass.MOB),
        ZOMBIE_VILLAGER("zombie-villager", ImagesClass.MOB),
        THUNDERBOLT("thunderbolt", ImagesClass.MOB),
        ENDER_DRAGON("enderdragon", ImagesClass.MOB),
        WITHER("wither", ImagesClass.MOB),
        WITHER_SKELETON("wither-skeleton", ImagesClass.MOB),

        FIRE("flintandsteel", ImagesClass.WEAPON),
        LAVA("lava", ImagesClass.WEAPON),
        CACTUS("cactus", ImagesClass.WEAPON),
        TNT("tnt", ImagesClass.WEAPON),
        FALL("fall", ImagesClass.WEAPON),
        SUFFOCATION("suffocation", ImagesClass.WEAPON),
        DROWNING("drowning", ImagesClass.WEAPON),
        STARVATION("starvation", ImagesClass.WEAPON),
        UNKNOWN("unknown-dark", ImagesClass.WEAPON);

        private final String CSS_CLASS_DAMAGE;
        private final ImagesClass CSS_CLASS_IMAGE;

        private DamageType(String cssClassDamage, ImagesClass cssClassImage)
        {
            CSS_CLASS_DAMAGE = cssClassDamage;
            CSS_CLASS_IMAGE = cssClassImage;
        }

        public String getCSSClassDamage()
        {
            return CSS_CLASS_DAMAGE;
        }

        public String getCSSClassImage()
        {
            return CSS_CLASS_IMAGE.getCSSClass();
        }
    }

    protected final DamageType damageType;

    public PlayerNaturalDamageRecord(Player player, double points)
    {
        this(player, points, DamageType.UNKNOWN);
    }

    public PlayerNaturalDamageRecord(Player player, double points, boolean isLethal)
    {
        this(player, points, DamageType.UNKNOWN, isLethal);
    }

    public PlayerNaturalDamageRecord(Player player, double points, DamageType damageType)
    {
        this(player, points, damageType, false);
    }

    public PlayerNaturalDamageRecord(Player player, double points, DamageType damageType, boolean isLethal)
    {
        super(player, points, isLethal);

        this.damageType = damageType;
    }

    public PlayerNaturalDamageRecord(Player player, long startDate, long endDate, double points, DamageType damageType, boolean isLethal)
    {
        super(player, startDate, endDate, points, isLethal);

        this.damageType = damageType;
    }

    public DamageType getDamageType()
    {
        return damageType;
    }

    @Override
    public JsonObject toJson()
    {
        JsonObject object = super.toJson();

        object.add("type", new JsonPrimitive("natural"));

        object.add("damage", new JsonPrimitive(damageType.toString()));

        JsonObject icon = new JsonObject();
        icon.add("image", new JsonPrimitive(damageType.getCSSClassImage()));
        icon.add("id", new JsonPrimitive(damageType.getCSSClassDamage()));

        object.add("icon", icon);

        return object;
    }
}
