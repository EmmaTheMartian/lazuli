package top.girlkisser.lazuli.common;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.lazuli.Lazuli;

import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
@ApiStatus.NonExtendable
public interface LazuliDataComponents
{
	DeferredRegister.DataComponents R = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Lazuli.MODID);

	static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> reg(String id, UnaryOperator<DataComponentType.Builder<T>> builder)
	{
		return R.registerComponentType(id, builder);
	}

	/**
	 * A data component to contain any fluid stack.
	 */
	DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> GENERIC_FLUID = reg("fluid",
		b -> b
			.persistent(SimpleFluidContent.CODEC)
			.networkSynchronized(SimpleFluidContent.STREAM_CODEC));
}
