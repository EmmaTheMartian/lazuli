package top.girlkisser.lazuli.api.energy;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * An NBT serializable energy storage.
 */
public interface ISerializableEnergyStorage extends IEnergyStorage, INBTSerializable<Tag>
{
}
