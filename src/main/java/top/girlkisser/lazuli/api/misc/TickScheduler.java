package top.girlkisser.lazuli.api.misc;

import com.mojang.datafixers.util.Pair;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Allows you to schedule one-shot events that occur in some amount of ticks.
 * <br/>
 * Generally you should not need to make a custom one, using either {@link #SERVER}
 * or {@link #CLIENT} should be sufficient.
 */
public class TickScheduler
{
	/** A tick scheduler for the server. */
	public static final TickScheduler SERVER = new TickScheduler();

	/** A tick scheduler for the client. */
	@OnlyIn(Dist.CLIENT)
	public static final TickScheduler CLIENT = new TickScheduler();

	private final List<Pair<AtomicInteger, Runnable>> scheduled = new ArrayList<>();

	/**
	 * Allows you to schedule one-shot events that occur in some amount of ticks.
	 * <br/>
	 * Generally you should not need to make a custom one, using either {@link #SERVER}
	 * or {@link #CLIENT} should be sufficient.
	 */
	public TickScheduler()
	{
	}

	/**
	 * Ticks the scheduler. The {@link #SERVER} and {@link #CLIENT} schedulers are ticked
	 * automatically by Lazuli.
	 */
	@ApiStatus.Internal
	public void tick()
	{
		if (scheduled.isEmpty())
			return;

		scheduled.forEach(it ->
		{
			if (it.getFirst().decrementAndGet() <= 0)
			{
				it.getSecond().run();
			}
		});

		scheduled.removeIf(it -> it.getFirst().get() <= 0);
	}

	/**
	 * Schedule a task to run in {@code inTicks} ticks.
	 *
	 * @param inTicks The amount of ticks to wait until execution.
	 * @param runnable The task to execute.
	 */
	public void scheduleWork(int inTicks, Runnable runnable)
	{
		scheduled.add(new Pair<>(new AtomicInteger(inTicks), runnable));
	}
}
