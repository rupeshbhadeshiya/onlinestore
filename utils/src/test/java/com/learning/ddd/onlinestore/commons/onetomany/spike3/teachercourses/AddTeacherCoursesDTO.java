package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AddTeacherCoursesDTO {

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
