package experiments;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;

public class Aff {
	private static Aff token = new Aff();
	
	public static AffMonad MonadInstance = new AffMonad();
	
	public static <T> App<Aff, T> completionStage(CompletionStage<T> value) {
        return new App<Aff, T>(token, value);
	}

    protected static <T> CompletionStage<T> leave(App<Aff, T> app) {
		return (CompletionStage<T>) app.apply(token);
	}
	
	public static <T> T runAff(App<Aff, T> aff) throws InterruptedException, ExecutionException {
		CompletionStage<T> x = leave(aff);
		return x.toCompletableFuture().get();
	}
	
	public static <T> App<Aff, Object[]> whenAll(Iterable<App<Aff, T>> affs) {
		CompletableFuture<T>[] xs =
			StreamSupport
			.stream(affs.spliterator(), false)
			.map(Aff::leave)
			.map(CompletionStage::toCompletableFuture)
			.toArray(s -> new CompletableFuture[s]);
		
		CompletableFuture<Void> allDone = CompletableFuture.allOf(xs);
		CompletableFuture<Object[]> result = 
			allDone
			.<Object[]>thenCompose(unused -> {
				try {
					Object[] ys = new Object[xs.length];
					for (int i = 0; i < xs.length; i++) {
						ys[i] = xs[i].get();
					}
					return CompletableFuture.completedFuture(ys);
				} catch (Throwable ex) {
					return CompletableFuture.failedFuture(ex);
				}
			});
		
		return completionStage(result);
	}

	public static <T> App<Aff, T> whenAny(Iterable<App<Aff, T>> affs) {
		CompletableFuture<T>[] xs =
			StreamSupport
			.stream(affs.spliterator(), false)
			.map(Aff::leave)
			.map(CompletionStage::toCompletableFuture)
			.toArray(s -> new CompletableFuture[s]);
		
		CompletableFuture<T> result = (CompletableFuture<T>) CompletableFuture.anyOf(xs);
		
		return completionStage(result);
	}
}