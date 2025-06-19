package top.girlkisser.lazuli.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A block entity with a "working zone."
 */
public interface IZonedBE extends IBE
{
	/**
	 * Gets the work zone for the block entity. Generally you should prefer {@link IZonedBE#getCachedWorkZone()}
	 *
	 * @return The work zone for the block entity.
	 */
	default AABB getWorkZone()
	{
		BlockPos origin = getBlockPos();

		boolean hasFacing = false;
		Direction facing = Direction.NORTH;
		if (getBlockState().hasProperty(DirectionalBlock.FACING))
		{
			hasFacing = true;
			facing = getBlockState().getValue(DirectionalBlock.FACING);
		}
		else if (getBlockState().hasProperty(HorizontalDirectionalBlock.FACING))
		{
			hasFacing = true;
			facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		}

		if (hasFacing)
		{
			origin = origin.relative(facing, getWorkZoneRange() + 1);
		}

		BlockPos start = origin
			.relative(facing.getOpposite(), getWorkZoneRange())
			.relative(facing.getCounterClockWise(), getWorkZoneRange());
		BlockPos end = origin
			.relative(facing, getWorkZoneRange())
			.relative(facing.getClockWise(), getWorkZoneRange())
			.relative(Direction.UP, getWorkZoneHeight() - 1);

		return AABB.encapsulatingFullBlocks(start, end);
	}

	default int getWorkZoneRange()
	{
		return 1;
	}

	default int getWorkZoneHeight()
	{
		return 1;
	}

	Lazy<AABB> getCachedWorkZone();

	/**
	 * Invalidates the cached work zone.
	 */
	default void invalidateWorkZone()
	{
		getCachedWorkZone().invalidate();
	}

	/**
	 * Finds the first entity within the work zone.
	 *
	 * @param class_ The entity class to find. Use {@code Entity.class} for "any."
	 * @param filter A filter to apply.
	 * @return The first entity, if any.
	 * @param <T> The entity type to find.
	 */
	default <T extends Entity> Optional<T> getFirstEntityInWorkZone(Class<T> class_, @Nullable Predicate<T> filter)
	{
		return getEntitiesInWorkZone(class_, filter).stream().findFirst();
	}

	/**
	 * Get all entities in the work zone that are of {@code class_} and match {@code filter}.
	 *
	 * @param class_ The entity class to find. Use {@code Entity.class} for "any."
	 * @param filter A filter to apply.
	 * @return The first entity, if any.
	 * @param <T> The entity type to find.
	 */
	default <T extends Entity> List<T> getEntitiesInWorkZone(Class<T> class_, @Nullable Predicate<T> filter)
	{
		List<T> found = getLevel().getEntitiesOfClass(class_, getCachedWorkZone().get());
		if (filter != null)
		{
			found.removeIf(filter.negate());
		}
		return found;
	}

	Predicate<Entity> IS_PLAYER = e -> e instanceof Player;
	Predicate<Entity> NOT_PLAYER = Predicate.not(IS_PLAYER);
	Predicate<Entity> IS_ITEM = e -> e instanceof ItemEntity;
	Predicate<Entity> NOT_ITEM = Predicate.not(IS_ITEM);
	Predicate<LivingEntity> IS_BABY = LivingEntity::isBaby;
	Predicate<LivingEntity> NOT_BABY = Predicate.not(IS_BABY);
	Predicate<LivingEntity> IS_ADULT_ANIMAL = NOT_BABY.and(NOT_PLAYER).and(NOT_ITEM);
	Predicate<LivingEntity> IS_BABY_ANIMAL = IS_BABY.and(NOT_PLAYER).and(NOT_ITEM);
}
