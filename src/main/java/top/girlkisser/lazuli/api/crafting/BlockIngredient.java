package top.girlkisser.lazuli.api.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An ingredient record for blocks or block tags.
 *
 * @param block The ingredient block, if any.
 * @param tag   The ingredient tag, if any.
 */
public record BlockIngredient(Optional<Block> block, Optional<TagKey<Block>> tag)
{
	/**
	 * An ingredient record for blocks or block tags.
	 *
	 * @param block The ingredient block.
	 */
	public BlockIngredient(Block block)
	{
		this(Optional.of(block), Optional.empty());
	}

	/**
	 * An ingredient record for blocks or block tags.
	 *
	 * @param blockTag The ingredient block tag.
	 */
	public BlockIngredient(TagKey<Block> blockTag)
	{
		this(Optional.empty(), Optional.of(blockTag));
	}

	/**
	 * Match the block state to the ingredient.
	 *
	 * @param state The block state to test.
	 * @return Whether the block state matches the ingredient.
	 */
	public boolean test(BlockState state)
	{
		return (block.isPresent() && state.is(block.get())) || (tag.isPresent() && state.is(tag.get()));
	}

	/**
	 * Converts the block ingredient to an item ingredient (using {@link Block#asItem()})
	 *
	 * @return The ingredient.
	 */
	public Ingredient toItemIngredient()
	{
		if (block.isPresent())
		{
			return Ingredient.of(block.get().asItem());
		}
		else if (tag.isPresent())
		{
			List<ItemLike> blocks = new ArrayList<>();
			BuiltInRegistries.BLOCK.getTag(tag.get()).ifPresent(tagEntries ->
				tagEntries.forEach(entry -> blocks.add(entry.value())));
			return Ingredient.of(blocks.toArray(new ItemLike[]{}));
		}

		throw new UnsupportedOperationException("Cannot invoke BlockIngredient.toNonBlockIngredient where neither block nor tag are present.");
	}

	/**
	 * Converts the block ingredient to a {@link BIRL} ("Block Ingredient from Resource Location").
	 *
	 * @return The BIRL.
	 */
	public BIRL toBIRL()
	{
		return new BIRL(
			block.map(BuiltInRegistries.BLOCK::getKey),
			tag.map(TagKey::location)
		);
	}

	/**
	 * A block ingredient where the block and tag are represented by their respective
	 * {@link ResourceLocation}s.
	 * <br/>
	 * Name stands for "Block Ingredient from Resource Location"
	 *
	 * @param block The block's identifier, if any.
	 * @param tag   The tag's identifier, if any.
	 */
	public record BIRL(Optional<ResourceLocation> block, Optional<ResourceLocation> tag)
	{
		/**
		 * Converts the BIRL to a {@link BlockIngredient}.
		 *
		 * @return The block ingredient.
		 */
		public BlockIngredient toIngredient()
		{
			return new BlockIngredient(
				block.map(BuiltInRegistries.BLOCK::get),
				tag.map(it -> TagKey.create(Registries.BLOCK, it))
			);
		}
	}

	public static final Codec<BIRL> BIRL_CODEC = RecordCodecBuilder.create(it -> it.group(
		ResourceLocation.CODEC.optionalFieldOf("block").forGetter(BIRL::block),
		ResourceLocation.CODEC.optionalFieldOf("tag").forGetter(BIRL::tag)
	).apply(it, BIRL::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, BIRL> BIRL_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(BIRL_CODEC);

	public static final Codec<BlockIngredient> CODEC = BIRL_CODEC.xmap(BIRL::toIngredient, BlockIngredient::toBIRL);
	public static final StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
