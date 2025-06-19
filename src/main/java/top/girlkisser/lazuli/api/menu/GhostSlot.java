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
	public GhostSlot(Container container, int slot, int x, int y)
	{
		super(container, slot, x, y);
	}

	public ItemStack safeInsert(ItemStack stack, int increment)
	{
		ItemStack held = stack.copy();
		super.safeInsert(stack, increment);
		return held;
	}

	public boolean isFake()
	{
		return true;
	}
}

