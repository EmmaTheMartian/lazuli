package top.girlkisser.lazuli.api.inventory;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
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
	protected int index = 0; //TODO: serialize this for consistency

	public RoundRobinInventory()
	{
		super();
	}

	public RoundRobinInventory(int size)
	{
		super(size);
	}

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
	public ItemStack insertItem(int ignoredSlot, ItemStack stack, boolean simulate)
	{
		// We will be disregarding `slot` here because we want to round-robin
		// distribute items
		ItemStack result = super.insertItem(index++, stack, simulate);
		if (index >= this.getSlots())
		{
			index = 0;
		}
		return result;
	}

	public ItemStack insertItem(ItemStack stack, boolean simulate)
	{
		return insertItem(0, stack, simulate);
	}
}
