package hotrodman106.hotcrafthosting.jlimeconsole;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Coolway99 on 2015-01-25
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 */
public class ModuleManager{
	private static final HashMap<String, Method> moduleList = new HashMap<>();
	private static final HashMap<String, File> fileList = new HashMap<>();
	private static File moduleDirectory;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Module{}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface ModInit{}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface CommandParser{}

	public static void init(File directory) throws IOException{
		moduleDirectory = directory;
		for(File file : moduleDirectory.listFiles()){
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			Class modClass = null;
			URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()},
					ModuleManager.class.getClassLoader());
			while(entries.hasMoreElements()){
				JarEntry entry = entries.nextElement();
				if(entry.getName().endsWith(".class")){
					try{
						Class tempClass = Class.forName(entry.getName().substring(0,
								entry.getName().lastIndexOf(".class")), true, loader);
						if(tempClass.getAnnotation(Module.class) != null){
							modClass = tempClass;
							break;
						}
					} catch(ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			}
			if(modClass != null){
				String name = null;
				Method commandParser = null;
				for(Method method : modClass.getMethods()){
					try{
						if(method.getAnnotation(ModInit.class) != null){
							name = (String) method.invoke(null);
						}
						if(method.getAnnotation(CommandParser.class) != null){
							commandParser = method;
						}
						if(name != null && commandParser != null){
							moduleList.put(name, commandParser);
							fileList.put(name, file);
						}
					} catch(IllegalAccessException | InvocationTargetException e){
						break;
					}
				}
			}
		}
	}
	public static void add(File file) throws IOException{
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			Class modClass = null;
			URLClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
			while(entries.hasMoreElements()){
				JarEntry entry = entries.nextElement();
				if(entry.getName().endsWith(".class")){
					try{
						Class tempClass = Class.forName(entry.getName().substring(0,
								entry.getName().lastIndexOf(".class")), true, loader);
						if(tempClass.getAnnotation(Module.class) != null){
							modClass = tempClass;
							break;
						}
					} catch(ClassNotFoundException e){
					}
				}
			}
		if(modClass != null){
			String name = null;
			Method commandParser = null;
			for(Method method : modClass.getMethods()){
				try{
					if(method.getAnnotation(ModInit.class) != null){
						name = (String) method.invoke(null);
					}
					if(method.getAnnotation(CommandParser.class) != null){
						commandParser = method;
					}
					if(name != null && commandParser != null){
						moduleList.put(name, commandParser);
					}
				} catch(IllegalAccessException | InvocationTargetException e){
					break;
				}
			}
		}
	}
	public static int run(ArrayList<String> modules, String cmd, String[] args, int startDepth, ArrayList<String> consoleOutput){
		try{
			for(String key : modules){
				Method method = moduleList.get(key);
				if(method == null){
					consoleOutput.add("OI! One of your modules doesn't exist\n");
					return -2;
				}
				int y = (int) method.invoke(null, cmd, args, startDepth, consoleOutput);
				if(y != -3){
					return y;
				}
			}
		} catch(Exception e){
			return -2;
		}
		consoleOutput.add("OI! Command not valid!\n");
		return -2;
	}
	public static void remove(String key){
		moduleList.remove(key);
		fileList.remove(key).delete();
	}
	public static String[] getList(){
		return moduleList.keySet().toArray(new String[0]);
	}
	public static File[] getFiles(){
		return fileList.values().toArray(new File[0]);
	}
}
