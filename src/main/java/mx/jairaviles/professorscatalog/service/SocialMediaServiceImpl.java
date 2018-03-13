package mx.jairaviles.professorscatalog.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mx.jairaviles.professorscatalog.dao.IGenericDao;
import mx.jairaviles.professorscatalog.dao.ISocialMediaDao;
import mx.jairaviles.professorscatalog.model.SocialMedia;
import mx.jairaviles.professorscatalog.model.TeacherSocialMedia;

@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService {
	
	@Autowired
	@Qualifier("socialMediaDaoImpl")
	private IGenericDao<SocialMedia> _genericDao;
	
	@Autowired
	private ISocialMediaDao _socialMediaDao;

	@Override
	public void save(SocialMedia socialMedia) {
		_genericDao.save(socialMedia);
	}

	@Override
	public List<SocialMedia> findAll() {
		return _genericDao.findAll();
	}

	@Override
	public void deleteById(Long id) {
		_genericDao.deleteById(id);
	}

	@Override
	public void update(SocialMedia socialMedia) {
		_genericDao.update(socialMedia);
	}

	@Override
	public SocialMedia findById(Long id) {
		return _genericDao.findById(id);
	}

	@Override
	public SocialMedia findByName(String name) {
		return _genericDao.findByName(name);
	}

	@Override
	public TeacherSocialMedia findByIdAndNickname(Long idSocialMedia, String nickname) {
		return _socialMediaDao.findByIdAndNickname(idSocialMedia, nickname);
	}

	@Override
	public TeacherSocialMedia findByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia) {
		return _socialMediaDao.findByIdTeacherAndIdSocialMedia(idTeacher, idSocialMedia);
	}

}
