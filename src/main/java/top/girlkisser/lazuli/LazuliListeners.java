package top.girlkisser.lazuli;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import top.girlkisser.lazuli.api.misc.TickScheduler;

final class LazuliListeners
{
	@EventBusSubscriber(modid = Lazuli.MODID, bus = EventBusSubscriber.Bus.GAME)
	static class GameEventListeners
	{
		@SubscribeEvent
		static void onServerTick(ServerTickEvent.Post event)
		{
			TickScheduler.SERVER.tick();
		}
	}
}
