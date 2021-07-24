package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.learning.ddd.onlinestore.commons.util.HttpUtil;

@TestMethodOrder(OrderAnnotation.class)
class TeacherCourseAPITest {

	private static final String TEACHER_NAME = "T1";
	private static final String COURSE1_TITLE = "C1";
	private static final String COURSE2_TITLE = "C2";
	private static final String COURSE3_TITLE = "C3";
	private static final String COURSE4_TITLE = "C4";

	private static int TEACHER_ID;
	private static int COURSE1_ID;

	@Test
	@Order(1)
	void addTeacherAndCourses() throws IOException {
		
		Teacher t1 = new Teacher(TEACHER_NAME);
		Course c1 = new Course(COURSE1_TITLE, t1, 5);
		Course c2 = new Course(COURSE2_TITLE, t1, 10);
		Course c3 = new Course(COURSE3_TITLE, t1, 2);
		Course c4 = new Course(COURSE4_TITLE, t1, 8);
		t1.addCourse(c1);
		t1.addCourse(c2);
		t1.addCourse(c3);
		t1.addCourse(c4);

		AddTeacherCoursesDTO dto = new AddTeacherCoursesDTO();
		dto.setTeacher(t1);
		dto.setCourses(Arrays.asList(new Course[] {c1,c2,c3,c4}));
		
		Teacher t = (Teacher) HttpUtil.post(
			"http://localhost:9009/commons/spike/teachers", dto, Teacher.class);
		TEACHER_ID = t.getId();
		
		assertNotNull(t);
		assertNotNull(t.getCourses());
		assertEquals(4, t.getCourses().size());
		
		assertEquals(TEACHER_NAME, t.getName());
		
		assertEquals(COURSE1_TITLE, t.getCourses().get(0).getTitle());
		assertEquals(COURSE2_TITLE, t.getCourses().get(1).getTitle());
		assertEquals(COURSE3_TITLE, t.getCourses().get(2).getTitle());
		assertEquals(COURSE4_TITLE, t.getCourses().get(3).getTitle());
	}
	
	@Test
	@Order(2)
	void getTeacherAndCourses() throws IOException {
		
		Teacher t = (Teacher) HttpUtil.get(
				"http://localhost:9009/commons/spike/teachers/"+TEACHER_ID, Teacher.class);
		
		assertNotNull(t);
		assertNotNull(t.getCourses());
		assertEquals(4, t.getCourses().size());
		
		assertEquals(TEACHER_ID, t.getId());
		assertEquals(TEACHER_NAME, t.getName());
		
		assertEquals(COURSE1_TITLE, t.getCourses().get(0).getTitle());
		assertEquals(COURSE2_TITLE, t.getCourses().get(1).getTitle());
		assertEquals(COURSE3_TITLE, t.getCourses().get(2).getTitle());
		assertEquals(COURSE4_TITLE, t.getCourses().get(3).getTitle());
		
		COURSE1_ID = t.getCourses().get(0).getId();
		
		System.out.println("getTeacherAndCourses(): data = " + t);
	}
	
	@Test
	@Order(3)
	void deleteOneCourse() throws IOException {
		
		HttpUtil.delete("http://localhost:9009/commons/spike/teachers/"+TEACHER_ID+
				"/courses/"+COURSE1_ID);
		
		Teacher t = (Teacher) HttpUtil.get(
				"http://localhost:9009/commons/spike/teachers/"+TEACHER_ID, Teacher.class);
		
		assertNotNull(t);
		assertNotNull(t.getCourses());
		assertEquals(3, t.getCourses().size());
		
		for (Course course : t.getCourses()) {
			assertNotEquals(COURSE1_ID, course.getId());
			assertNotEquals(COURSE1_TITLE, course.getTitle());
		}
		
		System.out.println("deleteOneCourse(): data = " + t);
	}
	
	@Test
	@Order(4)
	void deleteTeacherAndCourses() throws IOException {
		
		HttpUtil.delete("http://localhost:9009/commons/spike/teachers/"+TEACHER_ID);
		
		Teacher t = (Teacher) HttpUtil.get(
				"http://localhost:9009/commons/spike/teachers/"+TEACHER_ID, Teacher.class);
		
		assertNull(t);
	}

}
