package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Integer> {

//	@Modifying
//	@Query("delete from Course c where c.teacherId = :teacherId")
//	void deleteCoursesOfOneTeacher(@Param("teacherId") Integer teacherId);

	@Query("SELECT SUM(length) FROM Course")
	Integer allCoursesLength();
	
}
