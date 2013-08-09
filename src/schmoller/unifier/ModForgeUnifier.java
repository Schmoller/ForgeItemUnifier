package schmoller.unifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Logger;

import schmoller.network.IModPacketHandler;
import schmoller.network.ModPacket;
import schmoller.network.PacketManager;
import schmoller.unifier.gui.GuiUnifierSettings;
import schmoller.unifier.mods.factorization.*;
import schmoller.unifier.mods.forestry.*;
import schmoller.unifier.mods.gregtech.*;
import schmoller.unifier.mods.ic2.*;
import schmoller.unifier.mods.railcraft.*;
import schmoller.unifier.mods.thaumcraft.*;
import schmoller.unifier.mods.thermalExpansion.*;
import schmoller.unifier.mods.tinkersConstruct.*;
import schmoller.unifier.packets.ModPacketChangeMapping;
import schmoller.unifier.packets.ModPacketOpenGui;
import schmoller.unifier.vanilla.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

@Mod(name="Forge Unifier", modid="ForgeUnifier", version="##FUVersion##", dependencies="after:*")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class ModForgeUnifier implements IModPacketHandler
{
	public static Logger log;
	public static Mappings mappings = null;
	public static ProcessorManager manager;
	
	private Configuration mConfig;
	
	@SidedProxy(clientSide="schmoller.network.ClientPacketManager", serverSide="schmoller.network.PacketManager")
	public static PacketManager packetHandler;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		FMLLog.makeLog("OreUnifier");
		log = FMLLog.getLogger().getLogger("OreUnifier");
		log.setUseParentHandlers(true);
		
		File configFile = event.getSuggestedConfigurationFile();
		mConfig = new Configuration(configFile);
		
		manager = new ProcessorManager();
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		packetHandler.initialize("FrgUni");
		packetHandler.registerHandler(this);
		
		packetHandler.registerPacket(ModPacketOpenGui.class);
		packetHandler.registerPacket(ModPacketChangeMapping.class);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		log.info("Loading mappings:");
		
		manager.registerProcessor(new CraftingProcessor());
		manager.registerProcessor(new SmeltingProcessor());
		
		// IC2 processors
		manager.registerModProcessor("IC2", CompressorProcessor.class);
		manager.registerModProcessor("IC2", ExtractorProcessor.class);
		manager.registerModProcessor("IC2", MaceratorProcessor.class);
		manager.registerModProcessor("IC2", ScrapboxProcessor.class);

		// Railcraft processors
		manager.registerModProcessor("Railcraft", BlastFurnaceProcessor.class);
		manager.registerModProcessor("Railcraft", CokeOvenProcessor.class);
		manager.registerModProcessor("Railcraft", RockCrusherProcessor.class);
		manager.registerModProcessor("Railcraft", RollingMachineProcessor.class);
		
		// Thaumcraft processors
		manager.registerModProcessor("Thaumcraft", ArcaneCraftingProcessor.class);
		manager.registerModProcessor("Thaumcraft", CrucibleProcessor.class);
		manager.registerModProcessor("Thaumcraft", SmelterBonusProcessor.class);
		
		// Forestry processors
		manager.registerModProcessor("Forestry", CarpenterProcessor.class);
		manager.registerModProcessor("Forestry", CentrifugeProcessor.class);
		manager.registerModProcessor("Forestry", FabricatorProcessor.class);
		manager.registerModProcessor("Forestry", SqueezerProcessor.class);
		
		// Factorization processors
		manager.registerModProcessor("factorization", CrystallizerProcessor.class);
		manager.registerModProcessor("factorization", GrinderProcessor.class);
		manager.registerModProcessor("factorization", SlagFurnaceProcessor.class);
		
		// Tinkers construct
		manager.registerModProcessor("TConstruct", TableCastingProcessor.class);
		manager.registerModProcessor("TConstruct", BasinCastingProcessor.class);
		
		// Applied energistics
		manager.registerModProcessor("AppliedEnergistics", schmoller.unifier.mods.appeng.GrinderProcessor.class);
		
		// Thermal Expansion
		manager.registerModProcessor("ThermalExpansion", PulverizerProcessor.class);
		manager.registerModProcessor("ThermalExpansion", SawmillProcessor.class);
		manager.registerModProcessor("ThermalExpansion", InductionSmelterProcessor.class);
		
		// Gregtech
		manager.registerModProcessor("GregTech_Addon", AlloySmelterProcessor.class);
		manager.registerModProcessor("GregTech_Addon", AssemblerProcessor.class);
		manager.registerModProcessor("GregTech_Addon", CannerProcessor.class);
		manager.registerModProcessor("GregTech_Addon", ChemicalReactorProcessor.class);
		manager.registerModProcessor("GregTech_Addon", DistillationProcessor.class);
		manager.registerModProcessor("GregTech_Addon", ElectrolyzerProcessor.class);
		manager.registerModProcessor("GregTech_Addon", FusionProcessor.class);
		manager.registerModProcessor("GregTech_Addon", ImplosionCompressorProcessor.class);
		manager.registerModProcessor("GregTech_Addon", IndustrialBlastFurnaceProcessor.class);
		manager.registerModProcessor("GregTech_Addon", IndustrialCentrifugeProcessor.class);
		manager.registerModProcessor("GregTech_Addon", IndustrialGrinderProcessor.class);
		manager.registerModProcessor("GregTech_Addon", IndustrialSawmillProcessor.class);
		manager.registerModProcessor("GregTech_Addon", PlateBenderProcessor.class);
		manager.registerModProcessor("GregTech_Addon", VacuumFreezerProcessor.class);
		manager.registerModProcessor("GregTech_Addon", WiremillProcessor.class);
	}
	
	@ServerStarting
	public void onServerStarting(FMLServerStartingEvent event)
	{
		MinecraftServer server = event.getServer();
		
		File folder = null;
		if(server.isDedicatedServer())
			folder = server.getFile(server.worldServers[0].getSaveHandler().getWorldDirectoryName());
		else
			folder = server.getFile("saves/" + server.getFolderName());

		File path = new File(folder, "ForgeUnifier.dat");
		
		mappings = new Mappings();
		
		if(path.exists())
		{
			try
			{
				FileInputStream stream = new FileInputStream(path);
				NBTTagCompound root = CompressedStreamTools.readCompressed(stream);
				mappings.read(root);
				stream.close();
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
			
			manager.execute(mappings);
		}
		
		event.registerServerCommand(new CommandUnifier());
	}
	
	@ServerStopping
	public void onServerStopping(FMLServerStoppingEvent event)
	{
	}
	
	public static void saveMappings()
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		
		File folder = null;
		if(server.isDedicatedServer())
			folder = server.getFile(server.worldServers[0].getSaveHandler().getWorldDirectoryName());
		else
			folder = server.getFile("saves/" + server.getFolderName());

		File path = new File(folder, "ForgeUnifier.dat");
		
		try
		{
			FileOutputStream stream = new FileOutputStream(path);
			NBTTagCompound root = new NBTTagCompound();
			mappings.write(root);
			
			CompressedStreamTools.writeCompressed(root, stream);
			stream.close();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean onPacketArrive( ModPacket packet, Player sender )
	{
		if(packet instanceof ModPacketOpenGui && FMLCommonHandler.instance().getSide() == Side.CLIENT)
			openGui(null);
		else if(packet instanceof ModPacketChangeMapping)
		{
			if(FMLCommonHandler.instance().getSide() == Side.SERVER)
			{
				mappings.beingModify();
				for(Entry<String, ItemStack> entry : ((ModPacketChangeMapping) packet).newMappings.entrySet())
					mappings.changeMapping(entry.getKey(), entry.getValue());
				mappings.endModify();
			}
		}
		else
			return false;
		
		return true;
	}
	
	public static void openGui(EntityPlayer player)
	{
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
			FMLClientHandler.instance().showGuiScreen(new GuiUnifierSettings());
		else
			packetHandler.sendPacketToClient(new ModPacketOpenGui(true), player);
	}
	
}
