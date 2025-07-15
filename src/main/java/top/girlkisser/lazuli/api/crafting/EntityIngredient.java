package martian.minefactorial.api.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

/**
 * An ingredient record for entities or entity tags.
 *
 * @param entity The ingredient entity, if any.
 * @param tag The ingredient tag, if any.
 */
public record EntityIngredient(
	Optional<EntityType<?>> entity,
	Optional<TagKey<EntityType<?>>> tag
)
{
	public static final MapCodec<EntityIngredient> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
		BuiltInRegistries.ENTITY_TYPE.byNameCodec().optionalFieldOf("entity").forGetter(EntityIngredient::entity),
		TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("tag").forGetter(EntityIngredient::tag)
	).apply(it, EntityIngredient::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, EntityIngredient> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

	/**
	 * Test the entity ingredient against a given {@link EntityType}.
	 *
	 * @param type The entity type.
	 * @return If the ingredient matches the provided entity.
	 */
	public boolean test(EntityType<?> type)
	{
		if (entity.isPresent() && tag.isPresent())
			throw new RuntimeException("EntityIngredient was provided with both a specific entity type and a tag. Check your recipes!");
		else if (entity.isEmpty() && tag.isEmpty())
			throw new RuntimeException("EntityIngredient was provided with neither a specific entity type nor a tag. Check your recipes!");

		return entity
			.map(entityType -> entityType == type)
			.orElseGet(() -> type.is(tag.get()));
	}

	/**
	 * Create an entity ingredient using the provided {@link EntityType}.
	 *
	 * @param type The type.
	 * @return The ingredient.
	 */
	public static EntityIngredient of(EntityType<?> type)
	{
		return new EntityIngredient(Optional.ofNullable(type), Optional.empty());
	}

	/**
	 * Create an entity ingredient using the provided entity tag.
	 *
	 * @param tag The tag.
	 * @return The ingredient.
	 */
	public static EntityIngredient of(TagKey<EntityType<?>> tag)
	{
		return new EntityIngredient(Optional.empty(), Optional.of(tag));
	}
}
