package top.girlkisser.lazuli.api.client;

import net.minecraft.client.Minecraft;

public class LazuliClientHelpers
{
	public static int clientTicks = 0;

	public static boolean isGameActive()
	{
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) && !Minecraft.getInstance().isPaused();
	}
}
