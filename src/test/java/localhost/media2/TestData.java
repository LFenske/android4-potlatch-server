package localhost.media2;

import java.util.UUID;

import localhost.potlatchserver.repository.Media;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class to aid in the construction of
 * Media objects with random names, urls, and durations.
 * The class also provides a facility to convert objects
 * into JSON using Jackson, which is the format that the
 * MediaSvc controller is going to expect data in for
 * integration testing.
 * 
 * @author jules
 *
 */
public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Construct and return a Media object with a
	 * random name and url.
	 * 
	 * @return
	 */
	public static Media randomMedia() {
		// Information about the media
		// Construct a random identifier using Java's UUID class
		String id = UUID.randomUUID().toString();
		String title = "Media-"+id;
		String url = "http://coursera.org/some/media-"+id;
		String desc = "test description";
		return new Media(title, url, desc, 0, 0, 0);
	}
	
	/**
	 *  Convert an object to JSON using Jackson's ObjectMapper
	 *  
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
