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

/**
 * A block entity implementing both {@link ISingleTankBE} and {@link IInventoryBE}.
 */
public abstract class AbstractSingleTankAndInventoryBE
	extends BlockEntity
	implements ISingleTankBE, IInventoryBE<ItemStackHandler>
{
	/**
	 * The block entity's fluid tank.
	 */
	protected final FluidTank tank;

	/**
	 * The tank's capacity in mB.
	 */
	protected final int tankCapacity;

	/**
	 * The block entity's inventory
	 */
	protected final ItemStackHandler inventory;

	/**
	 * The amount of slots the inventory has.
	 */
	protected final int slots;

	/**
	 * A block entity implementing both {@link ISingleTankBE} and {@link IInventoryBE}.
	 *
	 * @param type         The block entity's type.
	 * @param tankCapacity The tank's capacity in mB.
	 * @param slots        The amount of inventory slots.
	 * @param pos          The block entity's position.
	 * @param blockState   The block entity's block state.
	 */
	public AbstractSingleTankAndInventoryBE(BlockEntityType<?> type, int tankCapacity, int slots, BlockPos pos, BlockState blockState)
	{
		super(type, pos, blockState);
		this.tankCapacity = tankCapacity;
		this.tank = makeFluidTank();
		this.slots = slots;
		this.inventory = makeItemStackHandler();
	}

	/**
	 * A basic validator for fluid stacks. If {@link AbstractSingleTankBE#makeFluidTank()}
	 * is overridden, this method may do nothing.
	 *
	 * @param stack The stack to validate.
	 * @return Whether the stack is valid for this tank.
	 */
	protected boolean validateFluidStack(FluidStack stack)
	{
		return true;
	}

	/**
	 * Creates the fluid tank for the block entity.
	 *
	 * @return The tank.
	 */
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


	/**
	 * Creates the item stack handler for the block entity.
	 *
	 * @return Any {@link ItemStackHandler} for the inventory.
	 */
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
	public ItemStackHandler getInventory()
	{
		return inventory;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		CompoundTag tankTag = new CompoundTag();
		tank.writeToNBT(registries, tankTag);
		tag.put("Tank", tankTag);
		tag.put("Inventory", inventory.serializeNBT(registries));
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);

		if (tag.contains("Tank"))
			tank.readFromNBT(registries, tag.getCompound("Tank"));

		if (tag.contains("Inventory"))
			inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
	}
}
