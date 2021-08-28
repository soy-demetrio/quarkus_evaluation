package ru.soy_demetrio.research.quarkus.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import ru.soy_demetrio.research.quarkus.domain.Person;

/**
 * Реализация сервиса доступа к данным физлиц с прямым доступом к БД. 
 * @author soy_demetrio
 */
@ApplicationScoped
public class DirectDBAccessPersonService implements PersonService {
	private static final String CORE_SELECT_QUERY = "select id, first_name, family_name, birth_date from ent_person";
	private static final String SELECT_BY_ID_QUERY = CORE_SELECT_QUERY + " where id = $1";
	private static final String INSERT_QUERY = "insert into ent_person(first_name, family_name, birth_date) values($1, $2, $3) returning id";
	private static final String UPDATE_QUERY = "update ent_person set first_name = $1, family_name = $2, birth_date = $3 where id = $4 returning 1";
	private static final String DELETE_QUERY = "delete from ent_person where id = $1";

    @Inject
    private io.vertx.mutiny.pgclient.PgPool client;

	@Override
	public Multi<Person> getAll() {
		return convertToMultiPerson(client.query(CORE_SELECT_QUERY).execute());
	}

	@Override
	public Uni<Person> getById(long argId) {
	    return client.preparedQuery(SELECT_BY_ID_QUERY).execute(Tuple.of(argId)) 
	            .onItem().transform(RowSet::iterator) 
	            .onItem().transform(iterator -> iterator.hasNext() ? dbRecord2person(iterator.next()) : null); 	}

	@Override
	public Uni<Long> save(Person arg) {
		if (arg.isNew()) {
		    return client.preparedQuery(INSERT_QUERY).execute(Tuple.of(arg.getFirstName(), arg.getFamilyName(), arg.getBirthDate()))
		            .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));			
		} else {
		    return client.preparedQuery(UPDATE_QUERY).execute(Tuple.of(arg.getFirstName(), arg.getFamilyName(), arg.getBirthDate(), arg.getId()))
		            .onItem().transform(RowSet::iterator)
		            .onItem().transform(iterator -> iterator.hasNext() ? arg.getId() : null); 	
			
		}
	}

	@Override
	public Uni<Boolean> delete(long argId) {
	    return client.preparedQuery(DELETE_QUERY).execute(Tuple.of(argId))
	            .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1); 
	}

	
	private Multi<Person> convertToMultiPerson(Uni<RowSet<Row>> argRowSet) {
		return argRowSet
				  .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
				  .onItem().transform(DirectDBAccessPersonService::dbRecord2person);
	}
	
	private static Person dbRecord2person(Row argRow) {
		return Person.builder()
				.id(argRow.getLong("id"))
				.firstName(argRow.getString("first_name"))
				.familyName(argRow.getString("family_name"))
				.birthDate(argRow.getLocalDate("birth_date"))
				.build();
	}
	
	
}
