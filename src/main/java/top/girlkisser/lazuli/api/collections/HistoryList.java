package top.girlkisser.lazuli.api.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A helper class for storing and managing a list with undo/redo capabilities.
 * <p>
 * See Minefactorial's TweakerulerHistory for a usage example.
 *
 * @param <T> The list's entry type.
 */
public class HistoryList<T>
{
	/**
	 * A stack containing the elements to add to {@link data} when {@link #redo()} is invoked.
	 */
	protected Stack<T> redoHistory = new Stack<>();

	/**
	 * The contained elements.
	 */
	protected List<T> data = new ArrayList<>();

	/**
	 * The maximum combined length of each list.
	 */
	protected int maxLength;

	/**
	 * A helper class for storing and managing a list with undo/redo capabilities.
	 * <p>
	 * See Minefactorial's TweakerulerHistory for a usage example.
	 *
	 * @param maxLength The maximum length for the list.
	 */
	public HistoryList(int maxLength)
	{
		this.maxLength = maxLength;
	}

	/**
	 * Clears all data and history from this list.
	 */
	public void clear()
	{
		data.clear();
		redoHistory.clear();
	}

	/**
	 * Get the current amount of elements.
	 *
	 * @return The amount.
	 */
	public int dataSize()
	{
		return data.size();
	}

	/**
	 * Get the current amount of elements stored in history.
	 *
	 * @return The amount.
	 */
	public int historySize()
	{
		return redoHistory.size();
	}

	/**
	 * Pushes an item onto the history. This will remove elements after the index, if
	 * there are any.
	 *
	 * @param it The item to add.
	 */
	public void push(T it)
	{
		data.add(it);

		// Clear any now-invalid redo history.
		if (!redoHistory.isEmpty())
		{
			redoHistory.clear();
		}

		// If we exceed the history, remove the oldest item on the list.
		if (data.size() > maxLength)
		{
			data.removeFirst();
		}
	}

	/**
	 * Get whether an {@link #undo()} will do anything when invoked.
	 *
	 * @return `true` if there is data to undo.
	 */
	public boolean canUndo()
	{
		return !data.isEmpty();
	}

	/**
	 * Undo the lastest {@link #push(Object)}.
	 */
	public void undo()
	{
		if (canUndo())
		{
			T undone = data.removeLast();
			onUndo(undone);
			redoHistory.push(undone);
		}
	}

	/**
	 * Get whether a {@link #redo()} will do anything when invoked.
	 *
	 * @return `true` if there is data to redo.
	 */
	public boolean canRedo()
	{
		return !redoHistory.isEmpty();
	}

	/**
	 * Undo the lastest {@link #undo()}.
	 */
	public void redo()
	{
		if (canRedo())
		{
			T redone = redoHistory.pop();
			onRedo(redone);
			data.add(redone);
		}
	}

	/**
	 * Invoked any time {@link #undo()} is called successfully.
	 *
	 * @param undoneItem The undone item.
	 */
	protected void onUndo(T undoneItem)
	{
	}

	/**
	 * Invoked any time {@link #redo()} is called successfully.
	 *
	 * @param redoneItem The redone item.
	 */
	protected void onRedo(T redoneItem)
	{
	}
}
