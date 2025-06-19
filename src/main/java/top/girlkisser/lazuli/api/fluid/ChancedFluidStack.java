package top.girlkisser.lazuli.api.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public record ChancedFluidStack(FluidStack stack, float chance)
{
	public static final Codec<ChancedFluidStack> CODEC = RecordCodecBuilder.create(it -> it.group(
		FluidStack.CODEC.fieldOf("stack").forGetter(ChancedFluidStack::stack),
		Codec.FLOAT.fieldOf("chance").forGetter(ChancedFluidStack::chance)
	).apply(it, ChancedFluidStack::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ChancedFluidStack> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public ChancedFluidStack(Fluid fluid, int amount, float chance)
	{
		this(new FluidStack(fluid, amount), chance);
	}

	public FluidStack roll(RandomSource random)
	{
		return random.nextFloat() <= chance ? stack : FluidStack.EMPTY;
	}
}
