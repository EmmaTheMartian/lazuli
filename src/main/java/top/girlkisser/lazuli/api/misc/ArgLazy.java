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

	public ArgLazy(Function<ArgT, ItT> function)
	{
		this.function = function;
	}

	public ItT get(ArgT arg)
	{
		if (it == null)
			it = function.apply(arg);
		return it;
	}

	public boolean isPresent()
	{
		return it != null;
	}
}
