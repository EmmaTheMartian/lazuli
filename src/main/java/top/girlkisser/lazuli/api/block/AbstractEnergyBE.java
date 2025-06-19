package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.lazuli.api.energy.ProtectiveEnergyStorage;

import java.util.Objects;

/**
 * A block entity with an energy storage.
 */
public abstract class AbstractEnergyBE extends BlockEntity implements IEnergyBE
{
	private final ProtectiveEnergyStorage energyStorage = makeEnergyStorage();

	/**
	 * A block entity with an energy storage.
	 *
	 * @param type       The block entity's type.
	 * @param pos        The block entity's position.
	 * @param blockState The block entity's block state.
	 */
	public AbstractEnergyBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
	{
		super(type, pos, blockState);
	}

	/**
	 * Creates the block entity's energy storage.
	 *
	 * @return The energy storage.
	 */
	protected ProtectiveEnergyStorage makeEnergyStorage()
	{
		return new ProtectiveEnergyStorage(getMaxEnergy(), getMaxEnergyReceive(), getMaxEnergyExtract());
	}

	@Override
	public ProtectiveEnergyStorage getEnergyStorage()
	{
		return energyStorage;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		tag.put("Energy", getEnergyStorage().serializeNBT(registries));
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		if (tag.contains("Energy"))
			this.getEnergyStorage().deserializeNBT(registries, Objects.requireNonNull(tag.get("Energy")));
	}
}
