package com.learning.ddd.onlinestore.commons.onetomany.spike2.ab;

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
public class A {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	
	@OneToMany(mappedBy = "a", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<B> bList;
	
	public A() {
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
	
	public List<B> getbList() {
		return bList;
	}
	
	public void setbList(List<B> bList) {
		this.bList = bList;
	}
	
	public void addB(B b) {
		// set space
		if (this.bList == null) {
			this.bList = new ArrayList<>();
		}
		// A -> B: one side of bi-directional relationship
		if (this.bList.contains(b)) {
			this.bList.set( this.bList.indexOf(b), b );
		} else {
			this.bList.add(b);
		}
		// B -> A: other side of bi-directional relationship
		b.setA(this);
	}
	
	public void removeB(B b) {
		// A -> B: one side of bi-directional relationship
		this.bList.remove(b);
		// B -> A: other side of bi-directional relationship
		b.setA(null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((bList == null) ? 0 : bList.hashCode());
//		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		A other = (A) obj;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (bList == null) {
			if (other.bList != null)
				return false;
		} else if (!bList.equals(other.bList))
			return false;

		//		if (id != other.id)
//			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "A [id=" + id + ", name=" + name + ", bList=" + bList + "]";
	}
	
}
