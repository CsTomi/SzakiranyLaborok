package jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Vonat {

	@Temporal(TemporalType.DATE)
    private  Date datum;
    private int keses;
    private Vonatszam szam;
    private Mozdony mozdony;

	@Id
	private int id;    
 
	
    public Vonat() {
    }
    
    public Vonat(int _id, int _keses, Date _datum, Vonatszam vsz, Mozdony m) {
    	this.id = _id;
    	this.keses = _keses;
    	this.datum = _datum;
    	this.setSzam(vsz);
    	this.setMozdony(m);
    }
    
	public int getId() {
    	return id;
	}

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getKeses() {
        return keses;
    }

    public void setKeses(int keses) {
        this.keses = keses;
    }
    @OneToOne
	public Vonatszam getSzam() {
		return szam;
	}

	public void setSzam(Vonatszam szam) {
		this.szam = szam;
	}

	@OneToOne
	public Mozdony getMozdony() {
		return mozdony;
	}

	public void setMozdony(Mozdony mozdony) {
		this.mozdony = mozdony;
	}

	public String toString() {
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy.MM.dd");
		String tmpDatum = sdfr.format( datum );
		
		return szam.getSzam() + " " + tmpDatum + " " + mozdony.getId() + " " + mozdony.getFutottkm() + " " + keses; 
	}
}
