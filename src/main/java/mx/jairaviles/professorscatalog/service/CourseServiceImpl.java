package mx.jairaviles.professorscatalog.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mx.jairaviles.professorscatalog.dao.ICourseDao;
import mx.jairaviles.professorscatalog.dao.IGenericDao;
import mx.jairaviles.professorscatalog.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	@Qualifier("courseDaoImpl")
	private IGenericDao<Course> _genericDao;
	
	@Autowired
	private ICourseDao _courseDao;

	@Override
	public void save(Course course) {
		_genericDao.save(course);
	}

	@Override
	public List<Course> findAll() {
		return _genericDao.findAll();
	}

	@Override
	public void deleteById(Long id) {
		_genericDao.deleteById(id);
	}

	@Override
	public void update(Course course) {
		_genericDao.update(course);
	}

	@Override
	public Course findById(Long id) {
		return _genericDao.findById(id);
	}

	@Override
	public Course findByName(String name) {
		return _genericDao.findByName(name);
	}

	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		return _courseDao.findByIdTeacher(idTeacher);
	}

}
