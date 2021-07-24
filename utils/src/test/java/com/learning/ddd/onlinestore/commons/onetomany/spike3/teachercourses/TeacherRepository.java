package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

}
