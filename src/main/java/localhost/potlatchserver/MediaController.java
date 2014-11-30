package localhost.potlatchserver;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import localhost.potlatchserver.repository.Media;
import localhost.potlatchserver.repository.MediaRepository;
import localhost.potlatchserver.repository.MediaStatus;

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
public class MediaController {
	
	@Autowired
	private MediaRepository media;

 	@RequestMapping(value="/media", method=RequestMethod.GET)
	public @ResponseBody Iterable<Media> getMediaList() {
 		return media.findAll();
	}
	
 	@RequestMapping(value="/media/unflagged", method=RequestMethod.GET)
	public @ResponseBody Iterable<Media> getMediaListUnflagged() {
 		return media.findByFlags(0);
	}
	
	@RequestMapping(value="/media", method=RequestMethod.POST)
	public @ResponseBody Media addMedia(@RequestBody Media v) {
		Media retval = media.save(v);
		return retval;
	}

    @RequestMapping(value="/media/{id}", method=RequestMethod.GET)
    public @ResponseBody Media getMedia(
    		@PathVariable("id") long id,
    		HttpServletResponse response)
    {
    	Media v = media.findOne(id);
    	if (v == null) {
        	response.setStatus(HttpStatus.NOT_FOUND.value());
        	return null;
    	} else {
    		return v;
    	}
    }
    
    @RequestMapping(value="/media/{id}/like", method=RequestMethod.POST)
    public @ResponseBody MediaStatus likeMedia (
			@PathVariable("id") long id,
    		HttpServletResponse response,
    		Principal p
    		) {
		Media v = media.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			String user = p.getName();
			if (v.getLikers().contains(user)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				v.addLiker(user);
				media.save(v);
			}
		}
		return new MediaStatus(MediaStatus.MediaState.READY);
    }
    
    @RequestMapping(value="/media/{id}/unlike", method=RequestMethod.POST)
    public @ResponseBody MediaStatus unlikeMedia (
			@PathVariable("id") long id,
    		HttpServletResponse response,
    		Principal p
    		) {
		Media v = media.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			String user = p.getName();
			if (!v.getLikers().contains(user)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				v.delLiker(user);
				media.save(v);
			}
		}
		return new MediaStatus(MediaStatus.MediaState.READY);
    }
    
    @RequestMapping(value="/media/{id}/likedby", method=RequestMethod.GET)
    public @ResponseBody Set<String> likedby (
			@PathVariable("id") long id,
    		HttpServletResponse response
    		) {
		Media v = media.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		} else {
			return v.getLikers();
		}
    }
    
    @RequestMapping(value="/media/{id}/flag", method=RequestMethod.POST)
    public @ResponseBody MediaStatus flagMedia (
			@PathVariable("id") long id,
    		HttpServletResponse response,
    		Principal p
    		) {
		Media v = media.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			String user = p.getName();
			if (v.getFlaggers().contains(user)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				v.addFlagger(user);
				media.save(v);
			}
		}
		return new MediaStatus(MediaStatus.MediaState.READY);
    }
    
    @RequestMapping(value="/media/{id}/unflag", method=RequestMethod.POST)
    public @ResponseBody MediaStatus unflagMedia (
			@PathVariable("id") long id,
    		HttpServletResponse response,
    		Principal p
    		) {
		Media v = media.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			String user = p.getName();
			if (!v.getFlaggers().contains(user)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				v.delFlagger(user);
				media.save(v);
			}
		}
		return new MediaStatus(MediaStatus.MediaState.READY);
    }
    
    @RequestMapping(value="/media/{id}/flaggedby", method=RequestMethod.GET)
    public @ResponseBody Set<String> flaggedby (
			@PathVariable("id") long id,
    		HttpServletResponse response
    		) {
		Media v = media.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		} else {
			return v.getFlaggers();
		}
    }
    
 	@RequestMapping(value="/media/search/findByName", method=RequestMethod.GET)
	public @ResponseBody Iterable<Media> getMediaByName(
			@RequestParam String title) {
 		return media.findByName(title);
	}
	
}
