package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AddTeacherCoursesDTO implements Serializable {

	private static final long serialVersionUID = -3296636555702245652L;

	private Teacher teacher;
	private List<Course> courses;
	
	public AddTeacherCoursesDTO() {
	}

	public AddTeacherCoursesDTO(Teacher teacher, List<Course> courses) {
		super();
		this.teacher = teacher;
		this.courses = courses;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "AddTeacherCoursesDTO [teacher=" + teacher + ", courses=" + courses + "]";
	}
	
}
