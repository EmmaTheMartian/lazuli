package top.girlkisser.lazuli.api.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * A basic implementation for {@link AbstractContainerMenu} providing
 * {@code quickMoveStack} and some helper functions.
 * <br/>
 * Based on https://www.mcjty.eu/docs/1.20.4_neo/ep3#container
 */
public abstract class AbstractLazuliContainer extends AbstractContainerMenu
{
	protected final int slotCount;
	protected Inventory playerInventory;

	protected AbstractLazuliContainer(@Nullable MenuType<?> menuType, int slotCount, int containerId, Inventory playerInventory)
	{
		super(menuType, containerId);
		this.slotCount = slotCount;
		this.playerInventory = playerInventory;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index)
	{
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot.hasItem())
		{
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();

			if (index < this.slotCount)
			{
				if (!this.moveItemStackTo(itemStack2, this.slotCount, this.slots.size(), true))
					return ItemStack.EMPTY;
			}
			else if (!this.moveItemStackTo(itemStack2, 0, this.slotCount, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty())
				slot.setByPlayer(ItemStack.EMPTY);
			else
				slot.setChanged();
		}

		return itemStack;
	}

	/**
	 * Add a range of slots.
	 *
	 * @param container The inventory that the slots are for.
	 * @param index The slot index that the first created slot should access.
	 * @param x The X position of the first slot.
	 * @param y The Y position of the first slot.
	 * @param count How many slots to create.
	 * @param dx The width/gap of each slot, usually 18.
	 * @return The slot index of the last slot + 1.
	 */
	protected int addSlotRange(Container container, int index, int x, int y, int count, int dx)
	{
		return addSlotRange(container, index, x, y, count, dx, Slot::new);
	}

	/**
	 * Add a range of slots using the provided slot factory to create slots with.
	 *
	 * @param container The inventory that the slots are for.
	 * @param index The slot index that the first created slot should access.
	 * @param x The X position of the first slot.
	 * @param y The Y position of the first slot.
	 * @param count How many slots to create.
	 * @param dx The width/gap of each slot, usually 18.
	 * @param factory The slot factory to use.
	 * @return The slot index of the last slot + 1.
	 */
	protected int addSlotRange(Container container, int index, int x, int y, int count, int dx, SlotFactory factory)
	{
		for (int i = 0 ; i < count ; i++)
		{
			addSlot(factory.makeSlot(container, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	/**
	 * Add a 2D range of slots.
	 *
	 * @param container The inventory that the slots are for.
	 * @param index The slot index that the first created slot should access.
	 * @param x The X position of the first slot.
	 * @param y The Y position of the first slot.
	 * @param horizontalCount How many slots to create on the horizontal axis.
	 * @param verticalCount How many slots to create on the vertical axis.
	 * @param dx The width/gap of each slot, usually 18.
	 * @return The slot index of the last slot + 1.
	 */
	protected int addSlotBox(Container container, int index, int x, int y, int horizontalCount, int verticalCount, int dx, int dy)
	{
		return addSlotBox(container, index, x, y, horizontalCount, verticalCount, dx, dy, Slot::new);
	}

	/**
	 * Add a 2D range of slots using the provided factory.
	 *
	 * @param container The inventory that the slots are for.
	 * @param index The slot index that the first created slot should access.
	 * @param x The X position of the first slot.
	 * @param y The Y position of the first slot.
	 * @param horizontalCount How many slots to create on the horizontal axis.
	 * @param verticalCount How many slots to create on the vertical axis.
	 * @param dx The width/gap of each slot, usually 18.
	 * @param factory The slot factory to use.
	 * @return The slot index of the last slot + 1.
	 */
	protected int addSlotBox(Container container, int index, int x, int y, int horizontalCount, int verticalCount, int dx, int dy, SlotFactory factory)
	{
		for (int i = 0 ; i < verticalCount ; i++)
		{
			index = addSlotRange(container, index, x, y, horizontalCount, dx, factory);
			y += dy;
		}
		return index;
	}

	/**
	 * Add a range of output-only slots.
	 *
	 * @param container The inventory that the slots are for.
	 * @param index The slot index that the first created slot should access.
	 * @param x The X position of the first slot.
	 * @param y The Y position of the first slot.
	 * @param count How many slots to create.
	 * @param dx The width/gap of each slot, usually 18.
	 * @return The slot index of the last slot + 1.
	 */
	protected int addOutputSlotRange(Container container, int index, int x, int y, int count, int dx)
	{
		for (int i = 0 ; i < count ; i++)
		{
			addSlot(new SlotOutputOnly(container, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	/**
	 * Add a 2D range of output slots.
	 *
	 * @param container The inventory that the slots are for.
	 * @param index The slot index that the first created slot should access.
	 * @param x The X position of the first slot.
	 * @param y The Y position of the first slot.
	 * @param horizontalCount How many slots to create on the horizontal axis.
	 * @param verticalCount How many slots to create on the vertical axis.
	 * @param dx The width/gap of each slot, usually 18.
	 * @return The slot index of the last slot + 1.
	 */
	protected int addOutputSlotBox(Container container, int index, int x, int y, int horizontalCount, int verticalCount, int dx, int dy)
	{
		for (int i = 0 ; i < verticalCount ; i++)
		{
			index = addOutputSlotRange(container, index, x, y, horizontalCount, dx);
			y += dy;
		}
		return index;
	}

	/**
	 * Add slots for a player's inventory (9 slots by 4 slots).
	 *
	 * @param playerInventory The player's inventory.
	 * @param x The X position for the slots.
	 * @param y The Y position for the slots.
	 */
	protected void addPlayerInventorySlots(Container playerInventory, int x, int y)
	{
		// Player inventory
		addSlotBox(playerInventory, 9, x, y, 9, 3, 18, 18);
		// Hotbar
		addSlotRange(playerInventory, 0, x, y + 58, 9, 18);
	}

	public Inventory getPlayerInventory()
	{
		return playerInventory;
	}

	@FunctionalInterface
	public interface SlotFactory
	{
		Slot makeSlot(Container container, int slot, int x, int y);
	}
}
