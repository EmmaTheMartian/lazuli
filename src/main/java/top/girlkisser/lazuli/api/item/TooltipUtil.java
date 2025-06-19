package top.girlkisser.lazuli.api.item;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;

/**
 * Provides misc helpers for item tooltips.
 */
public final class TooltipUtil
{
	private TooltipUtil()
	{
	}

	/**
	 * Get a {@link Component} for the tank representing its contents.
	 *
	 * @param tank The tank.
	 * @return The component.
	 */
	public static Component getFluidTankTooltip(IFluidTank tank)
	{
		return getFluidTankTooltip(tank.getFluid(), tank.getCapacity());
	}

	/**
	 * Get a {@link Component} for the {@link FluidStack}.
	 *
	 * @param stack The fluid stack.
	 * @param maxCapacity The maximum capacity for the fluid stack.
	 * @return The component.
	 */
	public static Component getFluidTankTooltip(FluidStack stack, int maxCapacity)
	{
		return stack.getHoverName().copy().append(": " + stack.getAmount() + "/" + maxCapacity + "mB");
	}
}
