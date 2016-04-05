package jpa;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Mozdony {

	@Id
	private int id;    
    private int futottkm;


     
    public Mozdony() {
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

}
