package mx.jairaviles.professorscatalog.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import mx.jairaviles.professorscatalog.model.TeacherSocialMedia;
import mx.jairaviles.professorscatalog.service.SocialMediaService;
import mx.jairaviles.professorscatalog.service.TeacherService;
import mx.jairaviles.professorscatalog.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class TeacherController {
	
	public static final String TEACHER_UPLOADED_FOLDER = "images/teachers/";
	
	@Autowired
	private TeacherService _teacherService;
	
	@Autowired
	private SocialMediaService _socialMediaService;
	
	//GET
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/teachers", method=RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required=false) String name)
	{
		List<Teacher> teachers = new ArrayList<>();
		if(name==null)
		{
			teachers = _teacherService.findAll();
			if(teachers.isEmpty())
			{
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}else {
			Teacher teacher = _teacherService.findByName(name);
			if(teacher == null)
			{
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}
	}
	
	//GET
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") Long idTeacher)
	{
		if(idTeacher == null || idTeacher <= 0)
		{
			return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if(teacher == null)
		{
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
	}
	
	//POST
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/teachers", method=RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder)
	{
		if(teacher.getName().equals(null) || teacher.getName().isEmpty())
		{
			return new ResponseEntity(new CustomErrorType("Teacher name is required"), HttpStatus.CONFLICT);
		}
		
		if(_teacherService.findByName(teacher.getName()) != null)
		{
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		_teacherService.save(teacher);
		Teacher teacherTmp = _teacherService.findByName(teacher.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder.path("/v1/teachers/{id}")
				.buildAndExpand(teacherTmp.getIdTeacher())
				.toUri()
				);
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//UPDATE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateTeacher(@PathVariable("id") Long idTeacher, @RequestBody Teacher teacher)
	{
		if(idTeacher == null || idTeacher<=0)
		{
			return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.CONFLICT);
		}
		
		Teacher currentTeacher = _teacherService.findById(idTeacher);
		if(currentTeacher == null)
		{
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		currentTeacher.setName(teacher.getName());
		currentTeacher.setAvatar(teacher.getAvatar());
		
		_teacherService.update(currentTeacher);
		return new ResponseEntity<Teacher>(currentTeacher, HttpStatus.OK);
	} */
	
	//DELETE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/teachers/{id}", method= RequestMethod.DELETE)
	public ResponseEntity<?> deleteTeacher(@PathVariable("id") Long idTeacher)
	{
		if(idTeacher==null || idTeacher<=0)
		{
			return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if(teacher == null)
		{
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		_teacherService.deleteById(idTeacher);
		return new ResponseEntity<Teacher>(HttpStatus.OK);
	}
	
	//CREATE TEACHER IMAGE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/teachers/image", method = RequestMethod.POST, headers=("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id_teacher") Long idTeacher, 
			@RequestParam("file") MultipartFile multipartFile, 
			UriComponentsBuilder uriComponentsBuilder) {
		
		if (idTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Please, specify id teacher"), HttpStatus.NO_CONTENT);
		}
		
		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please, select a file to upload"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher not found"), HttpStatus.NOT_FOUND);
		}
		
		if (!teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
			String fileName = teacher.getAvatar();
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
			String fileName = String.valueOf(idTeacher) + "-pictureTeacher-" + dateName + "." + multipartFile.getContentType().split("/")[1];
			teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			_teacherService.update(teacher);
			
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error while uploading: " + multipartFile.getOriginalFilename()), HttpStatus.NOT_FOUND);
		}
	}
	
	//GET IMAGE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/teachers/{id}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id") Long idTeacher) {
		if(idTeacher==null || idTeacher<=0)
		{
			return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with id: " + idTeacher + " not found."), HttpStatus.NOT_FOUND);
		}
		
		try {
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			
			if (f.exists()) {
				byte[] bytes = Files.readAllBytes(path);
				return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
			} else {
				return new ResponseEntity(new CustomErrorType("Image not found"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error while retrieving image"), HttpStatus.NOT_FOUND);
		}
	}
	
	//DELETE IMAGE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/teachers/{id}/images", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacherImage(@PathVariable("id") Long idTeacher) {
		if(idTeacher==null || idTeacher<=0)
		{
			return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with id: " + idTeacher + " not found."), HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getAvatar().isEmpty() || teacher.getAvatar() == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with id: " + idTeacher + " doesn't have avatar."), HttpStatus.NOT_FOUND);
		}
		
		String fileName = teacher.getAvatar();
		Path path = Paths.get(fileName);
		File f = path.toFile();
		
		if (f.exists()) {
			f.delete();
			teacher.setAvatar("");
			_teacherService.update(teacher);
			return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(new CustomErrorType("Image not found"), HttpStatus.NOT_FOUND);
		} 
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@RequestMapping(value="teachers/socialMedias", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> assignTeacherSocialMedia(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder) {
		if (teacher.getIdTeacher() == null) {
			return new ResponseEntity(new CustomErrorType("idTeacher, idSocialMedia and nickname are required"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacherSaved = _teacherService.findById(teacher.getIdTeacher());
		
		if (teacherSaved == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with id: " + teacher.getIdTeacher() + " not found."), HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getTeacherSocialMedia() == null || teacher.getTeacherSocialMedia().size() == 0) {
			return new ResponseEntity(new CustomErrorType("idTeacher, idSocialMedia and nickname are required"), HttpStatus.NO_CONTENT);
		} else {
			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedia().iterator();
			
			while (i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				
				if (teacherSocialMedia.getSocialMedia().getIdSocialMedia() == null || teacherSocialMedia.getNickname() == null) {
					return new ResponseEntity(new CustomErrorType("idTeacher, idSocialMedia and nickname are required"), HttpStatus.NO_CONTENT);
				} else {
					TeacherSocialMedia tsmAux = _socialMediaService.findByIdAndNickname(
							teacherSocialMedia.getSocialMedia().getIdSocialMedia(), 
							teacherSocialMedia.getNickname());
					
					if (tsmAux != null) {
						return new ResponseEntity(new CustomErrorType("The social media " 
							+ teacherSocialMedia.getSocialMedia().getIdSocialMedia() + " with nickname: "
							+ teacherSocialMedia.getNickname() 
							+ " already exists."), HttpStatus.NO_CONTENT);
					}
					
					SocialMedia socialMedia = _socialMediaService.findById(teacherSocialMedia.getSocialMedia().getIdSocialMedia());
					if (socialMedia == null) {
						return new ResponseEntity(new CustomErrorType("The social media with id " 
								+ teacherSocialMedia.getSocialMedia().getIdSocialMedia() + " not found."), HttpStatus.NOT_FOUND);
					}
					teacherSocialMedia.setSocialMedia(socialMedia);
					teacherSocialMedia.setTeacher(teacherSaved);
					
					if (tsmAux == null) {
						teacherSaved.getTeacherSocialMedia().add(teacherSocialMedia);
					} else {
						LinkedList<TeacherSocialMedia> teacherSocialMedias = new LinkedList<>();
						teacherSocialMedias.addAll(teacherSaved.getTeacherSocialMedia());
						
						for (int j = 0; j < teacherSocialMedias.size(); j++) {
							TeacherSocialMedia teacherSocialMediaTmp = teacherSocialMedias.get(j);
							
							if (teacherSocialMedia.getTeacher().getIdTeacher() == teacherSocialMediaTmp.getTeacher().getIdTeacher()
									&& teacherSocialMedia.getSocialMedia().getIdSocialMedia() == teacherSocialMediaTmp.getSocialMedia().getIdSocialMedia()) {
								teacherSocialMediaTmp.setNickname(teacherSocialMedia.getNickname());
								teacherSocialMedias.set(j, teacherSocialMediaTmp);
							} else {
								teacherSocialMedias.set(j, teacherSocialMediaTmp);
							}
						}
						
						teacherSaved.getTeacherSocialMedia().clear();
						teacherSaved.getTeacherSocialMedia().addAll(teacherSocialMedias);
						
					}
				}
			}
		}
		_teacherService.update(teacherSaved);
		return new ResponseEntity<Teacher>(teacherSaved, HttpStatus.OK);
	}
	
}
