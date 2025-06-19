package top.girlkisser.lazuli.api.inventory;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * An NBT serializable item handler.
 */
public interface ISerializableItemHandler extends IItemHandler, INBTSerializable<CompoundTag> {
}
