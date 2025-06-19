package top.girlkisser.lazuli.api.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A generic class for containers/menus for a block entity.
 *
 * @param <T> The block entity's that this menu is for.
 */
public abstract class AbstractBlockEntityContainer<T extends BlockEntity> extends AbstractLazuliContainer
{
	/** The container block entity's position. */
	public final BlockPos pos;

	/** The container block entity. */
	public final T blockEntity;

	/** The container's block. */
	protected final Block block;

	/**
	 * A generic class for containers/menus for a block entity.
	 *
	 * @param menuType The menu type.
	 * @param block The block.
	 * @param slotCount The amount of slots in the container.
	 * @param containerId The container ID.
	 * @param playerInventory The player's inventory.
	 * @param pos The position of the block.
	 */
	@SuppressWarnings("unchecked")
	public AbstractBlockEntityContainer(MenuType<?> menuType, Block block, int slotCount, int containerId, Inventory playerInventory, BlockPos pos)
	{
		super(menuType, slotCount, containerId, playerInventory);

		this.block = block;
		this.pos = pos;
		//noinspection resource
		this.blockEntity = (T) Objects.requireNonNull(
			playerInventory.player.level().getBlockEntity(pos),
			"AbstractMachineContainer created without a block entity"
		);
	}

	@Override
	public boolean stillValid(Player player)
	{
		return stillValid(ContainerLevelAccess.create(player.level(), this.pos), player, this.block);
	}
}
