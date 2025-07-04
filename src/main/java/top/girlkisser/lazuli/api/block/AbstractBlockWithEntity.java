package top.girlkisser.lazuli.api.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * A block class which provides helpers for block entities and tickers.
 *
 * @param <T> The block entity's type
 */
public abstract class AbstractBlockWithEntity<T extends BlockEntity> extends Block implements EntityBlock
{
	private final BlockEntityType.BlockEntitySupplier<T> blockEntityFactory;

	/**
	 * Whether this block entity has tickers on either the server or the client.
	 */
	protected boolean hasTicker = true;

	/**
	 * A block class which provides helpers for block entities and tickers.
	 *
	 * @param blockEntityFactory Factory to create the block entity.
	 * @param properties         Block properties.
	 */
	public AbstractBlockWithEntity(
		BlockEntityType.BlockEntitySupplier<T> blockEntityFactory,
		Properties properties
	)
	{
		super(properties);
		this.blockEntityFactory = blockEntityFactory;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		return blockEntityFactory.create(blockPos, blockState);
	}

	@Override
	public @Nullable <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, BlockState state, BlockEntityType<U> blockEntityType)
	{
		if (!hasTicker)
			return null;

		if (level.isClientSide)
			return (level_, pos, state_, be) ->
			{
				if (be instanceof ITickableBE tickableBE)
					tickableBE.clientTick((ClientLevel) level);
			};
		else
			return (level_, pos, state_, be) ->
			{
				if (be instanceof ITickableBE tickableBE)
					tickableBE.serverTick((ServerLevel) level);
			};
	}
}
