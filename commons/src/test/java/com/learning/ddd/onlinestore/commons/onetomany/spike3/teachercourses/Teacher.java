package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Teacher implements Serializable {
	
	private static final long serialVersionUID = -5241157174556408061L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY,
			orphanRemoval = true)
	private List<Course> courses;
	
	public Teacher() {
	}

	public Teacher(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public void addCourse(Course course) {
		if (this.courses == null) {
			this.courses = new ArrayList<Course>();
		}
		
		this.courses.add(course);
		course.setTeacher(this);
	}

	@Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", courses=" + courses + "]";
	}
	
}
