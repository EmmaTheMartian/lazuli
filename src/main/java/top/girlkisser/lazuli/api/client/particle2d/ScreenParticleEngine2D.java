package top.girlkisser.lazuli.api.client.particle2d;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * A particle engine for {@link net.minecraft.client.gui.screens.Screen}s.
 */
public class ScreenParticleEngine2D implements IParticleEngine2D
{
	/**
	 * A default instance to use.
	 */
	public static final ScreenParticleEngine2D INSTANCE = new ScreenParticleEngine2D();

	/**
	 * The maximum number of particles allowed at any given time.
	 */
	public static final int MAX_PARTICLES = Short.MAX_VALUE;

	/**
	 * The current list of particles.
	 */
	protected List<InstancedParticle2D> particles = new ArrayList<>();

	/**
	 * A queue of particles to add on the next frame.
	 */
	protected Queue<IParticle2D> particleQueue = new ArrayDeque<>();

	/**
	 * A particle engine for {@link net.minecraft.client.gui.screens.Screen}s.
	 */
	public ScreenParticleEngine2D()
	{
	}

	@Override
	public void addParticle(IParticle2D particle)
	{
		if (particles.size() + particleQueue.size() >= MAX_PARTICLES)
			return;
		particleQueue.add(particle);
	}

	@Override
	public void render(GuiGraphics graphics, float partialTick)
	{
		if (!particleQueue.isEmpty())
		{
			particleQueue.forEach(it -> particles.add(new InstancedParticle2D(it, it.lifetime())));
			particleQueue.clear();
		}

		for (var particle : particles)
			particle.particle.render(particle, graphics, partialTick);

		particles.removeIf(it -> it.lifetimeLeft <= 0);
	}

	@Override
	public void clear()
	{
		particles.clear();
		particleQueue.clear();
	}
}
