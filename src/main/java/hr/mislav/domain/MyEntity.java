package hr.mislav.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MyEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	public MyEntity(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MyEntity myEntity = (MyEntity) o;
		return Objects.equals(id, myEntity.id) &&
				Objects.equals(name, myEntity.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
