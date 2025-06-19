package top.girlkisser.lazuli.api.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * An {@link ItemStackHandler} with some helper methods.
 */
public class LazuliItemStackHandler extends ItemStackHandler implements ISerializableItemHandler
{
	public LazuliItemStackHandler()
	{
		this(1);
	}

	public LazuliItemStackHandler(int size)
	{
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public LazuliItemStackHandler(NonNullList<ItemStack> stacks)
	{
		this.stacks = stacks;
	}

	/**
	 * Attempt to insert an item into the stack handler.
	 *
	 * @param stack The item stack to insert.
	 * @param simulate If this action should be simulated.
	 * @return The remainder, if there was one.
	 */
	public ItemStack insertItem(ItemStack stack, boolean simulate)
	{
		return insertItem(this, stack, simulate);
	}

	/**
	 * Attempt to insert an item into the stack handler.
	 *
	 * @param handler The inventory to insert into.
	 * @param stack The item stack to insert.
	 * @param simulate If this action should be simulated.
	 * @return The remainder, if there was one.
	 */
	public static ItemStack insertItem(ItemStackHandler handler, ItemStack stack, boolean simulate)
	{
		ItemStack remainder = stack.copy();
		for (int i = 0 ; i < handler.getSlots() ; i++)
		{
			if (handler.insertItem(i, stack, true) != stack)
			{
				remainder = handler.insertItem(i, stack, simulate);
				if (remainder == ItemStack.EMPTY)
				{
					break;
				}
			}
		}
		return remainder;
	}
}
