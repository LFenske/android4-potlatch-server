package localhost.media.client;

import java.util.Collection;

import localhost.media.repository.Media;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 *                       DO NOT MODIFY THIS INTERFACE
                    ___                    ___           ___                            
     _____         /\  \                  /\  \         /\  \                           
    /::\  \       /::\  \                 \:\  \       /::\  \         ___              
   /:/\:\  \     /:/\:\  \                 \:\  \     /:/\:\  \       /\__\             
  /:/  \:\__\   /:/  \:\  \            _____\:\  \   /:/  \:\  \     /:/  /             
 /:/__/ \:|__| /:/__/ \:\__\          /::::::::\__\ /:/__/ \:\__\   /:/__/              
 \:\  \ /:/  / \:\  \ /:/  /          \:\~~\~~\/__/ \:\  \ /:/  /  /::\  \              
  \:\  /:/  /   \:\  /:/  /            \:\  \        \:\  /:/  /  /:/\:\  \             
   \:\/:/  /     \:\/:/  /              \:\  \        \:\/:/  /   \/__\:\  \            
    \::/  /       \::/  /                \:\__\        \::/  /         \:\__\           
     \/__/         \/__/                  \/__/         \/__/           \/__/           
      ___           ___                                     ___                         
     /\  \         /\  \         _____                     /\__\                        
    |::\  \       /::\  \       /::\  \       ___         /:/ _/_         ___           
    |:|:\  \     /:/\:\  \     /:/\:\  \     /\__\       /:/ /\__\       /|  |          
  __|:|\:\  \   /:/  \:\  \   /:/  \:\__\   /:/__/      /:/ /:/  /      |:|  |          
 /::::|_\:\__\ /:/__/ \:\__\ /:/__/ \:|__| /::\  \     /:/_/:/  /       |:|  |          
 \:\~~\  \/__/ \:\  \ /:/  / \:\  \ /:/  / \/\:\  \__  \:\/:/  /      __|:|__|          
  \:\  \        \:\  /:/  /   \:\  /:/  /   ~~\:\/\__\  \::/__/      /::::\  \          
   \:\  \        \:\/:/  /     \:\/:/  /       \::/  /   \:\  \      ~~~~\:\  \         
    \:\__\        \::/  /       \::/  /        /:/  /     \:\__\          \:\__\        
     \/__/         \/__/         \/__/         \/__/       \/__/           \/__/        
 * 
 * 
 * /**
 * This interface defines an API for a MediaSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * interface into a client capable of sending the appropriate
 * HTTP requests.
 * 
 * The HTTP API that you must implement so that this interface
 * will work:
 * 
 * POST /oauth/token
 *    - The access point for the OAuth 2.0 Password Grant flow.
 *    - Clients should be able to submit a request with their username, password,
 *       client ID, and client secret, encoded as described in the OAuth lecture
 *       videos.
 *    - The client ID for the Retrofit adapter is "mobile" with an empty password.
 *    - There must be 2 users, whose usernames are "user0" and "admin". All passwords 
 *      should simply be "pass".
 *    - Rather than implementing this from scratch, we suggest reusing the example
 *      configuration from the OAuth 2.0 example in GitHub by copying these classes over:
 *      https://github.com/juleswhite/mobilecloud-14/tree/master/examples/9-VideoServiceWithOauth2/src/main/java/org/magnum/mobilecloud/video/auth
 *      You will need to @Import the OAuth2SecurityConfiguration into your Application or
 *      other configuration class to enable OAuth 2.0. You will also need to remove one
 *      of the containerCustomizer() methods in either OAuth2SecurityConfiguration or
 *      Application (they are the exact same code). You may need to customize the users
 *      in the OAuth2Config constructor or the security applied by the ResourceServer.configure(...) 
 *      method. You should determine what (if any) adaptations are needed by comparing this 
 *      and the test specification against the code in that class.
 *  
 * GET /media
 *    - Returns the list of media that has been added to the
 *      server as JSON. The list of media should be persisted
 *      using Spring Data. The list of Media objects should be able 
 *      to be unmarshalled by the client into a Collection<Media>.
 *    - The return content-type should be application/json, which
 *      will be the default if you use @ResponseBody
 * 
 *      
 * POST /media
 *    - The media metadata is provided as an application/json request
 *      body. The JSON should generate a valid instance of the 
 *      Media class when deserialized by Spring's default 
 *      Jackson library.
 *    - Returns the JSON representation of the Media object that
 *      was stored along with any updates to that object made by the server. 
 *    - **_The server should store the Media in a Spring Data JPA repository.
 *    	 If done properly, the repository should handle generating ID's._** 
 *    - Media should not have any likes when it is initially created.
 *    - You will need to add one or more annotations to the Media object
 *      in order for it to be persisted with JPA.
 * 
 * GET /media/{id}
 *    - Returns the media with the given id or 404 if the media is not found.
 *      
 * POST /media/{id}/like
 *    - Allows a user to like a media. Returns 200 Ok on success, 404 if the
 *      media is not found, or 400 if the user has already liked the media.
 *    - The service should should keep track of which users have liked a media and
 *      prevent a user from liking a media twice. A POJO Media object is provided for 
 *      you and you will need to annotate and/or add to it in order to make it persistable.
 *    - A user is only allowed to like a media once. If a user tries to like a media
 *       a second time, the operation should fail and return 400 Bad Request.
 *      
 * POST /media/{id}/unlike
 *    - Allows a user to unlike a media that he/she previously liked. Returns 200 OK
 *       on success, 404 if the media is not found, and a 400 if the user has not 
 *       previously liked the specified media.
 *       
 * GET /media/{id}/likedby
 *    - Returns a list of the string usernames of the users that have liked the specified
 *      media. If the media is not found, a 404 error should be generated.
 * 
 * GET /media/search/findByName?title={title}
 *    - Returns a list of media whose titles match the given parameter or an empty
 *      list if none are found.
 *     
 * GET /media/search/findByDurationLessThan?duration={duration}
 *    - Returns a list of media whose durations are less than the given parameter or
 *      an empty list if none are found.	
 *     
 *     
 * The MediaSvcApi interface described below should be used as the ultimate ground
 * truth for what should be implemented in the assignment. If there are any details
 * in the description above that conflict with the MediaSvcApi interface below, use
 * the details in the MediaSvcApi interface and report the discrepancy on the course
 * forums. 
 * 
 * For the ultimate ground truth of how the assignment will be graded, please see 
 * AutoGradingTest, which shows the specific tests that will be run to grade your
 * solution. 
 *   
 * @author jules
 *
 *
 */
public interface MediaSvcApi {

	public static final String TITLE_PARAMETER = "title";
	
	//TODO
	public static final String DURATION_PARAMETER = "duration";
	
	public static final String FLAGS_PARAMETER = "flags";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path where we expect the MediaSvc to live
	public static final String MEDIA_SVC_PATH = "/media";

	// The path to search media by title
	public static final String MEDIA_TITLE_SEARCH_PATH = MEDIA_SVC_PATH + "/search/findByName";
	
	//TODO
	// The path to search media by duration
	public static final String MEDIA_DURATION_SEARCH_PATH = MEDIA_SVC_PATH + "/search/findByDurationLessThan";

	// The path to search media by flag count
	public static final String MEDIA_FLAGS_SEARCH_PATH = MEDIA_SVC_PATH + "/search/findByFlags";

	@GET(MEDIA_SVC_PATH)
	public Collection<Media> getMediaList();
	
	@GET(MEDIA_SVC_PATH + "/unflagged")
	public Collection<Media> getMediaListUnflagged();
	
	@GET(MEDIA_SVC_PATH + "/{id}")
	public Media getMediaById(@Path("id") long id);
	
	@POST(MEDIA_SVC_PATH)
	public Media addMedia(@Body Media v);
	
	@POST(MEDIA_SVC_PATH + "/{id}/like")
	public Void likeMedia(@Path("id") long id);
	
	@POST(MEDIA_SVC_PATH + "/{id}/unlike")
	public Void unlikeMedia(@Path("id") long id);
	
	@POST(MEDIA_SVC_PATH + "/{id}/flag")
	public Void flagMedia(@Path("id") long id);
	
	@POST(MEDIA_SVC_PATH + "/{id}/unflag")
	public Void unflagMedia(@Path("id") long id);
	
	@GET(MEDIA_TITLE_SEARCH_PATH)
	public Collection<Media> findByTitle(@Query(TITLE_PARAMETER) String title);
	
	//TODO
	@GET(MEDIA_DURATION_SEARCH_PATH)
	public Collection<Media> findByDurationLessThan(@Query(DURATION_PARAMETER) long duration);
	
	@GET(MEDIA_FLAGS_SEARCH_PATH)
	public Collection<Media> findByFlags(@Query(FLAGS_PARAMETER) long flags);
	
	@GET(MEDIA_SVC_PATH + "/{id}/likedby")
	public Collection<String> getUsersWhoLikedMedia(@Path("id") long id);
	
	@GET(MEDIA_SVC_PATH + "/{id}/flaggedby")
	public Collection<String> getUsersWhoFlaggedMedia(@Path("id") long id);
}
