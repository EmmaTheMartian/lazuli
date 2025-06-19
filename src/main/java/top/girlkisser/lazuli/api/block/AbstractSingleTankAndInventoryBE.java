package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * A block entity implementing both {@link ISingleTankBE} and {@link IInventoryBE}.
 */
public abstract class AbstractSingleTankAndInventoryBE
		extends BlockEntity
		implements ISingleTankBE, IInventoryBE<ItemStackHandler>
{
	private final FluidTank tank;
	protected final int tankCapacity;
	protected final ItemStackHandler inventory;
	protected final int slots;

	public AbstractSingleTankAndInventoryBE(BlockEntityType<?> type, int tankCapacity, int slots, BlockPos pos, BlockState blockState)
	{
		super(type, pos, blockState);
		this.tankCapacity = tankCapacity;
		this.tank = makeFluidTank();
		this.slots = slots;
		this.inventory = makeItemStackHandler();
	}

	protected boolean validateFluidStack(FluidStack stack)
	{
		return true;
	}

	protected FluidTank makeFluidTank()
	{
		return new FluidTank(tankCapacity, this::validateFluidStack)
		{
			@Override
			public void onContentsChanged()
			{
				AbstractSingleTankAndInventoryBE.this.setChanged();
			}
		};
	}

	protected ItemStackHandler makeItemStackHandler()
	{
		return new ItemStackHandler(slots)
		{
			@Override
			public void onContentsChanged(int slot)
			{
				AbstractSingleTankAndInventoryBE.this.setChanged();
			}
		};
	}

	@Override
	public FluidTank getTank()
	{
		return tank;
	}

	@Override
	public @NotNull ItemStackHandler getInventory()
	{
		return inventory;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		CompoundTag tankTag = new CompoundTag();
		tank.writeToNBT(registries, tankTag);
		tag.put("Tank", tankTag);
		tag.put("Inventory", inventory.serializeNBT(registries));
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);

		if (tag.contains("Tank"))
			tank.readFromNBT(registries, tag.getCompound("Tank"));

		if (tag.contains("Inventory"))
			inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
	}
}
