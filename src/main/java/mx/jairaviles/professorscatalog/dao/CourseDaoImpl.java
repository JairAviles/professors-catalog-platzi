package mx.jairaviles.professorscatalog.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import mx.jairaviles.professorscatalog.model.Course;

@Repository
@Transactional
public class CourseDaoImpl extends AbstractSession implements IGenericDao<Course>, ICourseDao {

	public CourseDaoImpl() {
		super();
	}

	@Override
	public void save(Course course) {
		getSession().persist(course);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findAll() {
		return getSession().createQuery("from Course").list();
	}

	@Override
	public void deleteById(Long id) {
		Course course = findById(id);
		
		if (course != null) {
			getSession().delete(course);
		}
	}

	@Override
	public void update(Course course) {
		getSession().update(course);
	}

	@Override
	public Course findById(Long id) {
		return getSession().get(Course.class, id);
	}

	@Override
	public Course findByName(String name) {
		return (Course) getSession().createQuery("from Course where name = :name")
				.setParameter("name", name).uniqueResult();	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		return getSession().createQuery("from Course c join c.teacher t where t.idTeacher = :idTeacher")
				.setParameter("idTeacher", idTeacher).list();
	}

}
