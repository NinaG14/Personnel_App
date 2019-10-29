package com.app.commline;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "persons")
@XmlAccessorType (XmlAccessType.FIELD)
 public class Persons {

//List to add multiple entries from xml file
    @XmlElement(name = "personnel")
    private List<Personnel> person_list = null;
 
    public List<Personnel> getPersons() {
        return person_list;
    }
 
    public void setPersons(List<Personnel> person_list) {
        this.person_list = person_list;
    }
    
  
   
    
}