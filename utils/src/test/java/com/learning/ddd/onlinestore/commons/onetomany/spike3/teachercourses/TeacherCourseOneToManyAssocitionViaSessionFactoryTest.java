package com.learning.ddd.onlinestore.commons.onetomany.spike3.teachercourses;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class TeacherCourseOneToManyAssocitionViaSessionFactoryTest {

	private static Integer TEACHER_ID_IN_DB = 0;
	
	private static SessionFactory sessionFactory = null;

	public static void main(String[] args) {
		
		Session session = null;
		
		try {
			
			sessionFactory = new Configuration()
				.configure(TeacherCourseOneToManyAssocitionViaSessionFactoryTest.class.getResource("hibernate.cfg.xml"))
				//.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Teacher.class)
				.addAnnotatedClass(Course.class)
				.buildSessionFactory();
			System.out.println("\n~~~~~>  SessionFactory is built");
			
			session = sessionFactory.getCurrentSession();
			System.out.println("\n~~~~~>  Current Session is retrieved");
			
			session = addOneTeacherAndFourCourses();
			System.out.println();
			
			session = getTeacherAndAssociatedCourses();
			System.out.println();
			
			session = deleteCourse();
			System.out.println();
			
			session = getTeacherAndAssociatedCourses();
			System.out.println();
			
			session = deleteTeacher();
			System.out.println();
			
			session = getTeacherAndAssociatedCourses();
			System.out.println();
			
			
		} catch (Exception e) {
			
			System.err.println("~~~~~> Error in main block...");
			e.printStackTrace();
			
		} finally {
			
			if (session != null) {
				session.close();
				System.out.println("\n~~~~~>  Session is closed");
			}
			
			if (sessionFactory != null) {
				sessionFactory.close();
				System.out.println("\n~~~~~>  SessionFactory is closed");
			}
			
		}
		
		
	}

	private static Session addOneTeacherAndFourCourses() {
		
		Session session = sessionFactory.getCurrentSession();
		
		//---------- within a transaction : start -----------
		
		Transaction transaction = session.beginTransaction();
		System.out.println("\n~~~~~> addOneTeacherAndFourCourses(): Transaction has begun");
		
		try {
			
			Teacher t1 = new Teacher("T1");
			Course c1 = new Course("C1", t1);
			Course c2 = new Course("C2", t1);
			Course c3 = new Course("C3", t1);
			Course c4 = new Course("C4", t1);
			t1.addCourse(c1);
			t1.addCourse(c2);
			t1.addCourse(c3);
			t1.addCourse(c4);
			
			TEACHER_ID_IN_DB = (Integer) session.save(t1);
			System.out.println("\n~~~~~>  Teacher(T1) and Courses (C1/2/3/4) are added");
			
			transaction.commit();
			System.out.println("\n~~~~~> addOneTeacherAndFourCourses(): Transaction is committed");
			
		} catch (Exception e) {
			
			System.err.println("~~~~~> addOneTeacherAndFourCourses(): Error while adding Teacher and Courses");
			e.printStackTrace();
			
			transaction.rollback();
			System.out.println("\n~~~~~> addOneTeacherAndFourCourses(): Transaction is rolled back");
		}
		
		//---------- within a transaction : end -----------
		
		return session;
	}
	
	private static Session getTeacherAndAssociatedCourses() {
		
		Session session = sessionFactory.getCurrentSession();
		
		//---------- within a transaction : start -----------
		
		Transaction transaction = session.beginTransaction();
		System.out.println("\n~~~~~>  Transaction has begun");
		
		try {
			
			Teacher t = session.get(Teacher.class, TEACHER_ID_IN_DB);
			System.out.println("\n~~~~~>  Teacher retrieved from DB: " + t);
			
			if (t != null) {
				
				for (Course c : t.getCourses()) {
					System.out.println("\n~~~~~>    Course for Teacher: " + c);
				}
				
			} else {
				
				System.err.println("~~~~~> Teacher/Course not found in DB!!!");
				
			}
			
			transaction.commit();
			System.out.println("\n~~~~~>  Transaction is committed");
			
		} catch (Exception e) {
			
			System.err.println("~~~~~> Error while retrieving Teacher and Courses");
			e.printStackTrace();
			
			transaction.rollback();
			System.out.println("\n~~~~~>  Transaction is rolled back");
		}
		
		//---------- within a transaction : end -----------
		
		return session;
	}
	
	private static Session deleteCourse() {
		
		Session session = sessionFactory.getCurrentSession();
		
		//---------- within a transaction : start -----------
		
		Transaction transaction = session.beginTransaction();
		System.out.println("\n~~~~~>  Transaction has begun");
		
		try {
			
			Teacher t = session.get(Teacher.class, TEACHER_ID_IN_DB);
			
			t.getCourses().remove(3); // remove 4th course C4
			
			session.save(t);
			
			System.out.println("\n~~~~~>  Course C4 deleted from DB");
			
			transaction.commit();
			System.out.println("\n~~~~~>  Transaction is committed");
			
		} catch (Exception e) {
			
			System.err.println("~~~~~> Error while deleting a Course");
			e.printStackTrace();
			
			transaction.rollback();
			System.out.println("\n~~~~~>  Transaction is rolled back");
		}
		
		//---------- within a transaction : end -----------
		
		return session;
	}
	
	private static Session deleteTeacher() {
		
		Session session = sessionFactory.getCurrentSession();
		
		//---------- within a transaction : start -----------
		
		Transaction transaction = session.beginTransaction();
		System.out.println("\n~~~~~>  Transaction has begun");
		
		try {
			
			Teacher t = session.get(Teacher.class, TEACHER_ID_IN_DB);
			
			session.delete(t);
			System.out.println("\n~~~~~>  Teacher and Courses deleted from DB");
			
			transaction.commit();
			System.out.println("\n~~~~~>  Transaction is committed");
			
		} catch (Exception e) {
			
			System.err.println("~~~~~> Error while deleting a Course");
			e.printStackTrace();
			
			transaction.rollback();
			System.out.println("\n~~~~~>  Transaction is rolled back");
		}
		
		//---------- within a transaction : end -----------
		
		return session;
	}
	
}
