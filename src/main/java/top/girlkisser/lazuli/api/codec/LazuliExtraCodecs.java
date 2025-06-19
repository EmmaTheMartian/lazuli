package top.girlkisser.lazuli.api.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2f;

/**
 * Extra codecs for random things.
 */
@ApiStatus.NonExtendable
public interface LazuliExtraCodecs
{
	/**
	 * A codec for a JOML {@link Vector2f}. Syntax:
	 * <br/>
	 * <code>
	 *     { "x": 0, "y": 0 }
	 * </code>
	 */
	Codec<Vector2f> VECTOR2F = RecordCodecBuilder.create(it -> it.group(
		Codec.FLOAT.fieldOf("x").forGetter(Vector2f::x),
		Codec.FLOAT.fieldOf("y").forGetter(Vector2f::y)
	).apply(it, Vector2f::new));

	/** Stream codec for {@link #VECTOR2F}. */
	StreamCodec<ByteBuf, Vector2f> VECTOR2F_STREAM_CODEC = ByteBufCodecs.fromCodec(VECTOR2F);
}
