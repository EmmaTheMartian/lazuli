package top.girlkisser.lazuli.api.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * A record for an item stack which has a chance of being empty. This is mainly
 * used as a recipe output.
 *
 * @param stack The item stack.
 * @param chance The chance that the item stack is not empty.
 */
public record ChancedItemStack(ItemStack stack, float chance)
{
	public static final Codec<ChancedItemStack> CODEC = RecordCodecBuilder.create(it -> it.group(
		ItemStack.CODEC.fieldOf("stack").forGetter(ChancedItemStack::stack),
		Codec.FLOAT.fieldOf("chance").forGetter(ChancedItemStack::chance)
	).apply(it, ChancedItemStack::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ChancedItemStack> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	/**
	 * A record for an item stack which has a chance of being empty. This is mainly
	 * used as a recipe output.
	 *
	 * @param item The item.
	 * @param chance The chance that the item stack is not empty.
	 */
	public ChancedItemStack(ItemLike item, float chance)
	{
		this(item.asItem().getDefaultInstance(), chance);
	}

	/**
	 * A record for an item stack which has a chance of being empty. This is mainly
	 * used as a recipe output.
	 *
	 * @param item The item.
	 * @param count The item's count.
	 * @param chance The chance that the item stack is not empty.
	 */
	public ChancedItemStack(ItemLike item, int count, float chance)
	{
		this(new ItemStack(item.asItem(), count), chance);
	}

	/**
	 * Get either the contained {@link ItemStack} or {@link ItemStack#EMPTY}.
	 *
	 * @param random The random source.
	 * @return The item stack.
	 */
	public ItemStack roll(RandomSource random)
	{
		return random.nextFloat() <= chance ? stack.copy() : ItemStack.EMPTY;
	}
}
