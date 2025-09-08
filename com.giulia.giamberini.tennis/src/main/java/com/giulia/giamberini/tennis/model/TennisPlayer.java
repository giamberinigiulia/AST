package com.giulia.giamberini.tennis.model;

import java.util.Objects;

public class TennisPlayer {

	private String id;
	private String name;
	private String surname;

	public TennisPlayer(String id, String name, String surname) {
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TennisPlayer other = (TennisPlayer) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(surname, other.surname);
	}

	@Override
	public String toString() {
		return "TennisPlayer [id=" + id + ", name=" + name + ", surname=" + surname + "]";
	}

}
