package github.chorman0773.sentry.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Downloader {
	
	
	private static final Map<String,Function<URI,? extends Downloader>> schemes;
	
	private static class HttpDownloader extends Downloader{
		
		private URL url;
		
		public HttpDownloader(URI uri) {
			try {
				this.url = uri.toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
		
		
		@Override
		public Path downloadFile(Path root) throws IOException {
			String file = url.getPath();
			if(file.endsWith("/"))
				file = file.substring(0,file.length()-1);
			file = file.substring(file.lastIndexOf('/')+1);
			InputStream in = url.openStream();
			Path out = root.resolve(file);
			try(OutputStream strm = Files.newOutputStream(out)){
				byte[] buffer = new byte[1024];
				int len;
				while((len=in.read(buffer))!=-1)
					strm.write(buffer, 0, len);
			}
			return null;
		}
		
	}
	
	static {
		schemes = new TreeMap<>();
		schemes.put("http", HttpDownloader::new);
		schemes.put("https", HttpDownloader::new);
		
	}
	
	public static Downloader getDownloader(URI uri) {
		String scheme = uri.getScheme();
		if(schemes.containsKey(scheme))
			return schemes.get(scheme).apply(uri);
		else
			return null;
	}
	public abstract Path downloadFile(Path root)throws IOException;
}
