package schmoller.unifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import cpw.mods.fml.common.network.Player;
import schmoller.unifier.packets.ModPacketChangeMapping;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

public class ServerMappings extends Mappings
{
	private File mFile;
	public ServerMappings( File file )
	{
		mFile = file;
	}
	
	@Override
	public void applyChanges( ModPacketChangeMapping changes, Player sender )
	{
		super.applyChanges(changes, sender);
		
		if(Utilities.isServer())
		{
			ModForgeUnifier.packetHandler.sendPacketToAllClients(changes);
		}
		
		try
		{
			save();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void save() throws IOException
	{
		FileOutputStream stream = new FileOutputStream(mFile);
		NBTTagCompound root = new NBTTagCompound();
		
		NBTTagList map = new NBTTagList();
		for(Entry<String, ItemStack> entry : mMappings.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("name", entry.getKey());
			tag.setInteger("id", entry.getValue().itemID);
			tag.setInteger("data", entry.getValue().getItemDamage());
			map.appendTag(tag);
		}
		
		root.setTag("mappings", map);
		
		CompressedStreamTools.writeCompressed(root, stream);
		stream.close();
	}
	
	@Override
	public void load() throws IOException
	{
		mMappings.clear();
		
		if(!mFile.exists())
			return;
		
		FileInputStream stream = new FileInputStream(mFile);
		NBTTagCompound root = CompressedStreamTools.readCompressed(stream);
		stream.close();
		
		NBTTagList map = root.getTagList("mappings");
		
		for(int i = 0; i < map.tagCount(); ++i)
		{
			NBTTagCompound tag = (NBTTagCompound)map.tagAt(i);
			
			String name = tag.getString("name");
			
			int id = tag.getInteger("id");
			int data = tag.getInteger("data");
			
			ItemStack item = new ItemStack(id, 1, data);
			
			if(item.getItem() == null)
			{
				ModForgeUnifier.log.severe(String.format("Unknown item [%d:%d] used for %s. Ignoring", id, data, name));
				continue;
			}
			
			int oreId = OreDictionary.getOreID(item);
			
			if(oreId == -1)
			{
				ModForgeUnifier.log.severe(String.format("Item [%d:%d] used for %s is not in the ore dictionary. Ignoring", id, data, name));
				continue;
			}
			
			String oreName = OreDictionary.getOreName(oreId);
			if(!name.equalsIgnoreCase(oreName))
			{
				ModForgeUnifier.log.severe(String.format("Item [%d:%d] used for %s has the ore type %s. Ignoring", id, data, name, oreName));
				continue;
			}
			
			mMappings.put(name, item);
		}
	}
}
