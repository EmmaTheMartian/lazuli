package top.girlkisser.lazuli.api.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.menu.AbstractLazuliContainer;

/**
 * Provides a basic {@link Screen} that does not implement {@link net.minecraft.client.gui.screens.inventory.ContainerScreen}.
 *
 * @param <T> The container that the screen is for.
 */
public abstract class AbstractLazuliMenuScreen<T extends AbstractLazuliContainer> extends Screen implements MenuAccess<T>
{
	protected T menu;
	protected int leftPos, topPos, imageWidth, imageHeight;

	protected AbstractLazuliMenuScreen(T menu, Component title)
	{
		super(title);
		this.menu = menu;
	}

	/**
	 * @return The texture to use for the screen's background.
	 */
	protected abstract ResourceLocation getUI();

	@Override
	public @NotNull T getMenu()
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
	public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
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
	protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(getUI(), leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	public int getGuiLeft()
	{
		return leftPos;
	}

	public int getGuiTop()
	{
		return topPos;
	}
}

