package top.girlkisser.lazuli.api.inventory;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An inventory that inserts items round-robin into the inventory.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RoundRobinInventory extends ItemStackHandler
{
	/**
	 * The next index to insert an item into.
	 */
	protected int index = 0; //TODO: serialize this for consistency

	/**
	 * Creates a round-robin inventory with a size of 1.
	 */
	public RoundRobinInventory()
	{
		super();
	}

	/**
	 * Creates a round-robin inventory with the given size.
	 *
	 * @param size The size.
	 */
	public RoundRobinInventory(int size)
	{
		super(size);
	}

	/**
	 * Creates a round-robin inventory with the given stacks.
	 *
	 * @param stacks The stacks.
	 */
	public RoundRobinInventory(NonNullList<ItemStack> stacks)
	{
		super(stacks);
	}

	/**
	 * You should use {@link RoundRobinInventory#insertItem(ItemStack, boolean)} instead
	 * since the {@code ignoredSlot} parameter is ignored.
	 */
	@Override
	@ApiStatus.Obsolete
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (simulate)
		{
			return super.insertItem(slot, stack, true);
		}
		else
		{
			ItemStack result = super.insertItem(index++, stack, false);
			if (index >= this.getSlots())
			{
				index = 0;
			}
			return result;
		}
	}

	/**
	 * Inserts an item into the inventory.
	 *
	 * @param stack    The stack to insert.
	 * @param simulate If this action should be simulated or not.
	 * @return The remainder item stack, if any.
	 */
	public ItemStack insertItem(ItemStack stack, boolean simulate)
	{
		return insertItem(0, stack, simulate);
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		CompoundTag tag = super.serializeNBT(provider);
		tag.putInt("Index", index);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag)
	{
		super.deserializeNBT(provider, tag);

		if (tag.contains("Index"))
			this.index = tag.getInt("Index");
	}
}
