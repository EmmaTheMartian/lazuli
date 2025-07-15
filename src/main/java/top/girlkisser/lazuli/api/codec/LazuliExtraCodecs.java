package top.girlkisser.lazuli.api.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
	 * { "x": 0, "y": 0 }
	 * </code>
	 */
	Codec<Vector2f> VECTOR2F = RecordCodecBuilder.create(it -> it.group(
		Codec.FLOAT.fieldOf("x").forGetter(Vector2f::x),
		Codec.FLOAT.fieldOf("y").forGetter(Vector2f::y)
	).apply(it, Vector2f::new));

	/**
	 * Stream codec for {@link #VECTOR2F}.
	 */
	StreamCodec<ByteBuf, Vector2f> VECTOR2F_STREAM_CODEC = ByteBufCodecs.fromCodec(VECTOR2F);

	/**
	 * A codec for an {@link AABB}. Uses {@link AABB#getMinPosition()} and {@link AABB#getMaxPosition()}
	 * for serialization.
	 */
	Codec<AABB> AABB = RecordCodecBuilder.create(it -> it.group(
		Vec3.CODEC.fieldOf("min").forGetter(net.minecraft.world.phys.AABB::getMinPosition),
		Vec3.CODEC.fieldOf("max").forGetter(net.minecraft.world.phys.AABB::getMaxPosition)
	).apply(it, AABB::new));

	/**
	 * Stream codec for {@link #AABB}.
	 */
	StreamCodec<ByteBuf, AABB> AABB_STREAM_CODEC = ByteBufCodecs.fromCodec(AABB);
}
