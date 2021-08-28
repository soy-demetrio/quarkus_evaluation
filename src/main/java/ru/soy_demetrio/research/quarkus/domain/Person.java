package ru.soy_demetrio.research.quarkus.domain;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Физическое лицо.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {

	/**
	 * Первичный ключ.
	 */
	private Long id;
	
	/**
	 * Имя.
	 */
	@Size(max = 50, message="Превышена максимальная длина поля 'Имя': 50 символов.")
	private String firstName;
	
	/**
	 * Фамилия.
	 */
	@NotBlank(message = "Поле 'Фамилия' должно быть заполнено.")
	@Size(max = 100, message="Превышена максимальная длина поля 'Фамилия': 100 символов.")
	private String familyName;
	
	/**
	 * Дата рождения.
	 */
	@Past(message = "Неверно указана дата рождения.")
	private LocalDate birthDate;

	@JsonIgnore
	public boolean isNew() {
		return id == null;
	}
}
