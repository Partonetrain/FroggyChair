package info.partonetrain.froggychair.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import info.partonetrain.froggychair.entity.SeatEntity;
import info.partonetrain.froggychair.util.VoxelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


public class FroggyChairBlock extends Block {

    //FurnitureBlock
    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return this.hasTileEntity(state);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
                world.updateComparatorOutputLevel(pos, this);
            }
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int type) {
        super.eventReceived(state, world, pos, id, type);
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null && tileEntity.receiveClientEvent(id, type);
    }

    //ChairBlock

    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(DIRECTION, context.getPlacementHorizontalFacing());
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(DIRECTION)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] FRONT_LEFT_LEG = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(2, 0, 12, 4, 8, 14), Direction.SOUTH));
        final VoxelShape[] BACKREST = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(1.5, 10, 1.5, 14.5, 22, 3.5), Direction.SOUTH));
        final VoxelShape[] BASE = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(1.5, 8, 1.5, 14.5, 10, 14.5), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_LEG = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(12, 0, 12, 14, 8, 14), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_LEG = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(12, 0, 2, 14, 8, 4), Direction.SOUTH));
        final VoxelShape[] BACK_LEFT_LEG = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(2, 0, 2, 4, 8, 4), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(FRONT_LEFT_LEG[direction.getHorizontalIndex()]);
            shapes.add(BACKREST[direction.getHorizontalIndex()]);
            shapes.add(BASE[direction.getHorizontalIndex()]);
            shapes.add(FRONT_RIGHT_LEG[direction.getHorizontalIndex()]);
            shapes.add(BACK_RIGHT_LEG[direction.getHorizontalIndex()]);
            shapes.add(BACK_LEFT_LEG[direction.getHorizontalIndex()]);
            builder.put(state, VoxelHelper.combineAll(shapes));
        }
        return builder.build();
    }

    //actual block starts here

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public FroggyChairBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state);
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return SHAPES.get(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
        return SeatEntity.create(world, pos, 0.4, playerEntity);
    }

    /*
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
      return ActionResultType.PASS;
   }
     */
}
