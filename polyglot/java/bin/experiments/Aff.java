package experiments;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

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
}