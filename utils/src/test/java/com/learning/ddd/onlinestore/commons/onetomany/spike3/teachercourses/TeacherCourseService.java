package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherCourseService {

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Transactional
	public Teacher addTeacherAndCourses(AddTeacherCoursesDTO dto) {
		
		// set bi-directional relationship if already not set
		
		Teacher teacher = dto.getTeacher();
		List<Course> courses = dto.getCourses();
		
		teacher.setCourses(courses);
		for (Course course : courses) {
			course.setTeacher(teacher);
		}
		
		// persist data in DB
		Teacher t = teacherRepository.save(dto.getTeacher());
		
//		System.out.println(
//			"\n---------------------------------------"
//			+ "\n----[ All Courses Length = " + courseRepository.allCoursesLength() + " ]----"
//			+ "\n---------------------------------------"
//		);
		
		return t;
	}

	public Teacher getTeacherAndCourses(Integer teacherId) {
		Optional<Teacher> teacherInDB = teacherRepository.findById(teacherId);
		return teacherInDB.isPresent() ? teacherInDB.get() : null;
	}

	@Transactional
	public void deleteCourse(Integer teacherId, Integer courseId) {
		courseRepository.deleteById(courseId);
	}

	@Transactional
	public void deleteTeacherAndCourses(Integer teacherId) {
		teacherRepository.deleteById(teacherId);
	}

}
