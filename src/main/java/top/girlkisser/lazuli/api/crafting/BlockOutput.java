package top.girlkisser.lazuli.api.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * A record for recipes that output blocks.
 *
 * @param block
 */
public record BlockOutput(Block block)
{
	/**
	 * Converts the block output to a {@link BORL} ("Block Output from Resource Location").
	 *
	 * @return The BORL.
	 */
	public BORL toBORL()
	{
		return new BORL(BuiltInRegistries.BLOCK.getKey(block));
	}

	/**
	 * A block output where the block is represented by its {@link ResourceLocation}.
	 * <br/>
	 * Stands for "Block Output from Resource Location"
	 *
	 * @param block
	 */
	public record BORL(ResourceLocation block)
	{
		/**
		 * Converts the BORL to a {@link BlockOutput}.
		 *
		 * @return The BlockOutput.
		 */
		public BlockOutput toBlockOutput()
		{
			return new BlockOutput(BuiltInRegistries.BLOCK.get(block));
		}
	}

	public static final Codec<BORL> BORL_CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.fieldOf("block").forGetter(BORL::block)
	).apply(it, BORL::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, BORL> BORL_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(BORL_CODEC);

	public static final Codec<BlockOutput> CODEC = BORL_CODEC.xmap(BORL::toBlockOutput, BlockOutput::toBORL);
	public static final StreamCodec<RegistryFriendlyByteBuf, BlockOutput> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
