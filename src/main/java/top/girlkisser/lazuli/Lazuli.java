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

/**
 * Woohoo the Lazuli mod! :3
 * (JavaDoc is making me put a comment here. Please somebody ~~wake me up (/reference)~~ help)
 */
@Mod(Lazuli.MODID)
@ApiStatus.Internal
public class Lazuli
{
	/**
	 * Lazuli's mod ID
	 */
	public static final String MODID = "lazuli";

	/**
	 * Lazuli's logger.
	 */
	public static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * Don't ever use this. Kthxbai!
	 *
	 * @param bus  The wheels on the bus go round-and-round...
	 * @param mod  Round-and-round...
	 * @param dist Round-and-round...
	 */
	public Lazuli(IEventBus bus, ModContainer mod, Dist dist)
	{
		NeoForge.EVENT_BUS.register(LazuliListeners.GameEventListeners.class);
		if (dist.isClient())
		{
			NeoForge.EVENT_BUS.register(LazuliClientListeners.GameEventListeners.class);
		}

		LOGGER.info("Lazuli has loaded :3");

		LazuliDataComponents.R.register(bus);
	}
}
