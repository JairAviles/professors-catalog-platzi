package mx.jairaviles.professorscatalog.dao;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao<T extends Serializable>  {
	
	void save(final T entity);
	
	List<T> findAll();
	
	void deleteById(final Long id);
	
	void update(final T entity);
	
	T findById(final Long id);
	
	T findByName(String name);

}
