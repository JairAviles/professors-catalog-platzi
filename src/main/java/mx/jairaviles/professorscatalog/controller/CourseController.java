package mx.jairaviles.professorscatalog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import mx.jairaviles.professorscatalog.model.Course;
import mx.jairaviles.professorscatalog.service.CourseService;
import mx.jairaviles.professorscatalog.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CourseController {
	
	@Autowired
	CourseService _courseService;
	
	//GET
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name, @RequestParam(value="id_teacher", required=false) Long idTeacher) {
		List<Course> courses = new ArrayList<>();
		
		if (name != null) {
			Course course = _courseService.findByName(name);
			if (course == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND); 
			}
			courses.add(course);
		}
		
		if (idTeacher != null) {
			courses = _courseService.findByIdTeacher(idTeacher);
			if (courses == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND); 
			}
		}
		if (name == null && idTeacher == null) {
			courses = _courseService.findAll();
			if (courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT); 
			}
		}
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
	}
	
	//GET
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/courses/{id}", method = RequestMethod.GET, headers = "Accept=application/json" )
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long idCourse) {
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); 
		}
		Course course = _courseService.findById(idCourse);
		if (course == null) {
			return new ResponseEntity(new CustomErrorType("idCourse is required."), HttpStatus.CONFLICT); 
		}
		
		return new ResponseEntity<Course>(course, HttpStatus.OK);
		
	}
	
	//POST
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/courses", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder) {
		if (course.getName() == null || course.getName().equals(null) || course.getName().isEmpty() ) {
			return new ResponseEntity(new CustomErrorType("Course  name is required"), HttpStatus.CONFLICT);
		}
		
		if (_courseService.findByName(course.getName()) != null) {
			return new ResponseEntity(new CustomErrorType("Course already exists."), HttpStatus.CONFLICT); 
		}
		
		_courseService.save(course);
		Course courseTmp = _courseService.findByName(course.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder.path("/v1/courses/{id}")
				.buildAndExpand(courseTmp.getIdCourse())
				.toUri());
		
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//UPDATE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/courses/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateCourse(@PathVariable("id") Long idCourse, @RequestBody Course course) {
		
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("idCourse is required."), HttpStatus.CONFLICT);
		}
		
		Course currentCourse = _courseService.findById(idCourse);
		
		if (currentCourse == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getProject());
		currentCourse.setThemes(course.getThemes());
		
		_courseService.update(currentCourse);
		
		return new ResponseEntity<Course>(currentCourse, HttpStatus.OK); 
	}
	
	//DELETE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/courses/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCourse(@PathVariable("id") Long idCourse) {
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("idCourse is required."), HttpStatus.CONFLICT);
		}
		
		Course course = _courseService.findById(idCourse);
		
		if (course == null) {
			return new ResponseEntity(new CustomErrorType("Course not found."), HttpStatus.NO_CONTENT);
		}
		
		_courseService.deleteById(idCourse);
		return new ResponseEntity<Course>(HttpStatus.OK); 
	}

}
