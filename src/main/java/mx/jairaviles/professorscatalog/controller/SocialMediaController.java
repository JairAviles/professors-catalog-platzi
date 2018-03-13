package mx.jairaviles.professorscatalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import mx.jairaviles.professorscatalog.model.SocialMedia;
import mx.jairaviles.professorscatalog.model.Teacher;
import mx.jairaviles.professorscatalog.service.SocialMediaService;
import mx.jairaviles.professorscatalog.util.CustomErrorType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/v1")
public class SocialMediaController {
	
	public static final String SOCIAL_MEDIAS_UPLOADED_FOLDER = "images/socialMedias/";
	
	@Autowired
	SocialMediaService _socialMediaService;
	
	//GET
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/socialMedias", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedias(@RequestParam(value="name", required=false) String name) {
		
		List<SocialMedia> socialMedias = new ArrayList<>();
		
		if (name == null) {
			socialMedias = _socialMediaService.findAll();
			
			if (socialMedias.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT); 
			}
			
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		} else {
			SocialMedia socialMedia = _socialMediaService.findByName(name);
			if (socialMedia == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND); 
			}
			socialMedias.add(socialMedia);
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
			
		}
	}
	
	//GET
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.GET, headers = "Accept=application/json" )
	public ResponseEntity<SocialMedia> getSocialMediaById(@PathVariable("id") Long idSocialMedia) {
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); 
		}
		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		if (socialMedia == null) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT); 
		}
		
		return new ResponseEntity<SocialMedia>(socialMedia, HttpStatus.OK);
		
	}
	
	//POST
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/socialMedias", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createSocialMedia(@RequestBody SocialMedia socialMedia, UriComponentsBuilder uriComponentsBuilder) {
		if (socialMedia.getName() == null || socialMedia.getName().equals(null) || socialMedia.getName().isEmpty() ) {
			return new ResponseEntity(new CustomErrorType("SocialMedia  name is required"), HttpStatus.CONFLICT);
		}
		
		if (_socialMediaService.findByName(socialMedia.getName()) != null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); 
		}
		
		_socialMediaService.save(socialMedia);
		SocialMedia socialMediaTmp = _socialMediaService.findByName(socialMedia.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder.path("/v1/socialMedias/{id}")
				.buildAndExpand(socialMediaTmp.getIdSocialMedia())
				.toUri());
		
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//UPDATE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateSocialMedia(@PathVariable("id") Long idSocialMedia, @RequestBody SocialMedia socialMedia) {
		
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}
		
		SocialMedia currentSocialMedia = _socialMediaService.findById(idSocialMedia);
		
		if (currentSocialMedia == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		currentSocialMedia.setName(socialMedia.getName());
		currentSocialMedia.setIcon(socialMedia.getIcon());
		
		_socialMediaService.update(currentSocialMedia);
		
		return new ResponseEntity<SocialMedia>(currentSocialMedia, HttpStatus.OK); 
	}
	
	//DELETE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSocialMedia(@PathVariable("id") Long idSocialMedia) {
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}
		
		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		
		if (socialMedia == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		_socialMediaService.deleteById(idSocialMedia);
		
		return new ResponseEntity<SocialMedia>(HttpStatus.OK); 
	}
	
	//CREATE SOCIALMEDIA IMAGE
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(value="/socialMedias/image", method = RequestMethod.POST, headers=("content-type=multipart/form-data"))
		public ResponseEntity<byte[]> uploadSocialMediaImage(@RequestParam("id_social_media") Long idSocialMedia, 
				@RequestParam("file") MultipartFile multipartFile, 
				UriComponentsBuilder uriComponentsBuilder) {
			
			if (idSocialMedia == null) {
				return new ResponseEntity(new CustomErrorType("Please, specify id social media"), HttpStatus.NO_CONTENT);
			}
			
			if (multipartFile.isEmpty()) {
				return new ResponseEntity(new CustomErrorType("Please, select a file to upload"), HttpStatus.NO_CONTENT);
			}
			
			SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
			
			if (socialMedia == null) {
				return new ResponseEntity(new CustomErrorType("Social Media not found"), HttpStatus.NOT_FOUND);
			}
			
			if (!socialMedia.getIcon().isEmpty() || socialMedia.getIcon() != null) {
				String fileName = socialMedia.getIcon();
				Path path = Paths.get(fileName);
				File f = path.toFile();
				
				if (f.exists()) {
					f.delete();
				}
			}
			
			try {
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				String dateName = dateFormat.format(date);
				String fileName = String.valueOf(idSocialMedia) + "-pictureSocialMedia-" + dateName + "." + multipartFile.getContentType().split("/")[1];
				socialMedia.setIcon(SOCIAL_MEDIAS_UPLOADED_FOLDER + fileName);
				
				byte[] bytes = multipartFile.getBytes();
				Path path = Paths.get(SOCIAL_MEDIAS_UPLOADED_FOLDER + fileName);
				Files.write(path, bytes);
				
				_socialMediaService.update(socialMedia);
				
				return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
			} catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity(new CustomErrorType("Error while uploading: " + multipartFile.getOriginalFilename()), HttpStatus.NOT_FOUND);
			}
		}
	
}
