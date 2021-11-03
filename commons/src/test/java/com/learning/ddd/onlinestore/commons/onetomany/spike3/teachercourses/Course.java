package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Course implements Serializable {

	private static final long serialVersionUID = 1716365009569648827L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String title;
	
	private int length; 	//in hours
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private Teacher teacher;
	
	public Course() {
	}

	public Course(String courseTitle, Teacher teacher) {
		super();
		this.title = courseTitle;
		this.teacher = teacher;
	}

	public Course(String courseTitle, Teacher teacher, int courseLengthInHours) {
		super();
		this.title = courseTitle;
		this.teacher = teacher;
		this.length = courseLengthInHours;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", title=" + title + "]";
	}
	
}
