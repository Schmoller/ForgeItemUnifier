package schmoller.unifier;

import java.util.Set;

public interface IProcess<T>
{
	public Set<T> getInputs();
	public Set<T> getOutputs();
}
