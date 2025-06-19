package top.girlkisser.lazuli.api.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import top.girlkisser.lazuli.api.menu.AbstractLazuliContainer;

/**
 * Provides a basic {@link Screen} that does not implement {@link net.minecraft.client.gui.screens.inventory.ContainerScreen}.
 *
 * @param <T> The container that the screen is for.
 */
public abstract class AbstractLazuliMenuScreen<T extends AbstractLazuliContainer> extends Screen implements MenuAccess<T>
{
	/**
	 * The menu.
	 */
	protected T menu;

	/**
	 * The screen's X position on the window.
	 */
	protected int leftPos;

	/**
	 * The screen's Y position on the window.
	 */
	protected int topPos;

	/**
	 * The screen's width.
	 */
	protected int imageWidth;

	/**
	 * The screen's height.
	 */
	protected int imageHeight;

	/**
	 * Provides a basic {@link Screen} that does not implement {@link net.minecraft.client.gui.screens.inventory.ContainerScreen}.
	 *
	 * @param menu  The menu.
	 * @param title The screen's title.
	 */
	protected AbstractLazuliMenuScreen(T menu, Component title)
	{
		super(title);
		this.menu = menu;
	}

	/**
	 * Gets the texture to use for the screen's background.
	 *
	 * @return The texture.
	 */
	protected abstract ResourceLocation getUI();

	@Override
	public T getMenu()
	{
		return menu;
	}

	@Override
	protected void init()
	{
		super.init();
		leftPos = this.width / 2 - this.imageWidth / 2;
		topPos = this.height / 2 - this.imageHeight / 2;
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		renderBg(guiGraphics, partialTick, mouseX, mouseY);
	}

	/**
	 * Renders the background.
	 *
	 * @param graphics    {@link GuiGraphics} to render using.
	 * @param partialTick The game's partial tick.
	 * @param mouseX      The mouse's X position.
	 * @param mouseY      The mouse's Y position.
	 */
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(getUI(), leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	/**
	 * Get the X position of the screen.
	 *
	 * @return The position.
	 */
	public int getGuiLeft()
	{
		return leftPos;
	}

	/**
	 * Get the Y position of the screen.
	 *
	 * @return The position.
	 */
	public int getGuiTop()
	{
		return topPos;
	}
}

