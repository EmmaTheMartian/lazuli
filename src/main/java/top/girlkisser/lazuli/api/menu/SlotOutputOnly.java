package top.girlkisser.lazuli.api.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A slot that allows extracts but not inserts.
 */
public class SlotOutputOnly extends Slot
{
	/**
	 * A slot that allows extracts but not inserts.
	 *
	 * @param container The container.
	 * @param slot      The slot index.
	 * @param x         The X position.
	 * @param y         The Y position.
	 */
	public SlotOutputOnly(Container container, int slot, int x, int y)
	{
		super(container, slot, x, y);
	}

	@Override
	public ItemStack safeInsert(ItemStack stack)
	{
		return stack;
	}

	@Override
	public ItemStack safeInsert(ItemStack stack, int increment)
	{
		return this.safeInsert(stack);
	}

	@Override
	public boolean allowModification(Player player)
	{
		return false;
	}

	@Override
	public boolean mayPlace(ItemStack stack)
	{
		return false;
	}
}

