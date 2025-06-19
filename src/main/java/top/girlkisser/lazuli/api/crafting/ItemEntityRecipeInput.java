package top.girlkisser.lazuli.api.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * A recipe input for a {@link NonNullList<ItemEntity>}.
 *
 * @param entities The input item entities.
 */
public record ItemEntityRecipeInput(NonNullList<ItemEntity> entities) implements Container, RecipeInput
{
	/**
	 * Remove the item entity with the given index from the world.
	 *
	 * @param index The index.
	 */
	public void removeEntityFromWorld(int index)
	{
		entities.get(index).remove(Entity.RemovalReason.DISCARDED);
	}

	@Override
	public int size()
	{
		return entities.size();
	}

	@Override
	public int getContainerSize()
	{
		return size();
	}

	@Override
	public boolean isEmpty()
	{
		return entities.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return entities.get(slot).getItem();
	}

	@Override
	public ItemStack removeItem(int slot, int amount)
	{
		ItemStack stack = getItem(slot);
		return stack.isEmpty() ? ItemStack.EMPTY : stack.split(amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		ItemStack stack = getItem(slot).copy();
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		else
		{
			setItem(slot, ItemStack.EMPTY);
			return stack;
		}
	}

	@Override
	public void setItem(int slot, ItemStack stack)
	{
		this.entities.get(slot).setItem(stack);
	}

	@Override
	@Deprecated
	public void setChanged()
	{
		throw new RuntimeException("Cannot invoke EntityContainer#setChanged");
	}

	@Override
	public boolean stillValid(Player player)
	{
		return false;
	}

	@Override
	public void clearContent()
	{
		for (ItemEntity entity : entities)
		{
			entity.remove(Entity.RemovalReason.DISCARDED);
		}
	}
}
