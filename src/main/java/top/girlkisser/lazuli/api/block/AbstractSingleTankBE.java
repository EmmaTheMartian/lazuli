package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

/**
 * A block entity with a single tank.
 */
public class AbstractSingleTankBE extends BlockEntity implements ISingleTankBE
{
	/**
	 * The block entity's fluid tank.
	 */
	protected final FluidTank tank;

	/**
	 * The tank's capacity in mB.
	 */
	protected final int capacity;

	/**
	 * A block entity with a single tank.
	 *
	 * @param type       The block entity's type.
	 * @param capacity   The tank's capacity in mB.
	 * @param pos        The block entity's position.
	 * @param blockState The block entity's block state.
	 */
	public AbstractSingleTankBE(BlockEntityType<?> type, int capacity, BlockPos pos, BlockState blockState)
	{
		super(type, pos, blockState);
		this.capacity = capacity;
		this.tank = makeFluidTank();
	}

	/**
	 * A basic validator for fluid stacks. If {@link AbstractSingleTankBE#makeFluidTank()}
	 * is overridden, this method may do nothing.
	 *
	 * @param stack The stack to validate.
	 * @return Whether the stack is valid for this tank.
	 */
	protected boolean validate(FluidStack stack)
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
		return new FluidTank(capacity, this::validate)
		{
			@Override
			public void onContentsChanged()
			{
				AbstractSingleTankBE.this.setChanged();
			}
		};
	}

	@Override
	public FluidTank getTank()
	{
		return tank;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		CompoundTag tankTag = new CompoundTag();
		tank.writeToNBT(registries, tankTag);
		tag.put("Tank", tankTag);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);

		if (tag.contains("Tank"))
			tank.readFromNBT(registries, tag.getCompound("Tank"));
	}
}
