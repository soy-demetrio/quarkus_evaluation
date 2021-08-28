package ru.soy_demetrio.research.quarkus.service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import ru.soy_demetrio.research.quarkus.domain.Person;
import ru.soy_demetrio.research.quarkus.supplementary.ConstraintViolatedException;

/**
 * Обёртка, обеспечивающая валидацию данных перед сохранением, и выдачу человекочитаемых сообщений об ошибках. 
 * @author soy_demetrio
 */
@Decorator
@Priority(10)
public class PersonServiceValidatingDecorator implements PersonService {

	@Inject
	@Delegate
	private PersonService nextInChain;
	
	@Inject
	Validator validator;

	@Override
	public Multi<Person> getAll() {
		return nextInChain.getAll();
	}
	
	@Override
	public Uni<Person> getById(long arg) {
		return nextInChain.getById(arg);
	}

	@Override
	public Uni<Long> save(Person arg) {
		// TODO Понять, в каком потоке вызывается метод. Быть может, асинхронная валидация не нужна.
		Uni<Void> asyncValidator = Uni.createFrom().completionStage(CompletableFuture.supplyAsync(() -> {
			ensureDataValid(arg);
			return null;
		}));
		
		return asyncValidator.flatMap(item -> nextInChain.save(arg));
	}

	@Override
	public Uni<Boolean> delete(long argId) {
		return nextInChain.delete(argId);
	}
	
	/**
	 * Проверка соблюдения ограничений, заданных аннотациями javax.validation.*.
	 * @param arg
	 * @throws ConstraintViolatedException Есть невалидные данные. 
	 */
	private void ensureDataValid(Person arg) throws ConstraintViolatedException {
		Set<ConstraintViolation<Person>> problems = validator.validate(arg);
		if (!problems.isEmpty()) {
			StringBuilder bf = new StringBuilder();
			problems.stream().forEach(item -> bf.append(item.getMessage()).append("\r\n"));
			throw new ConstraintViolatedException(bf.toString());
		}
	}
}
