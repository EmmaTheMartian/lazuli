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
 * A quadratic Bézier curve implementation.
 *
 * @param start   The starting position.
 * @param control The control point.
 * @param end     The ending position.
 */
public record QuadraticBezier(Vector2f start, Vector2f control, Vector2f end)
{
	/**
	 * A codec for a quadratic Bézier curve with the following format:
	 * <br/>
	 * <code>
	 * {
	 * "start": { "x": 0.0, "y": 0.0 },
	 * "control": { "x": 0.25, "y": 0.5 },
	 * "end": { "x": 1.0, "y": 1.0 }
	 * }
	 * </code>
	 */
	public static final Codec<QuadraticBezier> CODEC = RecordCodecBuilder.create(it -> it.group(
		LazuliExtraCodecs.VECTOR2F.fieldOf("start").forGetter(QuadraticBezier::start),
		LazuliExtraCodecs.VECTOR2F.fieldOf("control").forGetter(QuadraticBezier::control),
		LazuliExtraCodecs.VECTOR2F.fieldOf("end").forGetter(QuadraticBezier::end)
	).apply(it, QuadraticBezier::new));

	public static final StreamCodec<ByteBuf, QuadraticBezier> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	/**
	 * A codec for a quadratic Bézier curve with the following format:
	 * <br/>
	 * <code>
	 * [
	 * { "x": 0.0, "y": 0.0 },
	 * { "x": 0.5, "y": 0.5 },
	 * { "x": 1.0, "y": 1.0 }
	 * ]
	 * </code>
	 */
	public static final Codec<QuadraticBezier> LIST_CODEC = LazuliExtraCodecs.VECTOR2F.listOf(3, 3).xmap(
		(List<Vector2f> value) -> new QuadraticBezier(value.getFirst(), value.get(1), value.get(2)),
		(QuadraticBezier value) ->
		{
			List<Vector2f> points = new ArrayList<>();
			points.add(value.start);
			points.add(value.control);
			points.add(value.end);
			return points;
		}
	);

	public static final StreamCodec<ByteBuf, QuadraticBezier> LIST_STREAM_CODEC = ByteBufCodecs.fromCodec(LIST_CODEC);

	/**
	 * Create a quadratic Bézier curve using the provided control point. The start and end
	 * points are 0,0 and 1,1 respectively.
	 *
	 * @param control The control point.
	 */
	public QuadraticBezier(Vector2f control)
	{
		this(new Vector2f(0, 0), control, new Vector2f(1, 1));
	}

	/**
	 * Create a quadratic Bézier curve using the provided coordinates for the control point.
	 *
	 * @param controlX The control point's X coordinate.
	 * @param controlY The control point's Y coordinate.
	 */
	public QuadraticBezier(float controlX, float controlY)
	{
		this(new Vector2f(0, 0), new Vector2f(controlX, controlY), new Vector2f(1, 1));
	}

	/**
	 * Create a quadratic Bézier curve using the provided coordinates for points.
	 *
	 * @param startX   The start point's X coordinate.
	 * @param startY   The start point's Y coordinate.
	 * @param controlX The control point's X coordinate.
	 * @param controlY The control point's Y coordinate.
	 * @param endX     The end point's X coordinate.
	 * @param endY     The end point's Y coordinate.
	 */
	public QuadraticBezier(float startX, float startY, float controlX, float controlY, float endX, float endY)
	{
		this(new Vector2f(startX, startY), new Vector2f(controlX, controlY), new Vector2f(endX, endY));
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
		float x = m * m * start.x + 2 * m * t * control.x + t * t * end.x;
		float y = m * m * start.y + 2 * m * t * control.y + t * t * end.y;
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
