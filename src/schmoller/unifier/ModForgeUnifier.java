package schmoller.unifier;

import java.io.File;
import java.io.IOException;
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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
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
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

@Mod(name="Forge Unifier", modid="ForgeUnifier", version="##FUVersion##", dependencies="after:*")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class ModForgeUnifier implements IModPacketHandler, IConnectionHandler
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
		
		NetworkRegistry.instance().registerConnectionHandler(this);
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
		
		mappings = new ServerMappings(path);
		try
		{
			mappings.load();
			manager.execute(mappings);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		
		event.registerServerCommand(new CommandUnifier());
	}
	
	@ServerStopping
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		mappings.restoreOriginals();
	}
	
	@Override
	public boolean onPacketArrive( ModPacket packet, Player sender )
	{
		if(packet instanceof ModPacketOpenGui && FMLCommonHandler.instance().getSide() == Side.CLIENT)
			openGui(null, ((ModPacketOpenGui)packet).allowEdit);
		else if(packet instanceof ModPacketChangeMapping)
		{
			mappings.applyChanges((ModPacketChangeMapping)packet, sender);
		}
		else
			return false;
		
		return true;
	}
	
	public static void openGui(EntityPlayer player, boolean edit)
	{
		if(Utilities.isClient() && Utilities.isServer())
		{
			if(player.equals(Minecraft.getMinecraft().thePlayer))
				FMLClientHandler.instance().showGuiScreen(new GuiUnifierSettings(edit));
			else
				packetHandler.sendPacketToClient(new ModPacketOpenGui(true), player);
		}
		else
		{
			if(player == null)
				FMLClientHandler.instance().showGuiScreen(new GuiUnifierSettings(edit));
			else if(Utilities.isServer())
				packetHandler.sendPacketToClient(new ModPacketOpenGui(edit), player);
		}
	}

	@Override
	public void playerLoggedIn( Player player, NetHandler netHandler, INetworkManager manager )
	{
		ModPacketChangeMapping packet = mappings.asPacket();
		packetHandler.sendPacketToClient(packet, (EntityPlayer)player);
	}

	@Override
	public String connectionReceived( NetLoginHandler netHandler, INetworkManager manager )	{ return null; }

	@Override
	public void connectionOpened( NetHandler netClientHandler, String server, int port, INetworkManager manager ) 
	{
		// Prepare for connection to server
		mappings = new Mappings();
	}

	@Override
	public void connectionOpened( NetHandler netClientHandler, MinecraftServer server, INetworkManager manager ) {}

	@Override
	public void connectionClosed( INetworkManager manager ) 
	{
		// Disconnect from server, restore defaults
		if(!Utilities.isServer())
			mappings.restoreOriginals();
	}

	@Override
	public void clientLoggedIn( NetHandler clientHandler, INetworkManager manager, Packet1Login login ) {}
	
	public static boolean canPlayerEdit(EntityPlayer player)
	{
		return (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().areCommandsAllowed(player.username));
	}
}
