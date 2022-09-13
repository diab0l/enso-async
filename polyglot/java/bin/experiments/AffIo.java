package experiments;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.concurrent.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;

public class AffIo {
	public static App<Aff, String> readFile(String location) {
		Path path = Paths.get(location);

		CompletableFuture<String> result = FileAsync.readFile(path, 1024, StandardCharsets.UTF_8);

		return Aff.create(result);
	}
	
}
