package localhost.potlatchserver;

import javax.servlet.http.HttpServletResponse;

import localhost.potlatchserver.repository.Chain;
import localhost.potlatchserver.repository.ChainRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class ChainController {
	
	@Autowired
	private ChainRepository chain;

 	@RequestMapping(value="/chain", method=RequestMethod.GET)
	public @ResponseBody Iterable<Chain> getChainList() {
 		return chain.findAll();
	}
	
	@RequestMapping(value="/chain", method=RequestMethod.POST)
	public @ResponseBody Chain addChain(@RequestBody Chain v) {
		Chain retval = chain.save(v);
		return retval;
	}

    @RequestMapping(value="/chain/{id}", method=RequestMethod.GET)
    public @ResponseBody Chain getChain(
    		@PathVariable("id") long id,
    		HttpServletResponse response)
    {
    	Chain v = chain.findOne(id);
    	if (v == null) {
        	response.setStatus(HttpStatus.NOT_FOUND.value());
        	return null;
    	} else {
    		return v;
    	}
    }
    
 	@RequestMapping(value="/chain/search/findByName", method=RequestMethod.GET)
	public @ResponseBody Iterable<Chain> getChainByName(
			@RequestParam String name) {
		return chain.findByName(name);
	}
	
}
