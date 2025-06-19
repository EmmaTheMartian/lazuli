package top.girlkisser.lazuli;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import top.girlkisser.lazuli.common.LazuliDataComponents;

@Mod(Lazuli.MODID)
@ApiStatus.Internal
public class Lazuli
{
	public static final String MODID = "lazuli";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Lazuli(IEventBus bus, ModContainer mod, Dist dist)
	{
		NeoForge.EVENT_BUS.register(LazuliListeners.GameEventListeners.class);
		if (dist.isClient())
		{
			NeoForge.EVENT_BUS.register(CygnusClientListeners.GameEventListeners.class);
		}

		LOGGER.info("Lazuli has loaded :3");

		LazuliDataComponents.R.register(bus);
	}
}
