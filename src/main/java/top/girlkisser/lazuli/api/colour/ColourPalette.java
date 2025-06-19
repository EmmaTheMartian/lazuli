package top.girlkisser.lazuli.api.colour;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.Arrays;
import java.util.List;

/**
 * A palette of {@link UnpackedColour}s.
 *
 * @param colours
 */
public record ColourPalette(UnpackedColour... colours)
{
	public static final Codec<ColourPalette> CODEC = RecordCodecBuilder.create(it -> it.group(
		Codec.list(UnpackedColour.CODEC).fieldOf("colours").forGetter(colourPalette -> Arrays.asList(colourPalette.colours))
	).apply(it, ColourPalette::new));

	public static final Codec<ColourPalette> PACKED_CODEC = RecordCodecBuilder.create(it -> it.group(
		Codec.list(Codec.INT).xmap(
			packedColours -> packedColours.stream().map(UnpackedColour::new).toList(),
			unpackedColours -> unpackedColours.stream().map(UnpackedColour::pack).toList()
		).fieldOf("colours").forGetter(colourPalette -> Arrays.asList(colourPalette.colours))
	).apply(it, ColourPalette::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ColourPalette> PACKED_STREAM_CODEC = StreamCodec.of(
		(buf, it) ->
		{
			buf.writeInt(it.colours.length);
			for (UnpackedColour colour : it.colours)
			{
				buf.writeInt(colour.pack());
			}
		},
		buf ->
		{
			int length = buf.readInt();
			UnpackedColour[] colours = new UnpackedColour[length];
			for (int i = 0 ; i < length ; i++)
			{
				colours[i] = new UnpackedColour(buf.readInt());
			}
			return new ColourPalette(colours);
		}
	);

	public ColourPalette(List<UnpackedColour> colours)
	{
		this(colours.toArray(new UnpackedColour[]{}));
	}

	public UnpackedColour getRandom(RandomSource random)
	{
		return colours[random.nextInt(colours.length)];
	}
}
