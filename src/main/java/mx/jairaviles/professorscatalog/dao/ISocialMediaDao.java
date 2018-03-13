package mx.jairaviles.professorscatalog.dao;

import mx.jairaviles.professorscatalog.model.TeacherSocialMedia;

public interface ISocialMediaDao {
	
	TeacherSocialMedia findByIdAndNickname(Long idSocialMedia, String nickname);
	
	TeacherSocialMedia findByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia);

}
