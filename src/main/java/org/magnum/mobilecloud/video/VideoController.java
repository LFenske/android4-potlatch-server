package org.magnum.mobilecloud.video;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Set;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.magnum.mobilecloud.video.repository.VideoStatus;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class VideoController {
	
	@Autowired
	private VideoRepository videos;

 	@RequestMapping(value="/video", method=RequestMethod.GET)
	public @ResponseBody Iterable<Video> getVideoList() {
 		return videos.findAll();
	}
	
	@RequestMapping(value="/video", method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		Video retval = videos.save(v);
		return retval;
	}

    @RequestMapping(value="/video/{id}", method=RequestMethod.GET)
    public @ResponseBody Video getVideo(
    		@PathVariable("id") long id,
    		HttpServletResponse response)
    {
    	Video v = videos.findOne(id);
    	if (v == null) {
        	response.setStatus(HttpStatus.NOT_FOUND.value());
        	return null;
    	} else {
    		return v;
    	}
    }
    
    @RequestMapping(value="/video/{id}/like", method=RequestMethod.POST)
    public @ResponseBody VideoStatus likeVideo (
			@PathVariable("id") long id,
    		HttpServletResponse response,
    		Principal p
    		) {
		Video v = videos.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			String user = p.getName();
			if (v.getLikers().contains(user)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				v.addLiker(user);
				videos.save(v);
			}
		}
		return new VideoStatus(VideoStatus.VideoState.READY);
    }
    
    @RequestMapping(value="/video/{id}/unlike", method=RequestMethod.POST)
    public @ResponseBody VideoStatus unlikeVideo (
			@PathVariable("id") long id,
    		HttpServletResponse response,
    		Principal p
    		) {
		Video v = videos.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			String user = p.getName();
			if (!v.getLikers().contains(user)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				v.delLiker(user);
				videos.save(v);
			}
		}
		return new VideoStatus(VideoStatus.VideoState.READY);
    }
    
    @RequestMapping(value="/video/{id}/likedby", method=RequestMethod.GET)
    public @ResponseBody Set<String> likedby (
			@PathVariable("id") long id,
    		HttpServletResponse response
    		) {
		Video v = videos.findOne(id);
		if (v == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		} else {
			return v.getLikers();
		}
    }
    
 	@RequestMapping(value="/video/search/findByName", method=RequestMethod.GET)
	public @ResponseBody Iterable<Video> getVideoByName(
			@RequestParam String title) {
 		return videos.findByName(title);
	}
	
 	@RequestMapping(value="/video/search/findByDurationLessThan", method=RequestMethod.GET)
	public @ResponseBody Iterable<Video> getVideoByDurationLessThan(
			@RequestParam long duration) {
 		return videos.findByDurationLessThan(duration);
	}
	
}
