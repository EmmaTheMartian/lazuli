package top.girlkisser.lazuli.api.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Provides a large number of helper functions for block entities with fluid handlers.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IFluidBE extends IBE
{
	/**
	 * Attempts to distribute fluid to neighbouring blocks.
	 *
	 * @param tank       The fluid tank to distribute from.
	 * @param level      The level.
	 * @param maxExtract The maximum amount of fluid to distribute.
	 * @param be         The block entity to distribute fluid from.
	 */
	static void tryDistributeFluid(IFluidTank tank, Level level, int maxExtract, IBE be)
	{
		if (tank.getFluidAmount() <= 0)
		{
			return;
		}

		for (Direction direction : Direction.values())
		{
			if (tank.getFluidAmount() <= 0)
			{
				return;
			}

			FluidStack stack = tank.getFluid();
			IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos().relative(direction), direction);
			if (storage != null)
			{
				int toPush = Math.min(stack.getAmount(), maxExtract);
				int received = storage.fill(stack.copyWithAmount(toPush), IFluidHandler.FluidAction.EXECUTE);
				tank.drain(received, IFluidHandler.FluidAction.EXECUTE);
				be.setChanged();
				break;
			}
		}
	}

	/**
	 * Attempt to push fluid from a block entity in a given direction.
	 *
	 * @param tank       The tank to push fluid from.
	 * @param level      The level.
	 * @param maxExtract The maximum amount of fluid to push.
	 * @param be         The block entity to push fluid from.
	 * @param direction  The direction to push fluid in.
	 */
	static void tryPushFluid(IFluidTank tank, Level level, int maxExtract, IBE be, Direction direction)
	{
		if (tank.getFluidAmount() <= 0)
		{
			return;
		}

		FluidStack stack = tank.getFluid();
		IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos().relative(direction), direction);
		if (storage != null)
		{
			int toPush = Math.min(stack.getAmount(), maxExtract);
			int received = storage.fill(stack.copyWithAmount(toPush), IFluidHandler.FluidAction.EXECUTE);
			tank.drain(received, IFluidHandler.FluidAction.EXECUTE);
			be.setChanged();
		}
	}
}
