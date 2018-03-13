package mx.jairaviles.professorscatalog.dao;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import mx.jairaviles.professorscatalog.model.Teacher;
import mx.jairaviles.professorscatalog.model.TeacherSocialMedia;

@Repository
@Transactional
public class TeacherDaoImpl extends AbstractSession implements IGenericDao<Teacher>, ITeacherDao {
	
	public TeacherDaoImpl() {
		super();
	}

	@Override
	public void save(Teacher teacher) {
		getSession().persist(teacher);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> findAll() {
		return getSession().createQuery("from Teacher").list();
	}

	@Override
	public void deleteById(final Long id) {
		Teacher teacher = findById(id);
		
		if (teacher != null) {
			
			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedia().iterator();
			
			while (i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				i.remove();
				getSession().delete(teacherSocialMedia);
			}
			
			teacher.getTeacherSocialMedia().clear();
			
			getSession().delete(teacher);
		}
	}

	@Override
	public void update(Teacher teacher) {
		getSession().update(teacher);
	}

	@Override
	public Teacher findById(Long id) {
		return getSession().get(Teacher.class, id);
	}
	
	@Override
	public Teacher findByName(String name) {
		return (Teacher) getSession().createQuery("from Teacher where name = :name")
				.setParameter("name", name).uniqueResult();	
	}
}
