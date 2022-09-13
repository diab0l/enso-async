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
import java.io.ByteArrayOutputStream;
import java.lang.System;

public class FileAsync {
	public static String pwd() {
		return System.getProperty("user.dir");
	}

	public static App<Aff, String> readFile(String location) {
		Path path = Paths.get(location);

		CompletableFuture<String> result = FileAsync.readFileAsync(path, 1024, StandardCharsets.UTF_8);

		return Aff.completionStage(result);
	}

	private static CompletableFuture<String> readFileAsync(String location) {
		Path path = Paths.get(location);
		int bufferSize = 1024;
		Charset encoding = StandardCharsets.UTF_8;
		
		return readAllBytesAsync(path, bufferSize)
			   .thenApply(bytes -> new String(bytes, encoding));
    }

	private static CompletableFuture<String> readFileAsync(Path path, int bufferSize, Charset encoding) {
		return readAllBytesAsync(path, bufferSize)
			   .thenApply(bytes -> new String(bytes, encoding));
    }
	
	private static CompletableFuture<byte[]> readAllBytesAsync(Path path, int bufferSize) {
		CompletableFuture<byte[]> result = new CompletableFuture<>();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		
		try {
			AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
			
			readFileAsync(afc, 0, buffer, outStream, result);
		} catch(IOException exc) {
			result.completeExceptionally(exc);
		}
		
		return result;
    }
	
	private static void readFileAsync(AsynchronousFileChannel afc, Integer position, ByteBuffer buffer, ByteArrayOutputStream acc, CompletableFuture<byte[]> xs) {
		afc.read(buffer, position, acc, new CompletionHandler<Integer, ByteArrayOutputStream>() {
			@Override
			public void completed(Integer numberOfBytesRead, ByteArrayOutputStream attachment) {
				if(numberOfBytesRead == -1) {
					xs.complete(attachment.toByteArray());
				} else {
					buffer.flip();
					
					// TODO: optimize this by using bytebuffer with backing array and the write(b, pos, len) overload
					byte[] arr = new byte[buffer.remaining()];
					buffer.get(arr);
					attachment.writeBytes(arr);
					
					buffer.rewind();
					readFileAsync(afc, position + numberOfBytesRead, buffer, attachment, xs);
				}
			}

			@Override
			public void failed(Throwable exc, ByteArrayOutputStream attachment) {
				xs.completeExceptionally(exc);
			}
		});
	}
}
