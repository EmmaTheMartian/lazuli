package top.girlkisser.lazuli.api.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import top.girlkisser.lazuli.common.LazuliDataComponents;

/**
 * Provides a large handful of generic helpers for items.
 * None of the methods need to be implemented, everything has a default.
 * <br/>
 * TODO: Energy helpers and move helpers to more specific interfaces (`IEnergyItem`, `IFluidItem`)
 */
public interface LazuliItem
{
	// Fluid Helpers //

	/**
	 * @return `true` if this item has a fluid tank.
	 */
	default boolean hasFluidTank()
	{
		return false;
	}

	/**
	 * @return The default mB that this item's tank can hold.
	 */
	default int getDefaultFluidHandlerCapacity()
	{
		return 1000;
	}

	/**
	 * Creates a fluid handler for the item.
	 *
	 * @param stack The item stack.
	 * @return A fluid handler.
	 */
	default IFluidHandlerItem makeFluidHandler(ItemStack stack)
	{
		return new FluidHandlerItemStack(LazuliDataComponents.GENERIC_FLUID, stack, getDefaultFluidHandlerCapacity());
	}

	/**
	 * Get the fluid handler for the given stack.
	 *
	 * @param stack The stack.
	 * @return The fluid handler.
	 */
	default IFluidHandlerItem getFluidHandler(ItemStack stack)
	{
		if (!hasFluidTank())
			return null;
		return FluidUtil.getFluidHandler(stack).orElseThrow();
	}

	/**
	 * Returns true if the stack has a fluid in its tank.
	 *
	 * @param stack The stack.
	 * @return Whether the tank contains fluid.
	 */
	default boolean hasFluid(ItemStack stack)
	{
		return hasFluidTank() && !getFluid(stack).isEmpty();
	}

	/**
	 * Get the {@link FluidStack} contained in the given tank.
	 *
	 * @param stack The stack.
	 * @param tank The tank's index.
	 * @return The fluid stack.
	 */
	default FluidStack getFluid(ItemStack stack, int tank)
	{
		if (!hasFluidTank())
			return null;
		return getFluidHandler(stack).getFluidInTank(tank);
	}

	/**
	 * Get the {@link FluidStack} contained in the item's first tank.
	 *
	 * @param stack The stack.
	 * @return The fluid stack.
	 */
	default FluidStack getFluid(ItemStack stack)
	{
		return getFluid(stack, 0);
	}

	/**
	 * Get the fluid tank capacity in mB of the given tank.
	 *
	 * @param stack The stack.
	 * @param tank The tank's index.
	 * @return The capacity.
	 */
	default int getFluidTankCapacity(ItemStack stack, int tank)
	{
		if (!hasFluidTank())
			return -1;
		return getFluidHandler(stack).getTankCapacity(tank);
	}


	/**
	 * Get the fluid tank capacity in mB of the item's first tank.
	 *
	 * @param stack The stack.
	 * @return The capacity.
	 */
	default int getFluidTankCapacity(ItemStack stack)
	{
		return getFluidTankCapacity(stack, 0);
	}
}
