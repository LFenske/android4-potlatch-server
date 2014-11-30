/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package localhost.potlatchserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import localhost.potlatchserver.repository.Media;

/**
 * This class provides a simple implementation to store media binary
 * data on the file system in a "media" folder. The class provides
 * methods for saving media and retrieving their binary data.
 * 
 * @author jules
 *
 */
public class MediaFileManager {

	/**
	 * This static factory method creates and returns a 
	 * MediaFileManager object to the caller. Feel free to customize
	 * this method to take parameters, etc. if you want.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static MediaFileManager get() throws IOException {
		return new MediaFileManager();
	}
	
	private Path targetDir_ = Paths.get("media");
	
	// The MediaFileManager.get() method should be used
	// to obtain an instance
	private MediaFileManager() throws IOException{
		if(!Files.exists(targetDir_)){
			Files.createDirectories(targetDir_);
		}
	}
	
	// Private helper method for resolving media file paths
	private Path getMediaPath(Media m){
		assert(m != null);
		
		return targetDir_.resolve("media"+m.getId()+".jpg");
	}
	
	/**
	 * This method returns true if the specified Media has binary
	 * data stored on the file system.
	 * 
	 * @param m
	 * @return
	 */
	public boolean hasMediaData(Media m){
		Path source = getMediaPath(m);
		return Files.exists(source);
	}
	
	/**
	 * This method copies the binary data for the given media to
	 * the provided output stream. The caller is responsible for
	 * ensuring that the specified Media has binary data associated
	 * with it. If not, this method will throw a FileNotFoundException.
	 * 
	 * @param m 
	 * @param out
	 * @throws IOException 
	 */
	public void copyMediaData(Media m, OutputStream out) throws IOException {
		Path source = getMediaPath(m);
		if(!Files.exists(source)){
			throw new FileNotFoundException("Unable to find the referenced media file for mediaId:"+m.getId());
		}
		Files.copy(source, out);
	}
	
	/**
	 * This method reads all of the data in the provided InputStream and stores
	 * it on the file system. The data is associated with the Media object that
	 * is provided by the caller.
	 * 
	 * @param m
	 * @param mediaData
	 * @throws IOException
	 */
	public void saveMediaData(Media m, InputStream mediaData) throws IOException{
		assert(mediaData != null);
		
		Path target = getMediaPath(m);
		Files.copy(mediaData, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
}
