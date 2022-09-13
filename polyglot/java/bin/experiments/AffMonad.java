package experiments;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AffMonad implements Monad<Aff>  {
	public <a> App<Aff, a> return_(a value) {
		return Aff.create(CompletableFuture.completedStage(value));
	}
	
	public <a, b> App<Aff, b> bind(App<Aff, a> aff, Function<a, App<Aff, b>> f) {
		CompletionStage<a> fx = Aff.extract(aff);
		
		CompletionStage<b> fy = fx.thenCompose(x -> {
			return Aff.extract(f.apply(x));
		});
		
		return Aff.create(fy);
	}
}