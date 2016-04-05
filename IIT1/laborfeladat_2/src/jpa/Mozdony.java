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
   
    @ManyToOne
    @JoinColumn(name="TIPUS_AZONOSITO")
    public String getTipusAzonosito() {
    	return tipus.getAzonosito();
    }
    
    public void setTipusAzonosito(String azon) {
    	this.tipus.setAzonosito(azon);
    }
    
    public Tipus getTipus() {
    	return tipus;
    }
    
    public void setTipus(Tipus t) {
    	this.tipus = t;
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
		return new String(id + " " + tipus.getAzonosito() + " " +futottkm);
	}

}
