package top.girlkisser.lazuli.api.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Predicate;

/**
 * Provides a handful of helpers for containers.
 */
public final class ContainerUtils
{
	private ContainerUtils()
	{
	}

	/**
	 * Extract items from the given container.
	 *
	 * @param container The container to extract items from.
	 * @param matching  What predicate for which items to extract.
	 * @param count     The maximum amount of items to extract.
	 * @return The amount of items extracted.
	 */
	public static int extractItems(Container container, Predicate<ItemStack> matching, int count)
	{
		int remaining = count;

		for (int i = 0 ; i < container.getContainerSize() ; i++)
		{
			ItemStack stack = container.getItem(i);
			if (matching.test(stack))
			{
				int oldStackCount = stack.getCount();
				stack.shrink(remaining);
				int newStackCount = stack.getCount();
				if (stack.isEmpty())
					container.setItem(i, ItemStack.EMPTY);
				int change = oldStackCount - newStackCount;
				remaining -= change;
				if (remaining <= 0)
					break;
			}
		}

		return remaining;
	}

	/**
	 * Count how many items the container has that match the predicate.
	 *
	 * @param container The container to check.
	 * @param matching  The predicate to use.
	 * @return The amount of matching items.
	 */
	public static int countItems(Container container, Predicate<ItemStack> matching)
	{
		int count = 0;

		for (int i = 0 ; i < container.getContainerSize() ; i++)
		{
			ItemStack stack = container.getItem(i);
			if (matching.test(stack))
				count += stack.getCount();
		}

		return count;
	}

	/**
	 * Check if the provided list contains at least one item that matches the ingredient.
	 *
	 * @param stacks     The stacks to check.
	 * @param ingredient The ingredient to match.
	 * @return Whether the list contains the ingredient.
	 */
	public static boolean doesListContainItemMatchingIngredient(List<ItemStack> stacks, Ingredient ingredient)
	{
		for (var stack : stacks)
		{
			if (ingredient.test(stack))
				return true;
		}
		return false;
	}

	// Similar to extractItems but handles crafting remainders

	/**
	 * Extract items from the given container and checks for crafting remainders on
	 * extracted items.
	 *
	 * @param container The container to extract items from.
	 * @param matching  What predicate for which items to extract.
	 * @param count     The maximum amount of items to extract.
	 * @return The amount of items extracted.
	 * @see ContainerUtils#extractItems(Container, Predicate, int)
	 */
	public static int extractItemsForCrafting(Container container, Predicate<ItemStack> matching, int count)
	{
		int remaining = count;

		for (int i = 0 ; i < container.getContainerSize() ; i++)
		{
			ItemStack stack = container.getItem(i);
			if (matching.test(stack))
			{
				if (stack.hasCraftingRemainingItem())
				{
					container.setItem(i, stack.getCraftingRemainingItem());
					remaining--;
				}
				else
				{
					int oldStackCount = stack.getCount();
					stack.shrink(remaining);
					int newStackCount = stack.getCount();
					if (stack.isEmpty())
						container.setItem(i, ItemStack.EMPTY);
					int change = oldStackCount - newStackCount;
					remaining -= change;
				}
				if (remaining <= 0)
					break;
			}
		}

		return remaining;
	}
}
