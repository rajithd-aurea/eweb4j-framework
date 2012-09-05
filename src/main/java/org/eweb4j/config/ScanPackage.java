package org.eweb4j.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;

public abstract class ScanPackage {
	
	public final static Log log = LogFactory.getConfigLogger(ScanPackage.class);
	
	private Collection<String> packages = new HashSet<String>();
	private Collection<String> jars = new HashSet<String>();
	private String currentClassPath = null;
	
	public String readAnnotation(Collection<String> scans) {
		String error = null;
		
		try {
			if (scans == null)
				return error;
			
			Collection<String> classpaths = new HashSet<String>();
			
			for (String scan : scans){
				if (scan.startsWith("AP:")){
					classpaths.add(scan.replace("AP:", ""));
				}else if (scan.startsWith("JAR:")){
					
				}else{
					packages.add(scan);
				}
			}
			
			Collection<String> paths = FileUtil.getClassPath();
			for (String path : paths){
				if (path.endsWith(".jar")){
					final String name = "JAR:" + new File(path).getName().replace(".jar", "");
					if (scans.contains(name)){
						jars.add(path);
					}
				}else{
					classpaths.add(path);
				}
			}
			
			// 扫描ClassPath中的*.class
			for (String classpath : classpaths){
				scanDir(classpath);
			}
			
			// 扫描jar包
			scanJar();

		} catch (Exception e) {
			error = StringUtil.getExceptionString(e);
			log.error(error);
		}

		return error;
	}

	private void scanDir(String classDir) throws Exception {
		File dir = null;
		dir = new File(classDir);
		log.debug("scan dir -> " + dir);
		// 递归文件目录
		if (dir.isDirectory()){
			this.currentClassPath = dir.getAbsolutePath();
			scanFile(dir);
		}
	}

	/**
	 * 扫描class文件
	 * 
	 * @param dir
	 * @param actionPackage
	 * @throws Exception
	 */
	private void scanFile(File dir) throws Exception {
		if (!dir.isDirectory())
			return;

		File[] files = dir.listFiles();
		if (files == null || files.length == 0)
			return;
		
		for (File f : files) {
			if (f.isDirectory())
				scanFile(f);
			else if (f.isFile()) {
				if (!f.getName().endsWith(".class")){
					if (f.getName().endsWith(".jar")){
						final String jarName = f.getAbsolutePath();
						jars.add(jarName);
						log.debug(" jar add -> " + jarName);
					}
					continue;
				}

				String clsName = f.getAbsolutePath().replace(this.currentClassPath, "").replace(File.separator, ".").replace(".class", "").substring(1);
				boolean isPkg = false;
				for (String pkg : packages){
					if (".".equals(pkg) || clsName.startsWith(pkg)){
						isPkg = true;
						break;
					}
				}
				
				if (!isPkg)
					continue;
				
				if (!handleClass(clsName))
					continue;
			}
		}
	}

	/**
	 * scan package by jars
	 * 
	 * @param jarsParentDirPath
	 * @param packageName
	 * @throws Exception
	 */
	private void scanJar() {
		if (jars == null)
			return;

		for (String p : jars) {
			File f = new File(p);
			ZipInputStream zin = null;
			ZipEntry entry = null;
			try {
				zin = new ZipInputStream(new FileInputStream(f));

				log.debug("scanning jar -> " + f.getAbsolutePath());

				while ((entry = zin.getNextEntry()) != null) {
					
					String entryName = entry.getName().replace('/', '.');
					boolean isPkg = false;
					for (String pkg : packages){
						if (".".equals(pkg) || entryName.startsWith(pkg)){
							isPkg = true;
							break;
						}
					}
					
					if (isPkg){
						if (!entryName.endsWith(".class"))
							continue;
						
						final String className = entryName.replace(".class", "");
						try {
							if (!handleClass(className))
								continue;
						} catch (Error e) {
							continue;
						} catch (Exception e) {
							continue;
						}
					}

					zin.closeEntry();
				}
				zin.close();
			} catch (Error e) {
				continue;
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	protected abstract boolean handleClass(String className);
}
