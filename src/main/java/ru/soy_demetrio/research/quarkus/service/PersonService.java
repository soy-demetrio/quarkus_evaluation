package ru.soy_demetrio.research.quarkus.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import ru.soy_demetrio.research.quarkus.domain.Person;

public interface PersonService {

	public Multi<Person> getAll();
	
	public Uni<Person> getById(long argId); 
	
	public Uni<Long> save(Person arg);
	
	public Uni<Boolean> delete(long argId);
	
}
