package top.girlkisser.lazuli.api.misc;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * A clone of {@link net.neoforged.neoforge.common.util.Lazy} except ArgLazy takes in an
 * argument too.
 *
 * @param <ItT> The return type.
 * @param <ArgT> The argument type.
 */
public class ArgLazy<ItT, ArgT>
{
	private @Nullable ItT it;
	private final Function<ArgT, ItT> function;

	/**
	 * A clone of {@link net.neoforged.neoforge.common.util.Lazy} with arguments.
	 *
	 * @param function The function to provide the lazy's value.
	 */
	public ArgLazy(Function<ArgT, ItT> function)
	{
		this.function = function;
	}

	/**
	 * Get or generate the lazy's value.
	 *
	 * @param arg The argument to give the function.
	 * @return The lazy's value.
	 */
	public ItT get(ArgT arg)
	{
		if (it == null)
			it = function.apply(arg);
		return it;
	}

	/**
	 * Gets whether the lazy's value has been generated or not.
	 *
	 * @return `true` if the value has been generated.
	 */
	public boolean isPresent()
	{
		return it != null;
	}
}
