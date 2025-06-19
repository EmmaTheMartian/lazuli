package top.girlkisser.lazuli.api.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A fake item stack. Mimics the public methods from {@link ItemStack}.
 */
@ParametersAreNonnullByDefault
public class GhostItemStack implements INBTSerializable<CompoundTag>
{
	/** A codec for {@link GhostItemStack}s, based on {@link ItemStack#OPTIONAL_CODEC}. */
	public static final Codec<GhostItemStack> CODEC = ItemStack.OPTIONAL_CODEC.xmap(GhostItemStack::new, GhostItemStack::getStack);

	/** An empty {@link GhostItemStack}. */
	public static final GhostItemStack EMPTY = new GhostItemStack((Void) null);

	private ItemStack stack;

	/**
	 * A fake item stack. Mimics the public methods from {@link ItemStack}.
	 *
	 * @param item The ghost stack's item.
	 */
	public GhostItemStack(ItemLike item)
	{
		this(item, 1);
	}

	/**
	 * A fake item stack. Mimics the public methods from {@link ItemStack}.
	 *
	 * @param item The ghost stack's item.
	 */
	public GhostItemStack(Holder<Item> item)
	{
		this(item.value(), 1);
	}

	/**
	 * A fake item stack. Mimics the public methods from {@link ItemStack}.
	 *
	 * @param item The ghost stack's item.
	 * @param count The ghost stack's count.
	 * @param components The ghost stack's data components.
	 */
	public GhostItemStack(Holder<Item> item, int count, DataComponentPatch components)
	{
		this(item.value(), count);
		this.stack.applyComponentsAndValidate(components);
	}

	/**
	 * A fake item stack. Mimics the public methods from {@link ItemStack}.
	 *
	 * @param item The ghost stack's item.
	 * @param count The ghost stack's count.
	 */
	public GhostItemStack(Holder<Item> item, int count)
	{
		this(item.value(), count);
	}

	/**
	 * A fake item stack. Mimics the public methods from {@link ItemStack}.
	 *
	 * @param item The ghost stack's item.
	 * @param count The ghost stack's count.
	 */
	public GhostItemStack(ItemLike item, int count)
	{
		this(new ItemStack(item, count));
	}

	private GhostItemStack(ItemStack stack)
	{
		this.stack = stack;
		this.getItem().verifyComponentsAfterLoad(this.stack);
	}

	private GhostItemStack(@Nullable Void ignore)
	{
		this.stack = ItemStack.EMPTY;
	}

	/**
	 * Get the stack's item.
	 *
	 * @return The item.
	 */
	public Item getItem()
	{
		return stack.getItem();
	}

	/**
	 * Get the stack's count.
	 *
	 * @return The count.
	 */
	public int getCount()
	{
		return stack.getCount();
	}

	/**
	 * Get the ghost stack's data components.
	 *
	 * @return The data components.
	 */
	public DataComponentMap getComponents()
	{
		return stack.getComponents();
	}

	/**
	 * Get the ghost stack's data component patches.
	 *
	 * @return The data component patches.
	 */
	public DataComponentPatch getComponentsPatch()
	{
		return stack.getComponentsPatch();
	}

	/**
	 * Get the held stack.
	 *
	 * @return The stack.
	 */
	public ItemStack getStack()
	{
		return stack;
	}

	/**
	 * Copies the stack.
	 *
	 * @return The copy.
	 */
	public GhostItemStack copy()
	{
		return new GhostItemStack(stack.copy());
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider registries)
	{
		CompoundTag tag = new CompoundTag();
		Tag stackTag = this.stack.saveOptional(registries);
		tag.put("Stack", stackTag);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag)
	{
		this.stack = ItemStack.parseOptional(registries, tag);
	}

	/**
	 * Create a ghost item stack of the given stack with a count of 1.
	 *
	 * @param stack The stack.
	 * @return The ghost stack.
	 */
	public static GhostItemStack of(ItemStack stack)
	{
		return new GhostItemStack(stack.copyWithCount(1));
	}
}
