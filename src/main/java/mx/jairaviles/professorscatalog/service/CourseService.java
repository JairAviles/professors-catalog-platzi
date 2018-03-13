package mx.jairaviles.professorscatalog.service;

import java.util.List;

import mx.jairaviles.professorscatalog.model.Course;

public interface CourseService {
	
	void save(final Course course);
	
	List<Course> findAll();
	
	void deleteById(final Long id);
	
	void update(final Course course);
	
	Course findById(final Long id);
	
	Course findByName(String name);
	
	List<Course> findByIdTeacher(Long idTeacher);

}
