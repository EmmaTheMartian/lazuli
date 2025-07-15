package top.girlkisser.lazuli.api.fluid;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

/**
 * A basic implementation of a {@link FluidTank} with {@link ISerializableFluidHandler}.
 */
@ParametersAreNonnullByDefault
public class LazuliFluidTank extends FluidTank implements ISerializableFluidHandler
{
	/**
	 * If the fluid tank can have fluid extracted from it.
	 */
	public boolean canExtract = true;

	/**
	 * If the fluid tank can have fluid extracted from it.
	 */
	public boolean canReceive = true;

	/**
	 * Create a fluid tank with the given capacity.
	 *
	 * @param capacity The capacity in millibuckets.
	 */
	public LazuliFluidTank(int capacity)
	{
		super(capacity);
	}

	/**
	 * Create a fluid tank with the given capacity and a predicate to allow fluid stacks.
	 *
	 * @param capacity The capacity in millibuckets.
	 * @param validator A predicate to control whether a given {@link FluidStack} is
	 *                  allowed to be contained in the tank.
	 */
	public LazuliFluidTank(int capacity, Predicate<FluidStack> validator)
	{
		super(capacity, validator);
	}

	@Override
	public int fill(FluidStack resource, IFluidHandler.FluidAction action)
	{
		return canReceive ? super.fill(resource, action) : 0;
	}

	@Override
	public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action)
	{
		return canExtract ? super.drain(maxDrain, action) : FluidStack.EMPTY;
	}

	/**
	 * Forcefully fill the fluid tank, disregarding {@link LazuliFluidTank#canReceive}.
	 *
	 * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
	 * @param action If SIMULATE, fill will only be simulated.
	 * @return Amount of resource that was (or would have been, if simulated) filled.
	 */
	public int forceFill(FluidStack resource, IFluidHandler.FluidAction action)
	{
		return super.fill(resource, action);
	}

	/**
	 * Forcefully drain the tank, disregarding {@link LazuliFluidTank#canExtract}.
	 *
	 * @param maxDrain Maximum amount of fluid to drain.
	 * @param action If SIMULATE, drain will only be simulated.
	 * @return FluidStack representing the Fluid and amount that was (or would have been, if simulated) drained.
	 */
	public FluidStack forceDrain(int maxDrain, IFluidHandler.FluidAction action)
	{
		return super.drain(maxDrain, action);
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider lookup)
	{
		CompoundTag tag = new CompoundTag();
		this.writeToNBT(lookup, tag);
		tag.putBoolean("CanExtract", canExtract);
		tag.putBoolean("CanReceive", canReceive);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider lookup, CompoundTag tag)
	{
		this.readFromNBT(lookup, tag);

		if (tag.contains("CanExtract"))
			this.canExtract = tag.getBoolean("CanExtract");

		if (tag.contains("CanReceive"))
			this.canReceive = tag.getBoolean("CanReceive");
	}
}
