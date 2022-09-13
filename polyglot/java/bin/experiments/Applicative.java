package experiments;

import java.util.function.Function;

public interface Applicative<A> extends Functor<A> {
	<a> App<A, a> pure(a value);
		
	<a, b> App<A, b> apply(App<A, Function<a, b>> appF, App<A, a> app);

	default public <a, b> App<A, b> map(App<A, a> fa, Function<a, b> f) {
		return this.apply(pure(f), fa);
	}
}