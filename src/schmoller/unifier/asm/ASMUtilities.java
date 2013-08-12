package schmoller.unifier.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMUtilities
{
	public static ClassNode getClassNode(byte[] classBytes)
	{
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		
		return node;
	}
	public static MethodNode getMethodNode(ClassNode classNode, String methodName)
	{
		for(MethodNode method : classNode.methods)
		{
			if(method.name.equals(methodName))
				return method;
		}
		
		throw new RuntimeException("Cannot find " + methodName + " in " + classNode.name);
	}
	
	public static byte[] getBytes(ClassNode classNode)
	{
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		
		return writer.toByteArray();
	}
}
