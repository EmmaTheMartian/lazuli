package top.girlkisser.lazuli.api.block;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Objects;

/**
 * A basic interface for block entities with a single tank.
 */
public interface ISingleTankBE extends IFluidBE
{
	/**
	 * Get the block entity's tank.
	 *
	 * @return The tank.
	 */
	FluidTank getTank();

	/**
	 * Gets the tank capacity for the block entity.
	 *
	 * @return The capacity.
	 */
	default int getTankCapacity()
	{
		return getTank().getTankCapacity(0);
	}

	/**
	 * Gets the maximum fluid extractable per tick for the block entity.
	 *
	 * @return The amount.
	 */
	default int getMaxFluidExtract()
	{
		return 32;
	}

	/**
	 * Gets the maximum fluid receivable per tick for the block entity.
	 *
	 * @return The amount.
	 */
	default int getMaxFluidReceive()
	{
		return 32;
	}

	/**
	 * Attempts to distribute fluid to neighbouring blocks.
	 * <br/>
	 * **This method will throw an exception the level is null.**
	 */
	default void tryDistributeFluid()
	{
		IFluidBE.tryDistributeFluid(getTank(), Objects.requireNonNull(getLevel()), getMaxFluidExtract(), this);
	}
}
