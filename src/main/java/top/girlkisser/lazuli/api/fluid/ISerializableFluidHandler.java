package top.girlkisser.lazuli.api.fluid;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * A serializable fluid handler.
 */
public interface ISerializableFluidHandler extends IFluidHandler, INBTSerializable<CompoundTag> {
}
