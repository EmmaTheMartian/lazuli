package top.girlkisser.lazuli.api.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Implements methods for ticking on the server and the client.
 */
public interface ITickableBE
{
	/**
	 * Invoked on the server every tick.
	 *
	 * @param level The level.
	 */
	default void serverTick(ServerLevel level)
	{
	}

	/**
	 * Invoked on the client every tick.
	 *
	 * @param level The level.
	 */
	@OnlyIn(Dist.CLIENT)
	default void clientTick(ClientLevel level)
	{
	}
}
