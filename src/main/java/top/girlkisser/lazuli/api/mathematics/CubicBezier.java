package top.girlkisser.lazuli.api.mathematics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector2f;
import top.girlkisser.lazuli.api.codec.LazuliExtraCodecs;

import java.util.ArrayList;
import java.util.List;

/**
 * A cubic Bézier curve implementation.
 *
 * @param start    The starting position.
 * @param control1 The first control point.
 * @param control2 The second control point.
 * @param end      The ending position.
 */
public record CubicBezier(Vector2f start, Vector2f control1, Vector2f control2, Vector2f end)
{
	/**
	 * A codec for a cubic Bézier curve with the following format:
	 * <br/>
	 * <code>
	 * {
	 * "start": { "x": 0.0, "y": 0.0 },
	 * "control1": { "x": 0.25, "y": 0.5 },
	 * "control2": { "x": 0.75, "y": 0.2 },
	 * "end": { "x": 1.0, "y": 1.0 }
	 * }
	 * </code>
	 */
	public static final Codec<CubicBezier> CODEC = RecordCodecBuilder.create(it -> it.group(
		LazuliExtraCodecs.VECTOR2F.fieldOf("start").forGetter(CubicBezier::start),
		LazuliExtraCodecs.VECTOR2F.fieldOf("control1").forGetter(CubicBezier::control1),
		LazuliExtraCodecs.VECTOR2F.fieldOf("control2").forGetter(CubicBezier::control2),
		LazuliExtraCodecs.VECTOR2F.fieldOf("end").forGetter(CubicBezier::end)
	).apply(it, CubicBezier::new));

	public static final StreamCodec<ByteBuf, CubicBezier> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	/**
	 * A codec for a cubic Bézier curve with the following format:
	 * <br/>
	 * <code>
	 * [
	 * { "x": 0.0, "y": 0.0 },
	 * { "x": 0.25, "y": 0.5 },
	 * { "x": 0.75, "y": 0.2 },
	 * { "x": 1.0, "y": 1.0 }
	 * ]
	 * </code>
	 */
	public static final Codec<CubicBezier> LIST_CODEC = LazuliExtraCodecs.VECTOR2F.listOf(4, 4).xmap(
		(List<Vector2f> value) -> new CubicBezier(value.getFirst(), value.get(1), value.get(2), value.get(3)),
		(CubicBezier value) ->
		{
			List<Vector2f> points = new ArrayList<>();
			points.add(value.start);
			points.add(value.control1);
			points.add(value.control2);
			points.add(value.end);
			return points;
		}
	);

	public static final StreamCodec<ByteBuf, CubicBezier> LIST_STREAM_CODEC = ByteBufCodecs.fromCodec(LIST_CODEC);

	/**
	 * Create a cubic Bézier curve using the provided control points. The start and end
	 * points are 0,0 and 1,1 respectively.
	 *
	 * @param control1 The first control point.
	 * @param control2 The second control point.
	 */
	public CubicBezier(Vector2f control1, Vector2f control2)
	{
		this(new Vector2f(0, 0), control1, control2, new Vector2f(1, 1));
	}

	/**
	 * Create a cubic Bézier curve using the provided coordinates for control points. The
	 * start and end points are 0,0 and 1,1 respectively.
	 *
	 * @param control1X The first control point's X coordinate.
	 * @param control1Y The first control point's Y coordinate.
	 * @param control2X The second control point's X coordinate.
	 * @param control2Y The second control point's Y coordinate.
	 */
	public CubicBezier(float control1X, float control1Y, float control2X, float control2Y)
	{
		this(new Vector2f(0, 0), new Vector2f(control1X, control1Y), new Vector2f(control2X, control2Y), new Vector2f(1, 1));
	}

	/**
	 * Create a cubic Bézier curve using the provided coordinates for points.
	 *
	 * @param startX    The start point's X coordinate.
	 * @param startY    The start point's Y coordinate.
	 * @param control1X The first control point's X coordinate.
	 * @param control1Y The first control point's Y coordinate.
	 * @param control2X The second control point's X coordinate.
	 * @param control2Y The second control point's Y coordinate.
	 * @param endX      The end point's X coordinate.
	 * @param endY      The end point's Y coordinate.
	 */
	public CubicBezier(float startX, float startY, float control1X, float control1Y, float control2X, float control2Y, float endX, float endY)
	{
		this(new Vector2f(startX, startY), new Vector2f(control1X, control1Y), new Vector2f(control2X, control2Y), new Vector2f(endX, endY));
	}

	/**
	 * Get the point for the given `t` value.
	 *
	 * @param t The "progress" of the curve, from 0 to 1.
	 * @return The point at the given `t` value.
	 */
	public Vector2f sample(float t)
	{
		float m = 1 - t;
		float x = m * m * m * start.x + 3 * m * m * t * control1.x + 3 * m * t * t * control2.x + t * t * t * end.x;
		float y = m * m * m * start.y + 3 * m * m * t * control1.y + 3 * m * t * t * control2.y + t * t * t * end.y;
		return new Vector2f(x, y);
	}

	/**
	 * Get a list of points from 0 to 1 with `resolution` steps.
	 *
	 * @param resolution The amount of steps, or "resolution" of the points.
	 * @return The list of points.
	 */
	public List<Vector2f> getPoints(float resolution)
	{
		List<Vector2f> points = new ArrayList<>();
		for (float t = resolution ; t < 1 ; t += resolution)
		{
			points.add(sample(t));
		}
		return points;
	}
}
