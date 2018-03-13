package mx.jairaviles.professorscatalog.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import mx.jairaviles.professorscatalog.model.SocialMedia;
import mx.jairaviles.professorscatalog.model.TeacherSocialMedia;

@Repository
@Transactional
public class SocialMediaDaoImpl extends AbstractSession implements IGenericDao<SocialMedia>, ISocialMediaDao {

	public SocialMediaDaoImpl() {
		super();
	}

	@Override
	public void save(SocialMedia socialMedia) {
		getSession().persist(socialMedia);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SocialMedia> findAll() {
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public void deleteById(Long id) {
		SocialMedia socialMedia = findById(id);
		
		if (socialMedia != null) {
			getSession().delete(socialMedia);
		}
	}

	@Override
	public void update(SocialMedia socialMedia) {
		getSession().update(socialMedia);
	}

	@Override
	public SocialMedia findById(Long id) {
		return getSession().get(SocialMedia.class, id);
	}

	@Override
	public SocialMedia findByName(String name) {
		return (SocialMedia) getSession().createQuery("from SocialMedia where name = :name")
				.setParameter("name", name).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TeacherSocialMedia findByIdAndNickname(Long idSocialMedia, String nickname) {
		List<Object[]> objects = getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialMedia sm "
				+ "where sm.idSocialMedia = :idSocialMedia and tsm.nickname = :nickname")
				.setParameter("idSocialMedia", idSocialMedia)
				.setParameter("nickname", nickname).list();
		
		if (objects != null && objects.size() > 0) {
			for (Object[] objs : objects) {
				for (Object obj : objs) {
					if (obj instanceof TeacherSocialMedia) {
						return (TeacherSocialMedia) obj;
					}
				}
			}
		}
		
		return null;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public TeacherSocialMedia findByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia) {
		List<Object[]> objects = getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialMedia sm "
				+ "join tsm.teacher t where sm.idSocialMedia = :idSocialMedia "
				+ "and t.idTeacher = :idTeacher")
				.setParameter("idSocialMedia", idSocialMedia)
				.setParameter("idTeacher", idTeacher).list();
		
		if (objects != null && objects.size() > 0) {
			for (Object[] objs : objects) {
				for (Object obj : objs) {
					if (obj instanceof TeacherSocialMedia) {
						return (TeacherSocialMedia) obj;
					}
				}
			}
		}
		return null;
	}

}
