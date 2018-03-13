package mx.jairaviles.professorscatalog.dao;

import java.util.List;

import mx.jairaviles.professorscatalog.model.Course;

public interface ICourseDao {
	
	List<Course> findByIdTeacher(Long idTeacher);

}
