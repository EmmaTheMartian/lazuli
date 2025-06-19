package top.girlkisser.lazuli.api.block;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.girlkisser.lazuli.api.energy.ProtectiveEnergyStorage;

/**
 * Basic methods for block entities with energy.
 */
public interface IEnergyBE extends IBE
{
	/**
	 * The default amount of RF that an IEnergyBE can store.
	 */
	int DEFAULT_MAX_ENERGY = 1600;

	/**
	 * Gets the energy storage for this block entity.
	 *
	 * @return The energy storage.
	 */
	ProtectiveEnergyStorage getEnergyStorage();

	/**
	 * Gets the energy capacity of the machine
	 *
	 * @return The capacity.
	 */
	default int getMaxEnergy()
	{
		return DEFAULT_MAX_ENERGY;
	}

	/**
	 * Gets the maximum energy extractable per tick for the BE.
	 *
	 * @return The max energy extractable per tick.
	 */
	default int getMaxEnergyExtract()
	{
		return 32;
	}

	/**
	 * Gets the maximum energy receivable per tick for the BE.
	 *
	 * @return The max energy receivable per tick.
	 */
	default int getMaxEnergyReceive()
	{
		return 32;
	}

	/**
	 * Gets the amount of energy stored in the block entity.
	 *
	 * @return The amount.
	 */
	default int getEnergyStored()
	{
		return getEnergyStorage().getEnergyStored();
	}

	/**
	 * Get whether the block entity can push energy in the given direction.
	 *
	 * @param direction The direction to check.
	 * @return `true` if energy can be pushed in the given direction.
	 */
	default boolean canDistributeInDirection(Direction direction)
	{
		return true;
	}

	/**
	 * Distributes power to each direction if able.
	 */
	default void distributePower()
	{
		for (Direction direction : Direction.values())
		{
			if (!canDistributeInDirection(direction))
			{
				continue;
			}

			if (getEnergyStorage().getEnergyStored() <= 0)
			{
				return;
			}

			IEnergyStorage energy = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(direction), direction);
			if (energy != null && energy.canReceive())
			{
				int received = energy.receiveEnergy(Math.min(getEnergyStorage().getEnergyStored(), getMaxEnergyExtract()), false);
				getEnergyStorage().forceExtractEnergy(received, false);
				setChanged();
			}
		}
	}
}
