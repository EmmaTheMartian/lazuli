package top.girlkisser.lazuli.common;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.lazuli.Lazuli;

import java.util.function.UnaryOperator;

/**
 * Common data components that can be used by mods depending on Lazuli.
 */
public final class LazuliDataComponents
{
	private LazuliDataComponents()
	{
	}

	/**
	 * JavaDoc is holding me hostage...
	 * <br/>
	 * I wonder if there's a way to make JavaDoc ignore fields with {@code @ApiStatus.Internal}...
	 */
	@ApiStatus.Internal
	public static final DeferredRegister.DataComponents R = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Lazuli.MODID);

	/**
	 * A data component to contain any fluid stack.
	 */
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> GENERIC_FLUID =
		R.registerComponentType("fluid", b -> b
			.persistent(SimpleFluidContent.CODEC)
			.networkSynchronized(SimpleFluidContent.STREAM_CODEC));
}
