package top.girlkisser.lazuli.api.mathematics;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/**
 * Provides a handful of helpers for math related things.
 */
public final class Mathematics
{
	private Mathematics()
	{
	}

	/**
	 * Checks if the provided point is within the rectangle.
	 *
	 * @param pointX The point's X coordinate.
	 * @param pointY The point's Y coordinate.
	 * @param rectX The rect's X coordinate.
	 * @param rectY The rect's Y coordinate.
	 * @param rectWidth The rect's width.
	 * @param rectHeight The rect's height.
	 * @return Whether the rect contains the point.
	 */
	public static boolean pointWithinRectangle(int pointX, int pointY, int rectX, int rectY, int rectWidth, int rectHeight)
	{
		return pointX >= rectX &&
			pointX < rectX + rectWidth &&
			pointY >= rectY &&
			pointY < rectY + rectHeight;
	}

	/**
	 * Get the direction normals for the given direction.
	 *
	 * @param direction The direction.
	 * @return The normals.
	 */
	public static Vec3 getDirectionNormals(Direction direction)
	{
		return switch (direction)
		{
			case UP -> new Vec3(0, 1, 0);
			case DOWN -> new Vec3(0, -1, 0);
			case NORTH -> new Vec3(0, 0, -1);
			case EAST -> new Vec3(1, 0, 0);
			case SOUTH -> new Vec3(0, 0, 1);
			case WEST -> new Vec3(-1, 0, 0);
		};
	}
}
