package info.partonetrain.froggychair.core;

import info.partonetrain.froggychair.FroggyChair;
import info.partonetrain.froggychair.block.FroggyChairBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = FroggyChair.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, FroggyChair.MODID);
    public static final RegistryObject<Block> FROGGYCHAIR_BLOCK = BLOCKS.register("froggychair", () -> new FroggyChairBlock(Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 6.0F).lightValue(0).sound(SoundType.WOOD)));

}