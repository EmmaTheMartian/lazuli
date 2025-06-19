package top.girlkisser.lazuli.api.crafting;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A recipe input for... entities. Strange, but I have used it before!
 *
 * @param entity The entity to... craft with.
 */
public record EntityRecipeInput(EntityType<?> entity) implements RecipeInput
{
	@Override
	@ApiStatus.Obsolete
	public ItemStack getItem(int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}
}
