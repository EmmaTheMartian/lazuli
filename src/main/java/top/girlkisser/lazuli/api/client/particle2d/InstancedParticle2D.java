package top.girlkisser.lazuli.api.client.particle2d;

/**
 * Stores information about an active {@link IParticle2D}.
 */
public class InstancedParticle2D
{
	/**
	 * The particle.
	 */
	public IParticle2D particle;

	/**
	 * The ticks left until this particle is removed.
	 */
	public int lifetimeLeft;

	/**
	 * Stores information about an active {@link IParticle2D}.
	 *
	 * @param particle     The particle.
	 * @param lifetimeLeft The particle's remaining lifetime.
	 */
	public InstancedParticle2D(
		IParticle2D particle,
		int lifetimeLeft
	)
	{
		this.particle = particle;
		this.lifetimeLeft = lifetimeLeft;
	}
}
