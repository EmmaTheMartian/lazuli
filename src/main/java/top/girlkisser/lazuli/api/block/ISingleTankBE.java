package top.girlkisser.lazuli.api.block;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface ISingleTankBE extends IFluidBE
{
	FluidTank getTank();

	/**
	 * @return The tank capacity for the block entity.
	 */
	default int getTankCapacity()
	{
		return getTank().getTankCapacity(0);
	}

	/**
	 * @return The maximum fluid extractable per tick for the block entity.
	 */
	default int getMaxFluidExtract()
	{
		return 32;
	}

	/**
	 * @return The maximum fluid receivable per tick for the block entity.
	 */
	default int getMaxFluidReceive()
	{
		return 32;
	}

	/**
	 * Attempts to distribute fluid to neighbouring blocks.
	 */
	default void tryDistributeFluid()
	{
		IFluidBE.tryDistributeFluid(getTank(), getLevel(), getMaxFluidExtract(), this);
	}
}
