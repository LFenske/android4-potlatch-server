package localhost.media.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store Chain
 * objects and allow them to be searched by name.
 * 
 */
@Repository
public interface ChainRepository extends CrudRepository<Chain, Long>{

	// Find all medias with a matching title (e.g., Media.name)
	public Collection<Chain> findByName(String name);
	
}
