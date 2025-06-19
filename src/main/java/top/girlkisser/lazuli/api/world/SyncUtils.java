package top.girlkisser.lazuli.api.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Provides tiny utilities for syncing blocks and block entities.
 */
public final class SyncUtils
{
	private SyncUtils()
	{
	}

	/**
	 * Update clients of the block state at the given position.
	 *
	 * @param level The level.
	 * @param pos The block position.
	 */
	public static void sync(Level level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);
		level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
	}

	/**
	 * Sync the block and its block entity at the given location.
	 *
	 * @param be The block entity
	 */
	public static void sync(BlockEntity be)
	{
		if (be.getLevel() != null)
		{
			sync(be.getLevel(), be.getBlockPos());
			be.setChanged();
		}
	}
}
