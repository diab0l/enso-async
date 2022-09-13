package experiments;

import java.util.function.Function;

public interface Monad<M> extends Applicative<M> {
	<a> App<M, a> return_(a value);
	
	
	<a, b> App<M, b> bind(App<M, a> ma, Function<a, App<M, b>> f);
	
	
	default public <a> App<M, a> pure(a value) {
		return return_(value);
	}
	
	
	default public <a, b> App<M, b> apply(App<M, Function<a, b>> appF, App<M, a> app) {
		return bind(appF, f -> bind(app, x -> return_(f.apply(x))));
	}
}
