package com.app.commline;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "personnel")
public class Personnel {

	@XmlElement(name = "lname")
	private String lname;
	@XmlElement(name = "fname")
	private String fname;
	@XmlAttribute(name = "id")
	public int id;

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Personnel() {
		this(0, " ", " ");
	}

	public Personnel(int initId, String fName, String lName) {
		fname = fName;
		id = initId;
		lname = lName;
	}

}
