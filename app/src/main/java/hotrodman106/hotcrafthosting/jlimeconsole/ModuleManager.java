package hotrodman106.hotcrafthosting.jlimeconsole;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
	private static HashMap<String, Method> methodList = new HashMap<>();
	private static HashMap<File, ArrayList<String>> fileList = new HashMap<>();

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Module{}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface ModInit{}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Parser{}


	public static void init(File directory){
		for(File file : directory.listFiles()){
			add(file);
		}
	}
	public static void add(File file){
		try{
			fileList.put(file, new ArrayList<String>());
			URLClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()},
					ModuleManager.class.getClassLoader());
			Class<?> clazz = null;
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					String name = entry.getName();
					name = name.substring(0, name.lastIndexOf(".class"))
							.replaceAll("/", ".");
					if (loader == null) {
						clazz = Class.forName(name);
					} else {
						clazz = Class.forName(name, true, loader);
					}
					if(clazz.getAnnotation(ModuleManager.Module.class) != null){
						String modName = null;
						Method modMethod = null;
						for(Method method : clazz.getMethods()){
							if(method.getAnnotation(ModInit.class) != null){
								modName = (String) method.invoke(null);
							}
							if(method.getAnnotation(Parser.class) != null){
								modMethod = method;
							}
							if(modName != null && modMethod != null){
								methodList.put(modName, modMethod);
								fileList.get(file).add(modName);
								break;
							}
						}
					}
				}
			}
			if(fileList.get(file).isEmpty()){
				fileList.remove(file);
			}
			jarFile.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public static int run(ArrayList<String> modules, String cmd, String[] args, int startDepth, ArrayList<String> consoleOutput){
		try{
			for(String key : modules){
				Method method = methodList.get(key);
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
	public static String[] getList(){
		return methodList.keySet().toArray(new String[0]);
	}
	public static File[] getFiles(){
		return fileList.keySet().toArray(new File[0]);
	}
	public static void remove(File file){
		for(String name : fileList.get(file)){
			methodList.remove(name);
		}
		fileList.remove(file);
	}
}
