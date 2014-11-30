package localhost.potlatchserver.client;

import java.util.Collection;

import localhost.potlatchserver.repository.Chain;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * This interface defines an API for a ChainSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * interface into a client capable of sending the appropriate
 * HTTP requests.
 * 
 * The HTTP API that you must implement so that this interface
 * will work:
 * 
 * GET /chain
 *    - Returns the list of chain that has been added to the
 *      server as JSON. The list of chain should be persisted
 *      using Spring Data. The list of Chain objects should be able 
 *      to be unmarshalled by the client into a Collection<Chain>.
 *    - The return content-type should be application/json, which
 *      will be the default if you use @ResponseBody
 * 
 *      
 * POST /chain
 *    - The chain metadata is provided as an application/json request
 *      body. The JSON should generate a valid instance of the 
 *      Chain class when deserialized by Spring's default 
 *      Jackson library.
 *    - Returns the JSON representation of the Chain object that
 *      was stored along with any updates to that object made by the server. 
 *    - **_The server should store the Chain in a Spring Data JPA repository.
 *    	 If done properly, the repository should handle generating ID's._** 
 *    - Chain should not have any likes when it is initially created.
 *    - You will need to add one or more annotations to the Chain object
 *      in order for it to be persisted with JPA.
 * 
 * GET /chain/{id}
 *    - Returns the chain with the given id or 404 if the chain is not found.
 *      
 * GET /chain/search/findByName?name={name}
 *    - Returns a list of chain whose names match the given parameter or an empty
 *      list if none are found.
 *     
 *     
 * The ChainSvcApi interface described below should be used as the ultimate ground
 * truth for what should be implemented in the assignment. If there are any details
 * in the description above that conflict with the ChainSvcApi interface below, use
 * the details in the ChainSvcApi interface and report the discrepancy on the course
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
public interface ChainSvcApi {

	public static final String NAME_PARAMETER = "name";

	// The path where we expect the ChainSvc to live
	public static final String CHAIN_SVC_PATH = "/chain";

	// The path to search chain by name
	public static final String CHAIN_NAME_SEARCH_PATH = CHAIN_SVC_PATH + "/search/findByName";

	@GET(CHAIN_SVC_PATH)
	public Collection<Chain> getChainList();

	@GET(CHAIN_SVC_PATH + "/{id}")
	public Chain getChainById(@Path("id") long id);

	@POST(CHAIN_SVC_PATH)
	public Chain addChain(@Body Chain v);

	@GET(CHAIN_NAME_SEARCH_PATH)
	public Collection<Chain> findByName(@Query(NAME_PARAMETER) String name);
}
