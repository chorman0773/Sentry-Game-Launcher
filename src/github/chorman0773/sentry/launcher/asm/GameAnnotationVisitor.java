package github.chorman0773.sentry.launcher.asm;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.UUID;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import github.chorman0773.sentry.annotation.Game;
import github.chorman0773.sentry.annotation.Game.ClassInitFailBehavior;
import github.chorman0773.sentry.annotation.Provider;
import github.chorman0773.sentry.launcher.GameDescriptor;
import github.chorman0773.sentry.launcher.GameLauncher;
import github.lightningcreations.lclib.Version;

public class GameAnnotationVisitor extends AnnotationVisitor {
	private UUID gameUuid;
	private String gameId;
	private String name;
	private Version v;
	private long serialId;
	private boolean allowsMods = true;
	private boolean displayVersion;
	private List<ClassInitializer> initializers;
	private GameDescriptor g;
	private Provider provider;
	private GameLauncher launcher;
	
	private class ClassesToInitVisitor extends AnnotationVisitor{

		public ClassesToInitVisitor() {
			super(Opcodes.ASM7);
			
		}

		@Override
		public AnnotationVisitor visitAnnotation(String name, String descriptor) {
			return new ClassInitializerVisitor();
		}
		
	}
	
	private class ClassInitializerVisitor extends AnnotationVisitor{
		private ClassInitFailBehavior behavior;
		private Type name;
		private ClassInitializer init;
		public ClassInitializerVisitor() {
			super(Opcodes.ASM7);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void visit(String name, Object value) {
			if(name.equals("value"))
				this.name = (Type)value;
			else
				throw new LinkageError("Bad Field: "+name);
		}
		@Override
		public void visitEnum(String name, String descriptor, String value) {
			if(name.equals("failBehavior"))
				this.behavior = ClassInitFailBehavior.valueOf(value);
			else
				throw new LinkageError("Bad Field: "+name);
		}
		@Override
		public void visitEnd() {
			init = new ClassInitializer(name.getInternalName(),behavior);
			GameAnnotationVisitor.this.initializers.add(init);
		}
		
	}
	
	private class ProviderVisitor extends AnnotationVisitor{
		public String name = "";
		public String id = "";
		public String publicKey = "";
		public Provider.KeyType keyType = Provider.KeyType.Empty;
		
		public ProviderVisitor() {
			super(Opcodes.ASM7);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void visit(String name, Object value) {
			if(name.equals("humanName"))
				this.name = (String)value;
			else if(name.equals("id"))
				this.id = (String)value;
			else if(name.equals("publicKey"))
				publicKey = (String)value;
			else
				throw new LinkageError("Bad Field");
		}
		
		@Override
		public void visitEnum(String name, String descriptor, String value) {
			if(name.equals("keyType"))
				this.keyType = Provider.KeyType.valueOf(value);
			else
				throw new LinkageError("Bad Field");
		}
		
		
		public void visitEnd() {
			provider = new Provider() {

				@Override
				public Class<? extends Annotation> annotationType() {
					// TODO Auto-generated method stub
					return Provider.class;
				}

				@Override
				public String id() {
					// TODO Auto-generated method stub
					return ProviderVisitor.this.id;
				}

				@Override
				public String humanName() {
					// TODO Auto-generated method stub
					return ProviderVisitor.this.name;
				}

				@Override
				public String publicKey() {
					// TODO Auto-generated method stub
					return ProviderVisitor.this.publicKey;
				}

				@Override
				public KeyType keyType() {
					// TODO Auto-generated method stub
					return ProviderVisitor.this.keyType;
				}
			};
		}
		
	}
	
	
	
	
	public GameAnnotationVisitor(GameLauncher launcher) {
		super(Opcodes.ASM7);
		this.launcher = launcher;
	}

	@Override
	public void visit(String name, Object value) {
		switch(name) {
		case "displayVersion":
			this.displayVersion = (Boolean)value;
		break;
		case "allowsMods":
			this.allowsMods = (Boolean)value;
		break;
		case "gameId":
			this.gameId = (String)value;
		break;
		case "uuid":
			this.gameUuid = UUID.fromString((String)value);
		break;
		case "gameName":
			this.name = (String)value;
		break;
		case "gameVersion":
			this.v = new Version((String)value);
		break;
		case "serialId":
			this.serialId = (Long)serialId;
		break;
		default:
			throw new LinkageError("Bad Field");
		}
	}

	@Override
	public AnnotationVisitor visitAnnotation(String name, String descriptor) {
		if(name.equals("provider"))
				return new ProviderVisitor();
		else
			throw new LinkageError("Bad Field");
	}

	@Override
	public AnnotationVisitor visitArray(String name) {
		if(name.equals("classesToInit"))
			return new ClassesToInitVisitor();
		else
			throw new LinkageError("Bad Field");
	}

	@Override
	public void visitEnd() {
		g = new GameDescriptor(gameUuid,gameId,name,v);
		launcher.setProviderFromAnnotation(provider);
		StringBuilder title = new StringBuilder(name);
		if(displayVersion)
			title.append(" ").append(v);
		if(!allowsMods)
			launcher.disableMods();
	}

	@Override
	public void visitEnum(String name, String descriptor, String value) {
		// TODO Auto-generated method stub
		super.visitEnum(name, descriptor, value);
	}

}
