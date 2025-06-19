package top.girlkisser.lazuli.api.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.lazuli.api.collections.ArrayHelpers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a large number of helper functions for block entities with inventories.
 *
 * @param <T> The item stack handler (inventory) that the implementing block entity uses.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IInventoryBE<T extends IItemHandler & IItemHandlerModifiable & INBTSerializable<CompoundTag>> extends IBE, WorldlyContainer, StackedContentsCompatible
{
	/**
	 * Get the block entity's inventory
	 *
	 * @return The inventory.
	 */
	T getInventory();

	/**
	 * Get the inventory on the given side, by default the inventory is non-sided, so
	 * this returns the main inventory.
	 *
	 * @param side The side to access.
	 * @return The inventory for that side.
	 */
	default T getInventory(Direction side)
	{
		return getInventory();
	}

	/**
	 * Gets the indexes to any slots accessible on the given side.
	 *
	 * @param side The side to access.
	 * @return An array of slot indexes.
	 */
	@Override
	default int[] getSlotsForFace(Direction side)
	{
		return ArrayHelpers.rangeOf(getInventory(side).getSlots());
	}

	/**
	 * Controls whether an item can be inserted into the given slot through the given face.
	 *
	 * @param index The slot to check.
	 * @param stack The item attempting to be inserted.
	 * @param direction The direction the item is being inserted on.
	 * @return `true` if the item can be inserted.
	 */
	@Override
	default boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
	{
		return true;
	}

	/**
	 * Controls whether an item can be taken from the given slot through the given face.
	 *
	 * @param index The slot to check.
	 * @param stack The item attempting to be inserted.
	 * @param direction The direction the item is being inserted on.
	 * @return `true` if the item can be inserted.
	 */
	@Override
	default boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		return true;
	}

	@Override
	default void fillStackedContents(StackedContents contents)
	{
		for (int i = 0 ; i < getInventory().getSlots() ; i++)
		{
			contents.accountStack(getItem(i));
		}
	}

	/**
	 * @return `true` if the inventory is full. This does NOT account for stack sizes.
	 */
	default boolean isInventoryFull()
	{
		return isFull(getInventory());
	}

	/**
	 * @return The amount of slots in the inventory.
	 */
	default int getContainerSize()
	{
		return getInventory().getSlots();
	}

	/**
	 * @return `true` if all slots in the inventory are empty.
	 */
	default boolean isEmpty()
	{
		for (int i = 0 ; i < getInventory().getSlots() ; i++)
		{
			if (!getInventory().getStackInSlot(i).isEmpty())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the item in the given slot index.
	 *
	 * @param slot The index to aacces.
	 * @return The item stack contained in the slot.
	 */
	default ItemStack getItem(int slot)
	{
		return getInventory().getStackInSlot(slot);
	}

	/**
	 * Removes `amount` items from the given slot and returns the taken {@link ItemStack}.
	 *
	 * @param slot The slot to remove items from.
	 * @param amount The amount to remove.
	 * @return An {@link ItemStack} containing the item removed and the amount removed.
	 */
	default ItemStack removeItem(int slot, int amount)
	{
		return getInventory().extractItem(slot, amount, false);
	}

	/**
	 * Removes the item from the given slot without updating the container.
	 *
	 * @param slot The slot to remove the item from.
	 * @return The item removed.
	 */
	default ItemStack removeItemNoUpdate(int slot)
	{
		ItemStack stack = getInventory().getStackInSlot(slot).copy();
		getInventory().setStackInSlot(slot, ItemStack.EMPTY);
		return stack;
	}

	/**
	 * Set the {@link ItemStack} in the given slot.
	 *
	 * @param slot The slot index.
	 * @param stack The stack.
	 */
	default void setItem(int slot, ItemStack stack)
	{
		getInventory().setStackInSlot(slot, stack);
	}

	/**
	 * Returns true if the container is still valid for the player to interact with. The
	 * default implementation checks if the player is within range of the block entity.
	 *
	 * @param player The player to check.
	 * @return `true` if the player is still able to access the inventory.
	 */
	default boolean stillValid(Player player)
	{
		return ContainerLevelAccess.create(getLevel(), getBlockPos()).evaluate((level, pos) ->
			player.canInteractWithBlock(pos, player.blockInteractionRange()), true);
	}

	/**
	 * Clear all items from the inventory.
	 */
	default void clearContent()
	{
		for (int i = 0 ; i < getInventory().getSlots() ; i++)
		{
			getInventory().setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	/**
	 * Returns the index to the first non-empty {@link ItemStack} in the inventory or
	 * {@code -1} if no such item exists.
	 *
	 * @return The index.
	 */
	default int getFirstStackIndex()
	{
		for (int i = 0 ; i < getInventory().getSlots() ; i++)
		{
			if (!getItem(i).isEmpty())
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Add an item to the inventory, returning the remainder. The provided stack is not
	 * mutated.
	 *
	 * @param stack The stack to insert.
	 * @return The remainder, if the stack could not be fully inserted.
	 */
	default ItemStack giveItem(final ItemStack stack)
	{
		return insertItemInto(getInventory(), stack);
	}

	/**
	 * @return Whether the inventory should eject any items inside.
	 */
	default boolean shouldEjectItems()
	{
		return false;
	}

	/**
	 * Returns true if the item in the given slot can be ejected.
	 *
	 * @param slot The slot to check.
	 * @return Whether the item in the slot can be ejected.
	 */
	default boolean canEjectSlot(int slot)
	{
		return true;
	}

	/**
	 * Get the {@link Direction} to eject items at.
	 *
	 * @param state The {@link BlockState} of the implementing block entity.
	 * @return The direction to eject items at.
	 */
	default Direction getEjectDirection(BlockState state)
	{
		if (state.hasProperty(HorizontalDirectionalBlock.FACING))
		{
			return state.getValue(HorizontalDirectionalBlock.FACING);
		}
		else if (state.hasProperty(DirectionalBlock.FACING))
		{
			return state.getValue(DirectionalBlock.FACING);
		}
		else
		{
			return Direction.NORTH;
		}
	}

	/**
	 * Gets a random slot with at least 1 item in it.
	 *
	 * @param random Random source to use.
	 * @return The index to the random slot.
	 */
	default int getRandomUsedSlot(RandomSource random)
	{
		// Code from the dispenser functionality
		int slot = -1;
		int j = 1;
		IItemHandler inventory = getInventory();

		for (int i = 0 ; i < inventory.getSlots() ; i++)
		{
			if (!inventory.getStackInSlot(i).isEmpty() && random.nextInt(j++) == 0)
			{
				slot = i;
			}
		}

		return slot;
	}

	/**
	 * Gets a random slot with at least {@code minimumCount} items in it, or a slot where
	 * the maximum stack size equals 1.
	 *
	 * @param random       Random source to use.
	 * @param minimumCount The minimum count for a stack to be picked.
	 * @return The index to the random slot. Returns to -1 if no slots match.
	 */
	default int getRandomUsedSlot(RandomSource random, int minimumCount)
	{
		IItemHandler inventory = getInventory();
		List<Integer> possibleSlots = new ArrayList<>();
		for (int i = 0 ; i < inventory.getSlots() ; i++)
		{
			if (inventory.getStackInSlot(i).getCount() >= minimumCount)
			{
				possibleSlots.add(i);
			}
		}
		if (possibleSlots.isEmpty())
		{
			return -1;
		}
		return possibleSlots.get(random.nextInt(possibleSlots.size()));
	}

	/**
	 * Ejects an {@link ItemStack} into the world or into a valid container. This method
	 * does not care what the ejecting block is.
	 *
	 * @param level     The level.
	 * @param pos       The position of the block ejecting the stack.
	 * @param direction The direction to eject in.
	 * @param toEject   The stack to eject.
	 * @return If the stack could not be ejected (i.e, there is a block in the way or the
	 * container was full), this is the remainder.
	 */
	static ItemStack ejectStack(ServerLevel level, BlockPos pos, Direction direction, ItemStack toEject)
	{
		// Adapted from net.minecraft.core.dispenser.DefaultDispenseItemBehavior
		BlockPos ejectBlockPos = pos.relative(direction);
		Position ejectPos = ejectBlockPos.getCenter();

		if (toEject.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		// If the block in the eject direction has an item handler, we can try to dump the item into it instantly
		IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, ejectBlockPos, direction);
		if (itemHandler != null)
		{
			ItemStack remainder = insertItemInto(itemHandler, toEject);

			level.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pos, 0);
			level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, direction.get3DDataValue());

			return remainder;
		}

		// If the block in the eject direction did not have an item handler and is a full block, we cannot eject
		if (level.getBlockState(ejectBlockPos).isCollisionShapeFullBlock(level, ejectBlockPos))
		{
			return toEject;
		}

		// No obstructions, so we can eject the item
		double yPos = ejectPos.y();
		if (direction.getAxis() == Direction.Axis.Y)
		{
			yPos -= 0.125;
		}
		else
		{
			yPos -= 0.15625;
		}

		ItemEntity ie = new ItemEntity(level, ejectPos.x(), yPos, ejectPos.z(), toEject);
		ie.setDeltaMovement(0, 0, 0);
		level.addFreshEntity(ie);

		level.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pos, 0);
		level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, direction.get3DDataValue());
		return ItemStack.EMPTY;
	}

	/**
	 * Ejects an {@link ItemStack} into the world or into a valid container from an
	 * {@link IInventoryBE}.
	 *
	 * @param level     The level.
	 * @param pos       The position of the block ejecting the stack.
	 * @param direction The direction to eject in.
	 * @param slot      The slot to eject from.
	 * @param maxCount  The maximum amount of items to eject.
	 */
	static void ejectFrom(ServerLevel level, BlockPos pos, Direction direction, int slot, int maxCount)
	{
		if (!(level.getBlockEntity(pos) instanceof IInventoryBE<?> be))
		{
			return;
		}

		int i = slot == -1 ? be.getRandomUsedSlot(level.random) : slot;
		if (i < 0 || i > be.getInventory().getSlots())
		{
			return;
		}

		if (!be.canEjectSlot(i))
		{
			return;
		}

		ItemStack stack = be.getItem(i).copy();
		if (!stack.isEmpty())
		{
			// Adapted from net.minecraft.core.dispenser.DefaultDispenseItemBehavior
			BlockPos ejectBlockPos = pos.relative(direction);
			Position ejectPos = ejectBlockPos.getCenter();
			ItemStack toEject = stack.split(Math.min(stack.getCount(), maxCount));

			if (toEject.isEmpty())
			{
				return;
			}

			// If the block in the eject direction has an item handler, we can try to dump the item into it instantly
			IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, ejectBlockPos, direction);
			if (itemHandler != null)
			{
				ItemStack remainder = insertItemInto(itemHandler, toEject);

				level.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pos, 0);
				level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, direction.get3DDataValue());

				// Merge the remainder and the pre-existing stack
				stack.grow(remainder.getCount());

				be.setItem(i, stack);
				return;
			}

			// If the block in the eject direction did not have an item handler and is a full block, we cannot eject
			if (level.getBlockState(ejectBlockPos).isCollisionShapeFullBlock(level, ejectBlockPos))
			{
				return;
			}

			// No obstructions, so we can eject the item
			double yPos = ejectPos.y();
			if (direction.getAxis() == Direction.Axis.Y)
			{
				yPos -= 0.125;
			}
			else
			{
				yPos -= 0.15625;
			}

			ItemEntity ie = new ItemEntity(level, ejectPos.x(), yPos, ejectPos.z(), toEject);
			ie.setDeltaMovement(0, 0, 0);
			level.addFreshEntity(ie);

			level.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pos, 0);
			level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, direction.get3DDataValue());
			be.setItem(i, stack);
		}
	}

	/**
	 * Ejects an {@link ItemStack} into the world or into a valid container from an
	 * {@link IInventoryBE}.
	 *
	 * @param be        The block entity to eject an item from.
	 * @param maxAmount The maximum amount of items to eject.
	 * @param <T>       The container type of the block entity.
	 */
	static <T extends IItemHandler & IItemHandlerModifiable & INBTSerializable<CompoundTag>> void ejectFrom(IInventoryBE<T> be, int maxAmount)
	{
		ejectFrom((ServerLevel) be.getLevel(), be.getBlockPos(), be.getEjectDirection(be.getBlockState()), -1, maxAmount);
	}

	/**
	 * Ejects an {@link ItemStack} into the world or into a valid container from an
	 * {@link IInventoryBE}.
	 *
	 * @param be        The block entity to eject an item from.
	 * @param maxAmount The maximum amount of items to eject.
	 * @param slot      The slot to eject from.
	 * @param <T>       The container type of the block entity.
	 */
	static <T extends IItemHandler & IItemHandlerModifiable & INBTSerializable<CompoundTag>> void ejectFrom(IInventoryBE<T> be, int slot, int maxAmount)
	{
		ejectFrom((ServerLevel) be.getLevel(), be.getBlockPos(), be.getEjectDirection(be.getBlockState()), slot, maxAmount);
	}

	/**
	 * Inserts an item into the provided item handler. Use {@code startIndex} and
	 * {@code endIndex} to only insert into a specific range of slots.
	 *
	 * @param itemHandler The item handler to insert into.
	 * @param stack       The stack to insert.
	 * @param startIndex  The first index to check (inclusive).
	 * @param endIndex    The last index to check (exclusive).
	 * @return The remainder item stack if not all items could be inserted.
	 */
	static ItemStack insertItemInto(IItemHandler itemHandler, ItemStack stack, int startIndex, int endIndex)
	{
		ItemStack copy = stack.copy();
		for (int i = startIndex ; i < endIndex ; i++)
		{
			// Insert the item and yoink the remainder
			ItemStack remainder = itemHandler.insertItem(i, copy, false);
			// If the remainder is empty or its count is different from the stack's, then something changed
			if (remainder.isEmpty() || remainder.getCount() != copy.getCount())
			{
				if (remainder.isEmpty())
				{
					return ItemStack.EMPTY;
				}
				copy = remainder;
			}
		}
		return copy;
	}

	/**
	 * Inserts an item into the provided item handler. Use {@code startIndex} and
	 * {@code endIndex} to only insert into a specific range of slots.
	 *
	 * @param itemHandler The item handler to insert into.
	 * @param stack       The stack to insert.
	 * @return The remainder item stack if not all items could be inserted.
	 */
	static ItemStack insertItemInto(IItemHandler itemHandler, ItemStack stack)
	{
		return insertItemInto(itemHandler, stack, 0, itemHandler.getSlots());
	}

	/**
	 * Returns true if the given handler has no empty slots.
	 *
	 * @param handler The handler to check.
	 * @return Whether the provided inventory is full.
	 */
	static boolean isFull(IItemHandler handler)
	{
		for (int i = 0 ; i < handler.getSlots() ; i++)
		{
			if (handler.getStackInSlot(i).isEmpty())
			{
				return false;
			}
		}
		return true;
	}
}
