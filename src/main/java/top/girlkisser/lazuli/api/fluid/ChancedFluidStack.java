package top.girlkisser.lazuli.api.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * A record for a fluid stack which has a chance of being empty. This is mainly
 * used as a recipe output.
 *
 * @param stack  The fluid stack.
 * @param chance The chance that the fluid stack is not empty.
 */
public record ChancedFluidStack(FluidStack stack, float chance)
{
	public static final Codec<ChancedFluidStack> CODEC = RecordCodecBuilder.create(it -> it.group(
		FluidStack.CODEC.fieldOf("stack").forGetter(ChancedFluidStack::stack),
		Codec.FLOAT.fieldOf("chance").forGetter(ChancedFluidStack::chance)
	).apply(it, ChancedFluidStack::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ChancedFluidStack> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	/**
	 * A record for a fluid stack which has a chance of being empty. This is mainly
	 * used as a recipe output.
	 *
	 * @param fluid  The fluid.
	 * @param amount The amount of fluid in mB.
	 * @param chance The chance that the fluid stack is not empty.
	 */
	public ChancedFluidStack(Fluid fluid, int amount, float chance)
	{
		this(new FluidStack(fluid, amount), chance);
	}

	/**
	 * Get either the contained {@link FluidStack} or {@link FluidStack#EMPTY}.
	 *
	 * @param random The random source.
	 * @return The fluid stack.
	 */
	public FluidStack roll(RandomSource random)
	{
		return random.nextFloat() <= chance ? stack : FluidStack.EMPTY;
	}
}
