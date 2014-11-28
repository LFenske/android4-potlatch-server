package localhost.potlatchserver.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store Media
 * objects and allow them to be searched by title.
 * 
 * @author jules
 *
 */
@Repository
public interface MediaRepository extends CrudRepository<Media, Long>{

	// Find all medias with a matching title (e.g., Media.name)
	public Collection<Media> findByName(String title);
	
	//TODO
	//public Collection<Media> findByDurationLessThan(long duration);
	
	public Collection<Media> findByFlags(long flags);
	
}
