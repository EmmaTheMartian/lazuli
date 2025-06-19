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

	ProtectiveEnergyStorage getEnergyStorage();

	/**
	 * Energy capacity of the machine
	 */
	default int getMaxEnergy()
	{
		return DEFAULT_MAX_ENERGY;
	}

	/**
	 * Maximum energy extractable per tick for the BE.
	 */
	default int getMaxEnergyExtract()
	{
		return 32;
	}

	/**
	 * Maximum energy receivable per tick for the BE.
	 */
	default int getMaxEnergyReceive()
	{
		return 32;
	}

	/**
	 * @return The amount of energy stored in the block entity.
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
