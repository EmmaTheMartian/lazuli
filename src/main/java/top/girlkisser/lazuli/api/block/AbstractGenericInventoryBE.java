package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

/**
 * {@link top.girlkisser.lazuli.api.block.AbstractInventoryBE} but allowing for any valid item handler.
 * {@see BlockItemRouterBE} for an example use-case.
 * @param <T> The inventory type.
 */
public abstract class AbstractGenericInventoryBE<T extends IItemHandler & IItemHandlerModifiable & INBTSerializable<CompoundTag>>
	extends BlockEntity
	implements IInventoryBE<T>
{
	/**
	 * The block entity's inventory.
	 */
	protected final T inventory;

	/**
	 * The amount of slots that the inventory has.
	 */
	protected final int slots;

	/**
	 * A generic class for block entities with inventories.
	 *
	 * @param type The block entity type.
	 * @param slots The amount of slots in the inventory. Do not include the player's inventory slot count in this.
	 * @param pos The position of this block entity.
	 * @param blockState The block state at the given `pos`.
	 */
	public AbstractGenericInventoryBE(BlockEntityType<?> type, int slots, BlockPos pos, BlockState blockState)
	{
		super(type, pos, blockState);
		this.slots = slots;
		this.inventory = makeItemStackHandler();
	}

	/**
	 * Instantiates an inventory for the block entity.
	 *
	 * @return Any ItemStackHandler for the inventory.
	 */
	protected abstract T makeItemStackHandler();

	@Override
	public T getInventory()
	{
		return inventory;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		tag.put("Inventory", inventory.serializeNBT(registries));
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);

		if (tag.contains("Inventory"))
			inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
	}
}
