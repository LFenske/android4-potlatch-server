package localhost.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.magnum.autograder.junit.Rubric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import localhost.potlatchserver.client.MediaSvcApi;
import localhost.potlatchserver.client.SecuredRestBuilder;
import localhost.potlatchserver.repository.Media;
import localhost.media2.TestData;

import org.apache.http.HttpStatus;
import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

/**
 * A test for the Asgn2 media service
 * 
 * @author mitchell
 */
public class AutoGradingTest {

	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}

	private final String TEST_URL = "https://localhost:8443";

	private final String USERNAME1 = "admin";
	private final String USERNAME2 = "Alice";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";

	private MediaSvcApi readWriteMediaSvcUser1 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + MediaSvcApi.TOKEN_PATH)
			// .setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME1).setPassword(PASSWORD).setClientId(CLIENT_ID)
			.build().create(MediaSvcApi.class);

	private MediaSvcApi readWriteMediaSvcUser2 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + MediaSvcApi.TOKEN_PATH)
			// .setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME2).setPassword(PASSWORD).setClientId(CLIENT_ID)
			.build().create(MediaSvcApi.class);

	private Media media = TestData.randomMedia();


	@Rubric(value = "Media data is preserved", 
			goal = "The goal of this evaluation is to ensure that your Spring controller(s) "
			+ "properly unmarshall Media objects from the data that is sent to them "
			+ "and that the HTTP API for adding media is implemented properly. The"
			+ " test checks that your code properly accepts a request body with"
			+ " application/json data and preserves all the properties that are set"
			+ " by the client. The test also checks that you generate an ID and data"
			+ " URL for the uploaded media.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/61 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testAddMediaMetadata() throws Exception {
		Media received = readWriteMediaSvcUser1.addMedia(media);
		assertEquals(media.getName(), received.getName());
		assertEquals(media.getDescr(), received.getDescr());
		assertTrue(received.getLikes() == 0);
		assertTrue(received.getFlags() == 0);
		assertTrue(received.getId() > 0);
	}

	@Rubric(value = "The list of media is updated after an add", 
			goal = "The goal of this evaluation is to ensure that your Spring controller(s) "
			+ "can add media to the list that is stored in memory on the server."
			+ " The test also ensure that you properly return a list of media"
			+ " as JSON.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/61 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testAddGetMedia() throws Exception {
		readWriteMediaSvcUser1.addMedia(media);
		Collection<Media> stored = readWriteMediaSvcUser1.getMediaList();
		assertTrue(stored.contains(media));
	}

	@Rubric(value = "Requests without authentication token are denied.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "properly authenticates queries using the OAuth Password Grant flow."
			+ "Any query that does not contain the correct authorization token"
			+ "should be denied with a 401 error.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/117 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/127 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/123 ")
	@Test
	public void testDenyMediaAddWithoutOAuth() throws Exception {
		ErrorRecorder error = new ErrorRecorder();

		// Create an insecure version of our Rest Adapter that doesn't know how
		// to use OAuth.
		MediaSvcApi insecuremediaService = new RestAdapter.Builder()
				.setClient(
						new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
				.setErrorHandler(error).build().create(MediaSvcApi.class);
		try {
			// This should fail because we haven't logged in!
			insecuremediaService.addMedia(media);

			fail("Yikes, the security setup is horribly broken and didn't require the user to authenticate!!");

		} catch (Exception e) {
			// Ok, our security may have worked, ensure that
			// we got a 401
			assertEquals(HttpStatus.SC_UNAUTHORIZED, error.getError()
					.getResponse().getStatus());
		}

		// We should NOT get back the media that we added above!
		Collection<Media> medias = readWriteMediaSvcUser1.getMediaList();
		assertFalse(medias.contains(media));
	}

	@Rubric(value = "A user can like/unlike a media and increment/decrement the like count", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to like/unlike media using the /media/{id}/like endpoint, and"
			+ "and the /media/{id}/unlike endpoint."
			+ "Once a user likes/unlikes a media, the count of users that like that media"
			+ "should be incremented/decremented.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/121 ")
	@Test
	public void testLikeCount() throws Exception {

		// Add the media
		Media v = readWriteMediaSvcUser1.addMedia(media);

		// Like the media
		readWriteMediaSvcUser1.likeMedia(v.getId());

		// Get the media again
		v = readWriteMediaSvcUser1.getMediaById(v.getId());

		// Make sure the like count is 1
		assertTrue(v.getLikes() == 1);

		// Unlike the media
		readWriteMediaSvcUser1.unlikeMedia(v.getId());

		// Get the media again
		v = readWriteMediaSvcUser1.getMediaById(v.getId());

		// Make sure the like count is 0
		assertTrue(v.getLikes() == 0);
	}

	@Rubric(value = "A user can like/unlike a media and be added to/removed from the \"liked by\" list.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to like/unlike media using the /media/{id}/like endpoint"
			+ "and the /media/{id}/unlike endpoint."
			+ "Once a user likes/unlikes a media, the username should be added to/removed from the "
			+ "list of users that like that media.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/121 ")
	@Test
	public void testLikedBy() throws Exception {

		// Add the media
		Media v = readWriteMediaSvcUser1.addMedia(media);

		// Like the media
		readWriteMediaSvcUser1.likeMedia(v.getId());

		Collection<String> likedby = readWriteMediaSvcUser1.getUsersWhoLikedMedia(v.getId());

		// Make sure we're on the list of people that like this media
		assertTrue(likedby.contains(USERNAME1));
		
		// Have the second user like the media
		readWriteMediaSvcUser2.likeMedia(v.getId());
		
		// Make sure both users show up in the like list
		likedby = readWriteMediaSvcUser1.getUsersWhoLikedMedia(v.getId());
		assertTrue(likedby.contains(USERNAME1));
		assertTrue(likedby.contains(USERNAME2));

		// Unlike the media
		readWriteMediaSvcUser1.unlikeMedia(v.getId());

		// Get the media again
		likedby = readWriteMediaSvcUser1.getUsersWhoLikedMedia(v.getId());

		// Make sure user1 is not on the list of people that liked this media
		assertTrue(!likedby.contains(USERNAME1));
		
		// Make sure that user 2 is still there
		assertTrue(likedby.contains(USERNAME2));
	}

	@Rubric(value = "A user is only allowed to like a media once.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "restricts users to liking a media only once. "
			+ "This test simply attempts to like a media twice and then checks that "
			+ "the like count is only 1.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/121"
	)
	@Test
	public void testLikingTwice() throws Exception {

		// Add the media
		Media v = readWriteMediaSvcUser1.addMedia(media);

		// Like the media
		readWriteMediaSvcUser1.likeMedia(v.getId());

		// Get the media again
		v = readWriteMediaSvcUser1.getMediaById(v.getId());

		// Make sure the like count is 1
		assertTrue(v.getLikes() == 1);

		try {
			// Like the media again.
			readWriteMediaSvcUser1.likeMedia(v.getId());

			fail("The server let us like a media twice without returning a 400");
		} catch (RetrofitError e) {
			// Make sure we got a 400 Bad Request
			assertEquals(400, e.getResponse().getStatus());
		}

		// Get the media again
		v = readWriteMediaSvcUser1.getMediaById(v.getId());

		// Make sure the like count is still 1
		assertTrue(v.getLikes() == 1);
	}

	@Rubric(value = "A user cannot like a non-existant media", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "won't crash if a user attempts to like a non-existant media. "
			+ "This test simply attempts to like a non-existant media then checks "
			+ "that a 404 Not Found response is returned.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
					+ "https://class.coursera.org/mobilecloud-001/lecture/99 "
					+ "https://class.coursera.org/mobilecloud-001/lecture/121"
	)
	@Test
	public void testLikingNonExistantMedia() throws Exception {

		try {
			// Like the media again.
			readWriteMediaSvcUser1.likeMedia(getInvalidMediaId());

			fail("The server let us like a media that doesn't exist without returning a 404.");
		} catch (RetrofitError e) {
			// Make sure we got a 400 Bad Request
			assertEquals(404, e.getResponse().getStatus());
		}
	}

	@Rubric(value = "A user can find a media by providing its name", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to find media by searching for the media's name.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testFindByName() {

		// Create the names unique for testing.
		String[] names = new String[3];
		names[0] = "The Cat";
		names[1] = "The Spoon";
		names[2] = "The Plate";

		// Create three random media, but use the unique names
		ArrayList<Media> medias = new ArrayList<Media>();

		for (int i = 0; i < names.length; ++i) {
			medias.add(TestData.randomMedia());
			medias.get(i).setName(names[i]);
		}

		// Add all the media to the server
		for (Media v : medias){
			readWriteMediaSvcUser1.addMedia(v);
		}

		// Search for "The Cat"
		Collection<Media> searchResults = readWriteMediaSvcUser1.findByTitle(names[0]);
		assertTrue(searchResults.size() > 0);

		// Make sure all the returned media have "The Cat" for their title
		for (Media v : searchResults) {
			assertTrue(v.getName().equals(names[0]));
		}
	}

	/**
	 * Test finding a media by its duration.
	 */
	@Rubric(value = "A user can find media that have a duration less than a certain value.", 
			goal = "The goal of this evaluation is to ensure that your Spring application "
			+ "allows users to find media by searching for media with a duration "
			+ "less that a specified value.", 
			points = 20.0, 
			reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testFindByDurationLessThan() {

		// Create the durations unique for testing.
		long[] durations = new long[3];
		durations[0] = 1;
		durations[1] = 5;
		durations[2] = 9;

		// Create three random media, but use the unique durations
		ArrayList<Media> medias = new ArrayList<Media>();

		for (int i = 0; i < durations.length; ++i) {
			medias.add(TestData.randomMedia());
		}

		// Add all the media to the server
		for (Media v : medias){
			readWriteMediaSvcUser1.addMedia(v);
		}

//		// Search for "The Cat"
//		Collection<Media> searchResults = readWriteMediaSvcUser1.findByDurationLessThan(6L);
//		// Make sure that we have at least two media
//		assertTrue(searchResults.size() > 1);
	}

	private long getInvalidMediaId() {
		Set<Long> ids = new HashSet<Long>();
		Collection<Media> stored = readWriteMediaSvcUser1.getMediaList();
		for (Media v : stored) {
			ids.add(v.getId());
		}

		long nonExistantId = Long.MIN_VALUE;
		while (ids.contains(nonExistantId)) {
			nonExistantId++;
		}
		return nonExistantId;
	}

}
