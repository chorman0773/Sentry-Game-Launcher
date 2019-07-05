package github.chorman0773.sentry.launcher;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.StreamSupport;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import github.chorman0773.sentry.annotation.Game;
import github.chorman0773.sentry.annotation.Provider;
import github.chorman0773.sentry.cci.CCIVendor;
import github.chorman0773.sentry.launcher.mod.ModResolver;
import github.chorman0773.sentry.linterface.LauncherInterface;
import github.chorman0773.sentry.linterface.ModInterface;

public class GameLauncher implements LauncherInterface {
	private LoadingPhase phase;
	private Map<String,String> properties;
	private String[] args;
	private Path gameFolder;
	private Path gameJar;
	
	private boolean modsDisabled;
	
	private GameClassLoader loader;
	
	@SuppressWarnings("unused")
	private static class GameClassLoader extends ClassLoader{
		private Set<Path> jarsToSearch;
		private Map<String,Class<?>> classes = new TreeMap<>();
		private Map<String,ClassWriter> modifiedClasses = new TreeMap<>(); 
		private boolean hasSearched;
		public GameClassLoader(Path gameJar,Path libsFolder) throws IOException {
			jarsToSearch = new HashSet<>();
			try(var strm = Files.newDirectoryStream(libsFolder)){
				for(var p:strm)
					jarsToSearch.add(p);
			}
		}
		
		private static Path toJarPath(Path jarFile,String innerFile) {
			return Path.of("jar:file:"+jarFile.toString()+"!/"+innerFile);
		}
		
		private static ClassReader readerFor(JarFile f,JarEntry entry) throws IOException {
			return new ClassReader(f.getInputStream(entry));
		}
		
		private void applyForAllClassesIn(Path p,BiConsumer<String,JarEntry> apply) {
			try(JarFile jar = new JarFile(p.toFile())) {
				currentJar = jar;
				jar
					.versionedStream()
					.filter(e->e.getName().endsWith(".class"))
					.forEach(e->{
						String name = e.getName();
						name = name.substring(0,name.length()-5);
						apply.accept(name,e);
					});
			}catch(IOException e) {
				
			}
		}
		
		void unload(Path p) {
			jarsToSearch.remove(p);
			applyForAllClassesIn(p,(name,e)->{classes.remove(name);modifiedClasses.remove(name);});
		}
		private JarFile currentJar;
		private List<Function<ClassReader,ClassVisitor>> visitors = new ArrayList<>();
		
		void addSearchVisitor(Function<ClassReader,ClassVisitor> visitor) {
			visitors.add(visitor);
		}
		
		void search() {
			for(Path p:jarsToSearch)
				applyForAllClassesIn(p,this::search);
			hasSearched = true;
		}
		
		public void injectClass(String name,ClassWriter writer) {
			modifiedClasses.put(name, writer);
		}
		
		private void search(String cl,JarEntry e) {
			try {
				ClassReader r = readerFor(currentJar,e);
				visitors.stream()
					.map(f->f.apply(r))
					.filter(v->v instanceof ClassWriter)
					.findAny()
					.ifPresent(w->injectClass(cl,(ClassWriter)w));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		void addLoadableJar(Path p) {
			jarsToSearch.add(p);
			if(hasSearched) {
				applyForAllClassesIn(p,this::search);
			}
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			// TODO Auto-generated method stub
			return super.loadClass(name, resolve);
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			// TODO Auto-generated method stub
			return super.findClass(name);
		}

		@Override
		protected URL findResource(String moduleName, String name) throws IOException {
			// TODO Auto-generated method stub
			return super.findResource(moduleName, name);
		}

		@Override
		protected URL findResource(String name) {
			// TODO Auto-generated method stub
			return super.findResource(name);
		}

		@Override
		protected Enumeration<URL> findResources(String name) throws IOException {
			// TODO Auto-generated method stub
			return super.findResources(name);
		}

		@Override
		protected String findLibrary(String libname) {
			// TODO Auto-generated method stub
			return super.findLibrary(libname);
		}
	}
	
	public GameLauncher(Path gameFolder,Path gameJar,String[] args,Map<String,String> properties) throws IOException {
		this.args = args;
		this.properties = properties;
		properties.put("sentry.game.path", gameFolder.toString());
		properties.put("sentry.game.jarfile",gameJar.toString());
		this.gameFolder = gameFolder;
		this.gameJar = gameJar;
		Path deps = gameFolder.resolve("bin/libs");
		loader =new GameClassLoader(gameJar,deps);
	}
	
	private void handlePhase() {
		switch(phase) {
		case PHASE0_1:
			break;
		case PHASE0_2:
			break;
		case PHASE0_3:
			break;
		case PHASE1_1:
			break;
		case PHASE1_2:
			break;
		case PHASE1_3:
			break;
		case PHASE2_1:
			break;
		case PHASE2_2:
			break;
		case PHASE2_3:
			break;
		case PHASE2_4:
			break;
		case PHASE3_1:
			break;
		case PHASE3_2:
			break;
		case PHASE4_1:
			break;
		case PHASE4_2:
			break;
		case PHASE4_3:
			break;
		default:
			break;
		
		}
	}
	
	public void toNextPhase() {
		this.phase = this.phase.nextPhase();
		handlePhase();
	}

	@Override
	public String[] getGameArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModInterface[] activeMods() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CCIVendor getLauncherVendor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Window getGameWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path getGameDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instrumentation getInstrumentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public Writer getLauncherWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SecurityManager getSandbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disableMods() {
		modsDisabled = true;
		//TODO call Disable individually on each ModObject.
	}

	@Override
	public Game getGameAnnotation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ModInterface doInstallMod(ModResolver resolver) {
		return null;
	}

	@Override
	public ModInterface installMod(URI name) throws UnsupportedOperationException, IOException {
		switch(name.getScheme()) {
		case "http":
		case "https":
		case "ftp":
		case "magnet":
			return doInstallMod(new ModResolver(Downloader.getDownloader(name)));
		case "file":
			return doInstallMod(new ModResolver(Path.of(name)));
		case "sentry":
			return doInstallMod(new ModResolver(SentryLauncher.preexistingMod(name)));
			
		}
		return null;
	}
	
	public ClassLoader getClassLoader() {
		return null;
	}

	public LoadingPhase getLoadingPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProviderFromAnnotation(Provider provider) {
		// TODO Auto-generated method stub
		
	}
	
	private Map<LoadingPhase,Consumer<GameLauncher>> loadingPhases = new HashMap<>(); 
	
	public void addLoadingStep(ILoadingStep step) {
		LoadingPhase phase = step.phase();
		if(loadingPhases.containsKey(phase))
			loadingPhases.compute(phase, (_phase,existing)->existing.andThen(step::load));
		else
			loadingPhases.put(phase, step::load);
	}
	
}
