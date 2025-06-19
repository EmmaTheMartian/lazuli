package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Default methods for a block entity.
 * <br/>
 * You should never need to manually implement this since all block entities implement
 * these functions already. This is used by other block entity interfaces
 * ({@link IEnergyBE}, {@link IInventoryBE}, etc.) to access block entity information
 * without needing to be an abstract class that extends
 * {@link net.minecraft.world.level.block.entity.BlockEntity}.
 */
public interface IBE
{
	/**
	 * Get the block position of the block entity.
	 *
	 * @return The block position.
	 */
	BlockPos getBlockPos();

	/**
	 * Get the block state of the block entity.
	 *
	 * @return The block state.
	 */
	BlockState getBlockState();

	/**
	 * Get the level of the block entity.
	 *
	 * @return The level.
	 */
	Level getLevel();

	/**
	 * Mark the block entity as having changed.
	 */
	void setChanged();
}
