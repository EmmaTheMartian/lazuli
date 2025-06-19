package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A generic battery block with an inventory to charge items with.
 */
public abstract class AbstractCapacitorBE extends AbstractEnergyBE implements ITickableBE, IInventoryBE<ItemStackHandler>
{
	protected final ItemStackHandler itemHandler = new ItemStackHandler();

	public static final int SLOT_COUNT = 1;

	public AbstractCapacitorBE(BlockEntityType<? extends AbstractCapacitorBE> blockEntityType, BlockPos pos, BlockState blockState)
	{
		super(blockEntityType, pos, blockState);
	}

	public ItemStack getHeldStack()
	{
		return itemHandler.getStackInSlot(0);
	}

	public IEnergyStorage getEnergyStorage(@Nullable Direction direction)
	{
		return direction == Direction.UP || direction == null ?
			getEnergyStorage().getReceiveOnly() :
			getEnergyStorage().getExtractOnly();
	}

	@Override
	public ItemStackHandler getInventory()
	{
		return itemHandler;
	}

	@Override
	public boolean canDistributeInDirection(Direction direction)
	{
		return direction != Direction.UP;
	}

	@Override
	public void serverTick(ServerLevel level)
	{
		if (getEnergyStored() <= 0)
		{
			return;
		}

		// Attempt to charge the held item, if it exists
		IEnergyStorage itemEnergy = getHeldStack().getCapability(Capabilities.EnergyStorage.ITEM);
		if (itemEnergy != null && itemEnergy.canReceive())
		{
			int received = itemEnergy.receiveEnergy(Math.min(getEnergyStored(), getMaxEnergyExtract()), false);
			getEnergyStorage().forceExtractEnergy(received, false);
			setChanged();
		}

		// Attempt to charge adjacent blocks
		if (getEnergyStored() > 0)
		{
			this.distributePower();
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		tag.put("Item", itemHandler.serializeNBT(registries));
	}

	@Override
	@ParametersAreNonnullByDefault
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		if (tag.contains("Item"))
			itemHandler.deserializeNBT(registries, tag.getCompound("Item"));
	}
}
