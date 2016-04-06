package jpa;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Mozdony {

	@Id
	private int id;    
    private int futottkm;
    private Tipus tipus;
    private Vonat v;

    public Mozdony() {
    }
     
    public Mozdony(int id, Tipus t, int km/*, Vonat _v*/) {
    	this.id = id;
    	this.futottkm = km;
    	this.tipus = t;
    	//this.setV(_v);
    }
    
    public int getFutottkm() {
        return futottkm;
    }
    
    @ManyToOne
    public Tipus getTipus() {
    	return tipus;
    }
    
    public void setTipus(Tipus t) {
    	this.tipus = t;
    }

    public void setFutottkm(int futottkm) {
        this.futottkm = futottkm;
    }

    @ManyToOne
	public int getId() {
    	return id;
	}
	
	public void setId(int id) {
    	 this.id = id;
	}
	
	public String toString() {
		return new String(id + " " + tipus.getAzonosito() + " " +futottkm);
	}
/*
	public Vonat getV() {
		return v;
	}

	public void setV(Vonat v) {
		this.v = v;
	}
	
	@ManyToOne
	//@JoinColumn(name="V_ID")
	public int getVID() {
		return v.getId();
	}
*/
}
