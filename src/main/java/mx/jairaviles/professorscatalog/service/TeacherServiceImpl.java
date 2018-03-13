package mx.jairaviles.professorscatalog.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mx.jairaviles.professorscatalog.dao.IGenericDao;
import mx.jairaviles.professorscatalog.model.Teacher;

@Service("teacherService")
@Transactional
public class TeacherServiceImpl implements TeacherService {
	
	@Autowired
	@Qualifier("teacherDaoImpl")
	private IGenericDao<Teacher> _genericDao;

	@Override
	public void save(Teacher teacher) {
		_genericDao.save(teacher);
	}

	@Override
	public List<Teacher> findAll() {
		return _genericDao.findAll();
	}

	@Override
	public void deleteById(Long id) {
		_genericDao.deleteById(id);		
	}

	@Override
	public void update(Teacher teacher) {
		_genericDao.update(teacher);		
	}

	@Override
	public Teacher findById(Long id) {
		return _genericDao.findById(id);
	}

	@Override
	public Teacher findByName(String name) {
		return _genericDao.findByName(name);
	}

}
