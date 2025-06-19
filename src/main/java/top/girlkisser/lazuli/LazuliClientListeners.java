package top.girlkisser.lazuli;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import top.girlkisser.lazuli.api.client.LazuliClientHelpers;
import top.girlkisser.lazuli.api.misc.TickScheduler;

final class LazuliClientListeners
{
	@EventBusSubscriber(modid = Lazuli.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
	public static class GameEventListeners
	{
		@SubscribeEvent
		static void onTick(ClientTickEvent.Post event)
		{
			if (!LazuliClientHelpers.isGameActive())
				return;

			LazuliClientHelpers.clientTicks++;

			TickScheduler.CLIENT.tick();
		}
	}
}
