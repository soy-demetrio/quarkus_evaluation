package ru.soy_demetrio.research.quarkus.supplementary;

/**
 * При попытке сохранения сущности обнаружено, что данные сущности не соответствуют правилам валидации. 
 * @author soy_demetrio
 */
public class ConstraintViolatedException extends RuntimeException {
	private static final long serialVersionUID = -2798705542483194180L;

	public ConstraintViolatedException() {
		super();
	}

	public ConstraintViolatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConstraintViolatedException(String message) {
		super(message);
	}

	public ConstraintViolatedException(Throwable cause) {
		super(cause);
	}
}
