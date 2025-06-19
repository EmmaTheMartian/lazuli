package top.girlkisser.lazuli.testmod;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(LazuliTestMod.MODID)
public class LazuliTestMod
{
	public static final String MODID = "lazuli_test";
	public static final Logger LOGGER = LogUtils.getLogger();

	public LazuliTestMod(IEventBus bus, ModContainer mod)
	{
		LOGGER.info("Lazuli Test Mod has loaded :3");
	}
}
