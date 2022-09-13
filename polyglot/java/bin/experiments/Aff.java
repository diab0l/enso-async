package experiments;

import java.util.concurrent.CompletionStage;

public class Aff {
	private static Aff token = new Aff();
	
	protected static <T> App<Aff, T> create(CompletionStage<T> value) {
        return new App<Aff, T>(token, value);
	}

	protected static <T> App<Aff, T> enter(CompletionStage<T> value) {
        return new App<Aff, T>(token, value);
	}
	
    protected static <T> CompletionStage<T> extract(App<Aff, T> app) {
		return (CompletionStage<T>) app.apply(token);
	}
	
	protected static <T> CompletionStage<T> exit(App<Aff, T> app) {
		return (CompletionStage<T>) app.apply(token);
	}
}