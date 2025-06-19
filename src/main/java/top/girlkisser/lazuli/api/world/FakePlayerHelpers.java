package top.girlkisser.lazuli.api.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import top.girlkisser.lazuli.Lazuli;
import top.girlkisser.lazuli.api.misc.ArgLazy;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Provides helpers for NeoForge {@link FakePlayer}s.
 */
public final class FakePlayerHelpers
{
	/**
	 * A fake player to use.
	 */
	public static final ArgLazy<FakePlayer, ServerLevel> FAKE_PLAYER = new ArgLazy<>(FakePlayerFactory::getMinecraft);

	private FakePlayerHelpers()
	{
	}

	/**
	 * Breaks a block and gets the drops from it. The drops are not added in the world.
	 *
	 * @param serverLevel The level.
	 * @param blockState The block state to break.
	 * @param pos The block's position.
	 * @return The block's drops.
	 */
	public static List<ItemStack> breakBlockAndGetDrops(ServerLevel serverLevel, BlockState blockState, BlockPos pos)
	{
		FakePlayer fakePlayer = FAKE_PLAYER.get(serverLevel);
		BlockEntity blockentity = blockState.hasBlockEntity() ? serverLevel.getBlockEntity(pos) : null;
		List<ItemStack> drops = blockState.getDrops(new LootParams.Builder(serverLevel)
			.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
			.withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
			.withParameter(LootContextParams.THIS_ENTITY, fakePlayer)
			.withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity));
		serverLevel.destroyBlock(pos, false, fakePlayer);
		return drops;
	}

	/**
	 * Breaks a block and gets the drops from it. The drops are not added in the world.
	 *
	 * @param serverLevel The level.
	 * @param pos The block's position.
	 * @return The block's drops.
	 */
	public static List<ItemStack> breakBlockAndGetDrops(ServerLevel serverLevel, BlockPos pos)
	{
		return breakBlockAndGetDrops(serverLevel, serverLevel.getBlockState(pos), pos);
	}

	/**
	 * Places a block item.
	 *
	 * @param level          The level.
	 * @param pos            Where to place the block.
	 * @param blockItemStack The item stack. This must be a {@link BlockItem} or an
	 *                       error will be thrown.
	 * @param direction      The direction that the block is being placed in. This will
	 *                       not impact the location of the placed block typically, The
	 *                       block will always be placed at {@code pos}.
	 */
	public static void placeBlockItem(ServerLevel level, BlockPos pos, ItemStack blockItemStack, @Nullable Direction direction)
	{
		FakePlayer fakePlayer = FAKE_PLAYER.get(level);
		if (blockItemStack.getItem() instanceof BlockItem blockItem)
		{
			blockItem.place(new BlockPlaceContext(
				fakePlayer,
				InteractionHand.MAIN_HAND,
				blockItemStack,
				new BlockHitResult(Vec3.ZERO, direction == null ? Direction.UP : direction, pos, true)
			));
		}
		else
		{
			Lazuli.LOGGER.error("FakePlayerHelper#placeBlockItem was provided an item stack that was not a BlockItem! ItemStack: {}", blockItemStack);
		}
	}

	/**
	 * Creates a {@link UseOnContext}.
	 *
	 * @param level The level.
	 * @param stack The held item stack.
	 * @param pos   The block position.
	 * @return      The context.
	 */
	public static UseOnContext getUseOnContext(ServerLevel level, ItemStack stack, BlockPos pos)
	{
		return new UseOnContext(level, FAKE_PLAYER.get(level), InteractionHand.MAIN_HAND, stack, new BlockHitResult(Vec3.ZERO, Direction.UP, pos, true));
	}
}
