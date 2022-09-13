package experiments;

import java.lang.StringBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Reflect {
	public static String describe(Object x) {
		if (x == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();
		Class c = x.getClass();
		
		String interfaces = describeInterfaces(c.getGenericInterfaces());
		
		sb.append(c.toGenericString() + interfaces + " { \n");
		describeFields(sb, c.getDeclaredFields());
		describeMethods(sb, c.getDeclaredMethods());
		sb.append("}\n");
		
		return sb.toString();
	}
	
	private static void describeFields(StringBuilder sb, Field[] xs) {
		for (Field f: xs) {
			sb.append("  " + f.toGenericString() + "\n");
		}
	}
	
	private static void describeMethods(StringBuilder sb, Method[] xs) {
		for (Method m: xs) {
			sb.append("  " + m.toGenericString() + "\n");
		}
	}
	
	private static String describeInterfaces(Type[] xs) {
		if(xs.length == 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder(" implements ");
		boolean fst = true;
		for (Type c: xs) {
			if(!fst) {
				sb.append(", ");
			}
			sb.append(c.getTypeName());
		}
		
		return sb.toString();
	}
}
