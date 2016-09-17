package me.cassayre.florian.damageslogger.types.record;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.cassayre.florian.damageslogger.types.ImagesClass;
import me.cassayre.florian.damageslogger.types.record.core.HeartChangeRecord;
import org.bukkit.entity.Player;

public class PlayerHealingRecord extends HeartChangeRecord
{
	public enum HealingType
    {
		GOLDEN_APPLE("golden-apple"),
        NOTCH_APPLE("notch-apple"),
        HEALING_POTION("healing-potion"),
        UNKNOWN("");

        private final static ImagesClass CSS_CLASS_IMAGE = ImagesClass.HEALER;

        private final String CSS_CLASS_HEALER;

        private HealingType(String cssClassHealer)
        {
            CSS_CLASS_HEALER = cssClassHealer;
        }

        public String getCSSClassImage()
        {
            return CSS_CLASS_IMAGE.getCSSClass();
        }

        public String getCSSClassHealer()
        {
            return CSS_CLASS_HEALER;
        }
	}

	protected final HealingType healingType;

	public PlayerHealingRecord(Player player, double points, HealingType healingType)
    {
        super(player, points);

        this.healingType = healingType;
	}

    public PlayerHealingRecord(Player player, long startDate, long endDate, double points, HealingType healingType)
    {
        super(player, startDate, endDate, points, false);

        this.healingType = healingType;
    }

	public HealingType getHealingType()
    {
		return healingType;
	}

    @Override
    public boolean isLethal()
    {
        return false;
    }

    @Override
    public JsonObject toJson()
    {
        JsonObject object = super.toJson();

        object.add("type", new JsonPrimitive("heal"));

        object.add("healing", new JsonPrimitive(healingType.toString()));

        JsonObject icon = new JsonObject();
        icon.add("image", new JsonPrimitive(healingType.getCSSClassImage()));
        icon.add("id", new JsonPrimitive(healingType.getCSSClassHealer()));

        object.add("icon", icon);

        return object;
    }
}