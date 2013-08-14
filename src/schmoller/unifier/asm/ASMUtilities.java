package schmoller.unifier.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import schmoller.unifier.asm.NameHelper.TypeName;

public class ASMUtilities
{
	public static ClassNode getClassNode(byte[] classBytes)
	{
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		
		return node;
	}
	public static MethodNode getMethodNode(ClassNode classNode, String methodName, String desc)
	{
		for(MethodNode method : classNode.methods)
		{
			if(method.name.equals(methodName) && method.desc.equals(desc))
				return method;
		}
		
		throw new RuntimeException("Cannot find " + methodName + " in " + classNode.name);
	}
	
	public static String buildDescription(Object returnType, Object... args)
	{
		String desc = "";
		if(args.length > 0)
		{
			desc = "(";
			
			for(Object obj : args)
			{
				if(obj instanceof String)
					desc += (String)obj + ";";
				else if(obj instanceof TypeName)
					desc += ((TypeName)obj).getDesc() + ";";
			}
			
			desc += ")";
		}
		
		if(returnType instanceof String)
			desc += returnType;
		else if(returnType instanceof TypeName)
			desc += ((TypeName)returnType).getDesc();
		
		return desc;
	}
	
	public static byte[] getBytes(ClassNode classNode)
	{
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		
		return writer.toByteArray();
	}
}
