package top.girlkisser.lazuli.api.client.particle2d;

/**
 * Stores information about an active {@link IParticle2D}.
 */
public class InstancedParticle2D
{
	public IParticle2D particle;
	public int lifetimeLeft;

	public InstancedParticle2D(
		IParticle2D particle,
		int lifetimeLeft
	)
	{
		this.particle = particle;
		this.lifetimeLeft = lifetimeLeft;
	}
}
