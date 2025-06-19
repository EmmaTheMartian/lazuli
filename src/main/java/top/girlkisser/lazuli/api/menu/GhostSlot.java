package top.girlkisser.lazuli.api.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A slot which copies the inserted item and clears when a player attempts to take the
 * contained item.
 */
public class GhostSlot extends Slot
{
	/**
	 * A slot which copies the inserted item and clears when a player attempts to take the
	 * contained item.
	 *
	 * @param container The container.
	 * @param slot      The slot index.
	 * @param x         The X position.
	 * @param y         The Y position.
	 */
	public GhostSlot(Container container, int slot, int x, int y)
	{
		super(container, slot, x, y);
	}

	@Override
	public ItemStack safeInsert(ItemStack stack, int increment)
	{
		ItemStack held = stack.copy();
		super.safeInsert(stack, increment);
		return held;
	}

	@Override
	public boolean isFake()
	{
		return true;
	}
}

