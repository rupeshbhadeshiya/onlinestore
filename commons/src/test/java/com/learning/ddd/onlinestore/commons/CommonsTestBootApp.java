package com.learning.ddd.onlinestore.commons;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart.CartCart;
import com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart.CartCartService;
import com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart.PullCartCartDTO;
import com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses.AddTeacherCoursesDTO;
import com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses.Teacher;
import com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses.TeacherCourseService;

@SpringBootApplication
@ComponentScan( {"com.learning.ddd.onlinestore"} )
@RestController
@RequestMapping("/commons/spike")
public class CommonsTestBootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CommonsTestBootApp.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CommonsTestBootApp.class, args);
	}

	
	// ------------------- CartCart spike APIs -------------------
	
	@Autowired
	private CartCartService cartService;
	
	@PostMapping("/carts")
	public CartCart pullCartAndAddItems(@RequestBody PullCartCartDTO pullCartDTO) {
		
		return cartService.pullCartAndAddItems(pullCartDTO);
	}
	
	@GetMapping("/carts")
	public List<CartCart> getAllCarts() {
		
		return cartService.getAllCarts();
	}
	
	@DeleteMapping("/carts/{cartId}")
	public void deleteCart(@PathVariable Integer cartId) {
		
		cartService.deleteCart(cartId);
	}
	
	// ------------------- Teacher-Course spike APIs -------------------
	
	@Autowired
	private TeacherCourseService teacherCourseService;
	
	@PostMapping("/teachers")
	public Teacher addTeacherAndCourses(@RequestBody AddTeacherCoursesDTO dto) {
		
		return teacherCourseService.addTeacherAndCourses(dto);
	}
	
	@GetMapping("/teachers/{teacherId}")
	public Teacher getTeacherAndCourses(@PathVariable Integer teacherId) {

		return teacherCourseService.getTeacherAndCourses(teacherId);
	}
	
	@DeleteMapping("/teachers/{teacherId}/courses/{courseId}")
	public void deleteCourse(@PathVariable Integer teacherId, 
			@PathVariable Integer courseId) {

		teacherCourseService.deleteCourse(teacherId, courseId);
	}
	
	@DeleteMapping("/teachers/{teacherId}")
	public void deleteTeacherAndCourses(@PathVariable Integer teacherId) {

		teacherCourseService.deleteTeacherAndCourses(teacherId);
	}
	
}
