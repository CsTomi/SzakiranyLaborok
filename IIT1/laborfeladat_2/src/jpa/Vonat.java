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
	
    private int keses;
    private Mozdony mozdony;
    
    @Id
    private Vonatszam vonatSzam;    
	@Id
	private int id;    
	@Id
	@Temporal(TemporalType.DATE)
    private  Date datum;
	
    public Vonat() {
    }
    
    public Vonat(int _id, int _keses, Date _datum, Vonatszam vsz, Mozdony m) {
    	this.id = _id;
    	this.keses = _keses;
    	this.datum = _datum;
    	this.setVSzam(vsz);
    	this.setMozdony(m);
    }
    
    @OneToOne
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
    
	public Vonatszam getVSzam() {
		return vonatSzam;
	}

	public void setVSzam(Vonatszam szam) {
		this.vonatSzam = szam;
	}

	public Mozdony getMozdony() {
		return mozdony;
	}

	public void setMozdony(Mozdony mozdony) {
		this.mozdony = mozdony;
	}

	public String toString() {
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy.MM.dd");
		String tmpDatum = sdfr.format( datum );
		
		return vonatSzam.getSzam() + " " + tmpDatum + " " + mozdony.getId() + " " + mozdony.getFutottkm() + " " + keses; 
	}
}
