package top.girlkisser.lazuli.api.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.menu.AbstractLazuliContainer;

/**
 * Provides a basic implementation for {@link net.minecraft.client.gui.screens.Screen}s
 * with {@link AbstractLazuliContainer} menus.
 *
 * @param <T> The container that the screen is for.
 */
public abstract class AbstractLazuliContainerScreen<T extends AbstractLazuliContainer> extends AbstractContainerScreen<T>
{
	/**
	 * A basic implementation for {@link net.minecraft.client.gui.screens.Screen}s
	 * with {@link AbstractLazuliContainer} menus.
	 *
	 * @param menu The menu.
	 * @param playerInventory The player's inventory.
	 * @param title The screen's title.
	 */
	protected AbstractLazuliContainerScreen(T menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
	}

	/**
	 * Gets the texture to use for the screen's background.
	 *
	 * @return The texture.
	 */
	protected abstract ResourceLocation getUI();

	@Override
	public void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(getUI(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(graphics, mouseX, mouseY, partialTick);
		this.renderTooltip(graphics, mouseX, mouseY);
	}
}

