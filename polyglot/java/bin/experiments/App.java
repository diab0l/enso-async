package experiments;

import java.util.function.Function;

public class App<T, a> {
	private T token;
	private Object value;
	
	public App(T token, Object value) {
		this.token = token;
		this.value = value;
	}
	
	public <T, U> Object apply(T token) {
		if (token != this.token) {
			throw new UnsupportedOperationException("invalid token");
		}
		
		return this.value;
	}
}
