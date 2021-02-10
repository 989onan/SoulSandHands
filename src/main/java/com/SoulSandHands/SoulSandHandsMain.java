package com.SoulSandHands;


import net.minecraft.block.BlockState;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("soulsandhands")
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class SoulSandHandsMain
{
	public final static BasicParticleType SOULHANDS = (BasicParticleType) new BasicParticleType(true).setRegistryName(new ResourceLocation(SoulSandHandsMain.MODID,"soulhands"));
	public static final String MODID = "soulsandhands";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public SoulSandHandsMain() {
        // Register the setup method for modloading
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	
    	
    }
    
    protected BlockPos getOnPosition(Entity entity) {
        int i = MathHelper.floor(entity.getPositionVec().x);
        int j = MathHelper.floor(entity.getPositionVec().y - (double)0.2F);
        int k = MathHelper.floor(entity.getPositionVec().z);
        BlockPos blockpos = new BlockPos(i, j, k);
        if (entity.world.isAirBlock(blockpos)) {
           BlockPos blockpos1 = blockpos.down();
           BlockState blockstate = entity.world.getBlockState(blockpos1);
           if (blockstate.collisionExtendsVertically(entity.getEntityWorld(), blockpos1, entity)) {
              return blockpos1;
           }
        }

        return blockpos;
     }
    
    @SubscribeEvent
    public void walkOnSoulSand(LivingUpdateEvent event) {
    	Entity entity = event.getEntity();
    	BlockPos EntityBlockPosition = getOnPosition(entity);
    	if(entity.getEntityWorld().getBlockState(EntityBlockPosition).getBlock() instanceof SoulSandBlock) {
    		if(entity.getEntityWorld().getRandom().nextInt(10) > 6 && entity.shouldSpawnRunningEffects()) {
    			event.getEntity().getEntityWorld().addParticle(SOULHANDS,entity.getPosX()+((entity.getEntityWorld().getRandom().nextFloat()-.5)/2),entity.getPosY()+.5F,entity.getPosZ()+((entity.getEntityWorld().getRandom().nextFloat()-.5)/4),0,0,0);
    		}
    	}
    		
    	
    }
    
    private void clientRegistries(final FMLClientSetupEvent event) {
		
	}
    
    
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onParticlesRegistry(final RegistryEvent.Register<ParticleType<?>> event) {
            // register a new block here
        	event.getRegistry().registerAll(
        			SOULHANDS
        			);
        	LOGGER.info("Registering Particles from: "+MODID);
        	
        }
        @SubscribeEvent
        public static void onParticleFactoryRegister(final ParticleFactoryRegisterEvent event) {
        	Minecraft.getInstance().particles.registerFactory(SOULHANDS, SoulHandsParticle.Factory::new);
        }
        
        
    }
}

