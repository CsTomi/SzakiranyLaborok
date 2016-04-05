package jpa;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Mozdony {

	@Id
	private int id;    
    private int futottkm;
    @OneToOne
    private Tipus tipus;

    public Mozdony() {
    }
     
    public Mozdony(int id, Tipus t, int km) {
    	this.id = id;
    	this.futottkm = km;
    	this.tipus = t;
    }
    
    public int getFutottkm() {
        return futottkm;
    }

    public void setFutottkm(int futottkm) {
        this.futottkm = futottkm;
    }

	public int getId() {
    	return id;
	}
	
	public void setId(int id) {
    	 this.id = id;
	}
	
	public String toString() {
		return new String(id + " " +futottkm);
	}

}
