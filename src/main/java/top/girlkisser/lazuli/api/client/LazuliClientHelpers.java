package top.girlkisser.lazuli.api.client;

import net.minecraft.client.Minecraft;

/**
 * Misc client helpers.
 */
public final class LazuliClientHelpers
{
	private LazuliClientHelpers()
	{
	}

	/**
	 * The amount of ticks that have passed since the game opened on the client.
	 */
	public static int clientTicks = 0;

	/**
	 * Gets whether the game is paused or not.
	 *
	 * @return `true` when the game is not paused.
	 */
	public static boolean isGameActive()
	{
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) && !Minecraft.getInstance().isPaused();
	}
}
