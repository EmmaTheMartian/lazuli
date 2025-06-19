package top.girlkisser.lazuli.api.collections;

import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

/**
 * A list for picking random values with weights.
 * Mojang does have a WeightedRandomList implementation, however theirs uses an
 * immutable list. This one uses a mutable list, which makes it a little more
 * flexible.
 *
 * @param <T> The type of entries in the list.
 */
public class WeightedRandomList<T extends WeightedRandomList.IWeighted>
{
	/** The list's entries. */
	protected final List<Entry<T>> entries;
	/** The total weight of all entries in the list. */
	protected int accumulatedWeight;

	/**
	 * Create a weighted random list using the given values.
	 *
	 * @param entries The values.
	 */
	public WeightedRandomList(List<T> entries)
	{
		this.entries = new ArrayList<>();
		for (T element : entries)
			this.addEntry(element);
	}

	/**
	 * Get the list's entries. **Do not remove values from this list**, if you absolutely
	 * must, make sure to change the accumulated weight of all entries after the removed
	 * entry *and* of the list.
	 *
	 * @return The entries.
	 */
	public List<Entry<T>> getEntries()
	{
		return entries;
	}

	/**
	 * Pick a random value from the list.
	 *
	 * @param random A random source to use.
	 * @return The value.
	 */
	public T pick(RandomSource random)
	{
		double r = random.nextInt(accumulatedWeight);
		for (var entry : entries)
		{
			if (entry.accumulatedWeight > r)
			{
				return entry.it;
			}
		}
		return null; // this should only occur when the list is empty
	}

	/**
	 * Add an entry to the list.
	 *
	 * @param it The entry.
	 */
	public void addEntry(T it)
	{
		accumulatedWeight += it.weight();
		entries.add(new Entry<>(it, accumulatedWeight));
	}

	/**
	 * An entry in the list.
	 *
	 * @param it The entry's value.
	 * @param accumulatedWeight The accumulated weight of the value.
	 * @param <T> The entry's type.
	 */
	public record Entry<T>(T it, int accumulatedWeight)
	{
	}

	/**
	 * An interface for a weighted value.
	 */
	public interface IWeighted
	{
		/**
		 * Gets the weight of this value.
		 *
		 * @return The weight.
		 */
		int weight();
	}
}
