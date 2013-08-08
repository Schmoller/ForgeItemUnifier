package schmoller.unifier;

public interface IProcessor
{
	public String getName();
	
	public int applyMappings(Mappings mappings);
}
