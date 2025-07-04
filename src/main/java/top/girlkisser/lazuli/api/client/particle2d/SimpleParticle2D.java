package top.girlkisser.lazuli.api.client.particle2d;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * A simple implementation of {@link IParticle2D}.
 *
 * @param texture  The particle's texture.
 * @param position The particle's position.
 * @param velocity The particle's velocity.
 * @param gravity  A gravity force to apply to the velocity every tick.
 * @param size     The particle's size.
 * @param lifetime The particle's lifetime.
 */
public record SimpleParticle2D(
	ResourceLocation texture,
	Vector2f position,
	Vector2f velocity,
	Vector2f gravity,
	Vector2i size,
	int lifetime
) implements IParticle2D
{
	@Override
	public void render(InstancedParticle2D instance, GuiGraphics graphics, float partialTick)
	{
		RenderSystem.enableBlend();
		float lifetimePercent = (float) instance.lifetimeLeft / lifetime;
		graphics.setColor(1, 1, 1, lifetimePercent); // Fade the particle out
		graphics.blitSprite(
			texture(),
			(int) position().x,
			(int) position().y,
			(int) (size().x * lifetimePercent),
			(int) (size().y * lifetimePercent)
		);
		graphics.setColor(1, 1, 1, 1);
		RenderSystem.disableBlend();
		position.x += velocity.x;
		position.y += velocity.y;
		velocity.x += gravity.x;
		velocity.y += gravity.y;
		instance.lifetimeLeft--;
	}
}
