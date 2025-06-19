package top.girlkisser.lazuli.api.energy;

import net.minecraft.util.Mth;

/**
 * An energy storage implementation that provides forceful receive/extract methods.
 */
public class ProtectiveEnergyStorage extends LazuliEnergyStorage
{
	/**
	 * An energy storage implementation that provides forceful receive/extract methods.
	 *
	 * @param capacity The capacity in RF/FE.
	 * @param maxReceive The maximum amount of energy receivable per tick.
	 * @param maxExtract The maximum amount of energy extractable per tick.
	 */
	public ProtectiveEnergyStorage(int capacity, int maxReceive, int maxExtract)
	{
		super(capacity, maxReceive, maxExtract);
	}

	/**
	 * Forcefully receive energy, ignoring if the energy storage can receive energy or not.
	 *
	 * @param energy How much energy to add.
	 * @param simulate If this action should be simulated or not.
	 * @return The amount of energy received.
	 */
	public int forceReceiveEnergy(int energy, boolean simulate)
	{
		if (energy > 0)
		{
			int energyReceived = Mth.clamp(this.capacity - this.energy, 0, energy);
			if (!simulate)
				this.energy += energyReceived;

			if (energyReceived != 0)
				this.onChange();

			return energyReceived;
		}
		else
			return 0;
	}

	/**
	 * Forcefully extract energy, ignoring if the energy storage can have energy
	 * extracted from or not.
	 *
	 * @param energy How much energy to add.
	 * @param simulate If this action should be simulated or not.
	 * @return The amount of energy received.
	 */
	public int forceExtractEnergy(int energy, boolean simulate)
	{
		if (energy > 0)
		{
			int energyExtracted = Math.min(this.energy, energy);
			if (!simulate)
				this.energy -= energyExtracted;

			if (energyExtracted != 0)
				this.onChange();

			return energyExtracted;
		}
		else
			return 0;
	}
}
