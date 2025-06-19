package top.girlkisser.lazuli.api.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Predicate;

/**
 * An ingredient with a count.
 *
 * @param ingredient The ingredient.
 * @param count      The count.
 * @see top.girlkisser.lazuli.api.inventory.ContainerUtils#extractItems(Container, Predicate, int)
 * @see top.girlkisser.lazuli.api.inventory.ContainerUtils#countItems(Container, Predicate)
 * @see top.girlkisser.lazuli.api.inventory.ContainerUtils#extractItemsForCrafting(Container, Predicate, int)
 */
public record CountedIngredient(Ingredient ingredient, int count)
{
	public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(it -> it.group(
		Ingredient.CODEC.fieldOf("ingredient").forGetter(CountedIngredient::ingredient),
		Codec.INT.fieldOf("count").forGetter(CountedIngredient::count)
	).apply(it, CountedIngredient::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CountedIngredient> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
