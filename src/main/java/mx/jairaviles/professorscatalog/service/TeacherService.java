package mx.jairaviles.professorscatalog.service;

import java.util.List;

import mx.jairaviles.professorscatalog.model.Teacher;

public interface TeacherService {

	void save(final Teacher teacher);
	
	List<Teacher> findAll();
	
	void deleteById(final Long id);
	
	void update(final Teacher teacher);
	
	Teacher findById(final Long id);
	
	Teacher findByName(String name);
	
}
