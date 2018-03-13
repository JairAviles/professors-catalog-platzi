package mx.jairaviles.professorscatalog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mx.jairaviles.professorscatalog.dao.CourseDaoImpl;
import mx.jairaviles.professorscatalog.dao.IGenericDao;
import mx.jairaviles.professorscatalog.dao.SocialMediaDaoImpl;
import mx.jairaviles.professorscatalog.dao.TeacherDaoImpl;
import mx.jairaviles.professorscatalog.model.Course;
import mx.jairaviles.professorscatalog.model.SocialMedia;
import mx.jairaviles.professorscatalog.model.Teacher;

@Configuration
public class InterfaceConfiguration {

	@Bean
	public IGenericDao<Course> courseDaoImpl() {
		return new CourseDaoImpl();
	}
	
	@Bean
	public IGenericDao<SocialMedia> socialMediaDaoImpl() {
		return new SocialMediaDaoImpl();
	}
	
	@Bean
	public IGenericDao<Teacher> teacherDaoImpl() {
		return new TeacherDaoImpl();
	}
}
