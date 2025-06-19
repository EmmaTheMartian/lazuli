package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An {@link AbstractEnergyBE} that distributes power every tick and cannot receive power.
 */
public abstract class AbstractGeneratorBE extends AbstractEnergyBE implements ITickableBE
{
	public AbstractGeneratorBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
	{
		super(type, pos, blockState);
	}

	@Override
	public int getMaxEnergyReceive()
	{
		return 0;
	}

	@Override
	public void serverTick(ServerLevel level)
	{
		this.distributePower();
	}
}
