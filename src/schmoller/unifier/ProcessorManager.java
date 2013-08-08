package schmoller.unifier;

import java.util.HashSet;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.Loader;

public class ProcessorManager
{
	private HashSet<IProcessor> mProcessors = new HashSet<IProcessor>();
	
	public void registerProcessor(IProcessor processor)
	{
		mProcessors.add(processor);
	}
	
	public void registerModProcessor(String modId, Class<? extends IProcessor> clazz)
	{
		if(Loader.isModLoaded(modId))
		{
			try
			{
				IProcessor processor = clazz.newInstance();
				mProcessors.add(processor);
			}
			catch(Throwable e)
			{
				Throwables.propagateIfPossible(e);
				throw new RuntimeException(e);
			}
		}
	}
	
	public void execute(Mappings mappings)
	{
		for(IProcessor processor : mProcessors)
		{
			try
			{
				int count = processor.applyMappings(mappings);
				if(count != 0)
					ModForgeUnifier.log.info(String.format("%d items remapped for %s", count, processor.getName()));
			}
			catch(Exception e)
			{
				ModForgeUnifier.log.severe("Error occured while attempting to remap " + processor.getName());
				
				e.printStackTrace();
			}
		}
	}
	
	
}
