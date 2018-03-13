package mx.jairaviles.professorscatalog.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="teacher_social_media")
public class TeacherSocialMedia implements Serializable {

	private static final long serialVersionUID = -5501230147500950715L;
	
	@Id
	@Column(name="id_teacher_social_media")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idTeacherSocialMedia;
	
	@Column(name="nickname")
	private String nickname;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_teacher")
	@JsonIgnore
	private Teacher teacher;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_social_media")
	private SocialMedia socialMedia;
	
	public TeacherSocialMedia() {
		super();
	}
	
	public TeacherSocialMedia(Teacher tacher, SocialMedia socialMedia, String nickname) {
		super();
		this.teacher = tacher;
		this.socialMedia = socialMedia;
		this.nickname = nickname;
	}

	public Long getIdTeacherSocialMedia() {
		return idTeacherSocialMedia;
	}
	public void setIdTeacherSocialMedia(Long idTeacherSocialMedia) {
		this.idTeacherSocialMedia = idTeacherSocialMedia;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public SocialMedia getSocialMedia() {
		return socialMedia;
	}
	public void setSocialMedia(SocialMedia socialMedia) {
		this.socialMedia = socialMedia;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
