package top.girlkisser.lazuli.api.energy;

import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * A basic serializable energy storage implementation.
 */
public class LazuliEnergyStorage extends EnergyStorage implements ISerializableEnergyStorage
{
	/**
	 * A basic serializable energy storage implementation.
	 *
	 * @param capacity   The capacity in RF/FE.
	 * @param maxReceive The maximum amount of energy receivable per tick.
	 * @param maxExtract The maximum amount of energy extractable per tick.
	 */
	public LazuliEnergyStorage(int capacity, int maxReceive, int maxExtract)
	{
		super(capacity, maxReceive, maxExtract);
	}

	/**
	 * Invoked when the energy storage changes.
	 */
	protected void onChange()
	{
	}

	/**
	 * Add energy to the storage.
	 *
	 * @param energy   How much energy to add.
	 * @param simulate If this action should be simulated or not.
	 * @return The amount of energy received.
	 */
	@Override
	public int receiveEnergy(int energy, boolean simulate)
	{
		int amount = super.receiveEnergy(energy, simulate);
		if (amount != 0) onChange();
		return amount;
	}

	/**
	 * Remove energy from the storage.
	 *
	 * @param energy   How much energy to add.
	 * @param simulate If this action should be simulated or not.
	 * @return The amount of energy extracted.
	 */
	@Override
	public int extractEnergy(int energy, boolean simulate)
	{
		int amount = super.extractEnergy(energy, simulate);
		if (amount != 0) onChange();
		return amount;
	}

	/**
	 * Creates a copy of this energy storage that cannot receive energy.
	 *
	 * @return The copy.
	 */
	public IEnergyStorage getExtractOnly()
	{
		return new IEnergyStorage()
		{
			@Override
			public int receiveEnergy(int i, boolean bl)
			{
				return 0;
			}

			@Override
			public int extractEnergy(int i, boolean bl)
			{
				return LazuliEnergyStorage.this.extractEnergy(i, bl);
			}

			@Override
			public int getEnergyStored()
			{
				return LazuliEnergyStorage.this.getEnergyStored();
			}

			@Override
			public int getMaxEnergyStored()
			{
				return LazuliEnergyStorage.this.getMaxEnergyStored();
			}

			@Override
			public boolean canExtract()
			{
				return true;
			}

			@Override
			public boolean canReceive()
			{
				return false;
			}
		};
	}

	/**
	 * Creates a copy of this energy storage that cannot have energy extracted.
	 *
	 * @return The copy.
	 */
	public IEnergyStorage getReceiveOnly()
	{
		return new IEnergyStorage()
		{
			@Override
			public int receiveEnergy(int i, boolean bl)
			{
				return LazuliEnergyStorage.this.receiveEnergy(i, bl);
			}

			@Override
			public int extractEnergy(int i, boolean bl)
			{
				return 0;
			}

			@Override
			public int getEnergyStored()
			{
				return LazuliEnergyStorage.this.getEnergyStored();
			}

			@Override
			public int getMaxEnergyStored()
			{
				return LazuliEnergyStorage.this.getMaxEnergyStored();
			}

			@Override
			public boolean canExtract()
			{
				return false;
			}

			@Override
			public boolean canReceive()
			{
				return true;
			}
		};
	}
}
