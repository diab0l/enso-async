package experiments;

import java.util.function.Function;

public interface Functor<F> {
	<a, b> App<F, b> map(App<F, a> fa, Function<a, b> f);
}
