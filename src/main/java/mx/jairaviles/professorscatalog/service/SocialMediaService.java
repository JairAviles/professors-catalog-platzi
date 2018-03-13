package mx.jairaviles.professorscatalog.service;

import java.util.List;

import mx.jairaviles.professorscatalog.model.SocialMedia;
import mx.jairaviles.professorscatalog.model.TeacherSocialMedia;

public interface SocialMediaService {

	void save(final SocialMedia socialMedia);
	
	List<SocialMedia> findAll();
	
	void deleteById(final Long id);
	
	void update(final SocialMedia socialMedia);
	
	SocialMedia findById(final Long id);
	
	SocialMedia findByName(String name);
	
	TeacherSocialMedia findByIdAndNickname(Long idSocialMedia, String nickname);
	
	TeacherSocialMedia findByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia);
	
}
