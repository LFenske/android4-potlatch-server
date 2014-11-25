package localhost.media.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

/**
 * A simple object to represent a gift chain.
 * 
 */
@Entity
public class Chain {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	
	public Chain() {
	}

	public Chain(String name) {
		super();
		this.name    = name;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Two media will generate the same hashcode if they have exactly the same
	 * values for their id and name.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(id, name);
	}

	/**
	 * Two media are considered equal if they have exactly the same values for
	 * their name.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Chain) {
			Chain other = (Chain) obj;
			// Google Guava provides great utilities for equals too!
			return  Objects.equal(name, other.name) &&
					Objects.equal(id  , other.id  );
		} else {
			return false;
		}
	}

}
