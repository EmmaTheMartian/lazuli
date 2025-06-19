package top.girlkisser.lazuli.api.world;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Helpers for performing raycasts.
 */
public final class Raycasting
{
	private Raycasting()
	{
	}

	/**
	 * Performs a block raycast.
	 *
	 * @param entity    The entity to raycast from.
	 * @param reach     The maximum distance to check.
	 * @param hitFluids If the raycast should also hit fluids.
	 * @return The block that the raycast hit, if any.
	 */
	public static @Nullable BlockHitResult blockRaycast(Entity entity, double reach, boolean hitFluids)
	{
		HitResult hit = entity.pick(reach, 0, hitFluids);
		if (hit.getType() != HitResult.Type.BLOCK)
		{
			return null;
		}
		return (BlockHitResult) hit;
	}

	/**
	 * Performs an entity raycast.
	 *
	 * @param entity The entity to raycast from.
	 * @param reach  The maximum distance to check.
	 * @return The entity that the raycast hit, if any.
	 */
	public static @Nullable EntityHitResult entityRaycast(Entity entity, double reach)
	{
		HitResult hit = entity.pick(reach, 0, false);
		if (hit.getType() != HitResult.Type.ENTITY)
		{
			return null;
		}
		return (EntityHitResult) hit;
	}

	/**
	 * Performs a raycast.
	 *
	 * @param entity    The entity to raycast from.
	 * @param reach     The maximum distance to check.
	 * @param hitFluids If fluids should be hit.
	 * @return The HitResult.
	 */
	public static HitResult raycast(Entity entity, double reach, boolean hitFluids)
	{
		return entity.pick(reach, 0, hitFluids);
	}
}
