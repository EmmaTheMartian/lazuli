package top.girlkisser.lazuli.api.colour;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;

import java.util.List;

/**
 * Represents an RGBA colour in an unpacked form.
 *
 * @param r Red value from 0 to 255.
 * @param g Green value from 0 to 255.
 * @param b Blue value from 0 to 255.
 * @param a Alpha value from 0 to 255.
 */
public record UnpackedColour(int r, int g, int b, int a)
{
	public static final Codec<UnpackedColour> CODEC = RecordCodecBuilder.create(it -> it.group(
		Codec.INT.fieldOf("r").forGetter(UnpackedColour::r),
		Codec.INT.fieldOf("g").forGetter(UnpackedColour::g),
		Codec.INT.fieldOf("b").forGetter(UnpackedColour::b),
		Codec.INT.optionalFieldOf("a", 255).forGetter(UnpackedColour::a)
	).apply(it, UnpackedColour::new));

	public static final Codec<UnpackedColour> LIST_CODEC = Codec.INT.listOf(3, 4).xmap(
		from -> new UnpackedColour(from.getFirst(), from.get(1), from.get(2), from.size() > 3 ? from.get(3) : 255),
		to -> List.of(to.r, to.g, to.b, to.a)
	);

	public static final Codec<UnpackedColour> STRING_CODEC = Codec.STRING.xmap(
		from ->
		{
			int r = Integer.parseInt(from.substring(1, 3), 16);
			int g = Integer.parseInt(from.substring(2, 5), 16);
			int b = Integer.parseInt(from.substring(4, 7), 16);
			int a = from.length() == 9 ? Integer.parseInt(from.substring(6, 9), 16) : 255;
			return new UnpackedColour(r, g, b, a);
		},
		to -> "#" +
			Integer.toString(to.r, 16) +
			Integer.toString(to.g, 16) +
			Integer.toString(to.b, 16) +
			Integer.toString(to.a, 16)
	);

	public static final Codec<UnpackedColour> PACKED_CODEC = Codec.INT.xmap(UnpackedColour::new, UnpackedColour::pack);

	/**
	 * A codec for colours allowing a variety of formats:
	 * - RGB(A) as an object (<code>{"r":255,"g":100,"b":150,"a":255}</code>)
	 * - RGBA as a list (<code>[255, 100, 150, 255]</code>)
	 * - RGB as a list (<code>[255, 100, 150]</code>)
	 * - Hex (RGBA) codes as a string (<code>"#rrggbbaa"</code>)
	 * - Hex (RGB) codes as a string (<code>"#rrggbb"</code>)
	 * - Packed colours as an integer (<code>16777215</code>)
	 */
	public static final Codec<UnpackedColour> FLEXIBLE_CODEC = Codec.withAlternative(
		Codec.withAlternative(UnpackedColour.CODEC, UnpackedColour.LIST_CODEC),
		Codec.withAlternative(UnpackedColour.STRING_CODEC, UnpackedColour.PACKED_CODEC)
	);

	/**
	 * Creates an unpacked colour using the provided R, G, and B values (0-255).
	 *
	 * @param r Red value from 0 to 255.
	 * @param g Green value from 0 to 255.
	 * @param b Blue value from 0 to 255.
	 */
	public UnpackedColour(int r, int g, int b)
	{
		this(r, g, b, 255);
	}

	/**
	 * Creates an unpacked colour from the packed colour (expects ARGB32).
	 *
	 * @param packedColour The packed colour.
	 */
	public UnpackedColour(int packedColour)
	{
		this(
			FastColor.ARGB32.red(packedColour),
			FastColor.ARGB32.green(packedColour),
			FastColor.ARGB32.blue(packedColour),
			FastColor.ARGB32.alpha(packedColour)
		);
	}

	/**
	 * Gets the colour as a packed integer in ARGB32 format.
	 *
	 * @return The packed colour.
	 */
	public int pack()
	{
		return FastColor.ARGB32.color(a, r, g, b);
	}

	/**
	 * Converts the unpacked colour to a {@link ColorRGBA}.
	 *
	 * @return The ColorRGBA.
	 */
	public ColorRGBA toColorRGBA()
	{
		return new ColorRGBA(pack());
	}

	/**
	 * Add {@code random.nextInt(-variance, variance)} to the R, G, and B values.
	 *
	 * @param random The random source to use.
	 * @param variance How much to vary the ranges.
	 * @return A {@link UnpackedColour} with the new values.
	 */
	public UnpackedColour modulateRGB(RandomSource random, int variance)
	{
		return new UnpackedColour(
			r + Math.clamp(random.nextInt(-variance, variance), 0, 255),
			g + Math.clamp(random.nextInt(-variance, variance), 0, 255),
			b + Math.clamp(random.nextInt(-variance, variance), 0, 255),
			a
		);
	}

	/**
	 * Add {@code random.nextInt(-variance, variance)} to the alpha value.
	 *
	 * @param random The random source to use.
	 * @param variance How much to vary the range.
	 * @return A {@link UnpackedColour} with the new value.
	 */
	public UnpackedColour modulateAlpha(RandomSource random, int variance)
	{
		return new UnpackedColour(
			r,
			g,
			b,
			a + Math.clamp(random.nextInt(-variance, variance), 0, 255)
		);
	}
}

