package top.girlkisser.lazuli.api.collections;

/**
 * Provides a handful of helpers for arrays.
 */
public final class ArrayHelpers
{
	private ArrayHelpers()
	{
	}

	/**
	 * Creates an array using the provided range.
	 *
	 * @param min The initial value (inclusive).
	 * @param max The ending value (exclusive).
	 * @param step The step between values.
	 * @return An array containing the range of integers.
	 */
	public static int[] rangeOf(int min, int max, int step)
	{
		assert max > min;
		int[] range = new int[max - min];
		int i = 0;
		for (int value = min ; value < max ; value += step)
		{
			range[i++] = value;
		}
		return range;
	}

	/**
	 * Creates an array using the provided range.
	 *
	 * @param min The initial value (inclusive).
	 * @param max The ending value (exclusive).
	 * @return An array containing the range of integers.
	 */
	public static int[] rangeOf(int min, int max)
	{
		return rangeOf(min, max, 1);
	}

	/**
	 * Creates an array using the provided range where the starting value is 0.
	 *
	 * @param max The ending value (exclusive).
	 * @return An array containing the range of integers.
	 */
	public static int[] rangeOf(int max)
	{
		return rangeOf(0, max, 1);
	}

	/**
	 * Check if an array contains a given value.
	 *
	 * @param array The array to check.
	 * @param entry The entry to check for.
	 * @return Whether the array contains the given entry.
	 * @param <T> The array's type.
	 */
	public static <T> boolean contains(T[] array, T entry)
	{
		for (T it : array)
		{
			if (it.equals(entry))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if an array contains a given value.
	 *
	 * @param array The array to check.
	 * @param entry The entry to check for.
	 * @return Whether the array contains the given entry.
	 */
	public static boolean contains(int[] array, int entry)
	{
		for (int it : array)
		{
			if (it == entry)
			{
				return true;
			}
		}
		return false;
	}
}
