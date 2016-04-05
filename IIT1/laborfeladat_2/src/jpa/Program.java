package jpa;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.logging.*;

import javax.naming.directory.InvalidAttributeValueException;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class Program {

	private EntityManagerFactory factory;
	private EntityManager em;


	public void initDB() {
		factory = Persistence.createEntityManagerFactory(CommonData.getUnit());
		em = factory.createEntityManager();
	}

	void closeDB() {

		em.close();
	}

	public Program(EntityManager em) {
	        this.em = em;
	}	

	public Program() {
}	
	
	public static void main(String[] args) {
		Program app = new Program();
		app.initDB();
		app.startControl();
		app.closeDB();
	
    }


    public void startControl() {
//	    InputStream input = System.in;
        BufferedReader instream = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print(">");
                String inputLine = instream.readLine();
                StringTokenizer tokenizer = new StringTokenizer(inputLine, "   ");
                String command = tokenizer.nextToken();

                if ("t".startsWith(command)) {
                    ujTipus(readString(tokenizer), readString(tokenizer));
                } else if ("m".startsWith(command)) {
                    ujMozdony(readString(tokenizer), readString(tokenizer), readString(tokenizer));
                } else if ("s".startsWith(command)) {
                    ujVonatszam(readString(tokenizer), readString(tokenizer));
                } else if ("v".startsWith(command)) {
                    ujVonat(readString(tokenizer), readString(tokenizer),  readString(tokenizer), readString(tokenizer));
                } else if ("l".startsWith(command)) {
                    String targy = readString(tokenizer);
                    if ("t".startsWith(targy)) {
                        listazTipus();
                    } else if ("m".startsWith(targy)) {
                        listazMozdony();
                    } else if ("s".startsWith(targy)) {
                        listazVonatszam();
                    } else if ("v".startsWith(targy)) {
                        listazVonat();
                    }
                } else if ("x".startsWith(command)) {
                    lekerdezes(readString(tokenizer));
                } else if ("e".startsWith(command)) {
                    break;
                } else {
                    throw new Exception("Hibas parancs! (" + inputLine + ")");
                }
            } catch (Exception e) {
                System.out.println("? ".concat( e.getMessage() ));
            }
        }

    }

    static String readString(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        } else {
            throw new Exception("Keves parameter!");
        }
    }

    //Uj entitások felvetelehez kapcsolodo szolgaltat�sok
    protected void ujEntity(Object o) throws Exception {
        em.getTransaction().begin();
        try {
            em.persist(o);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
        	/*if (em.isOpen())
        		em.close();*/
        }
    }
    
    // Uj tipus felvetele
    public void ujTipus(String azonosito, String fajta) throws Exception {
    	if (fajta == null)
    		throw new Exception("Hiba uj Tipus megadaskor: A tipus neve ures!");
    	else if (azonosito == null)
    		throw new Exception("Hiba uj Tipus megadaskor: Azonosito ures!");
    	
    	try {
	    	Query azonositok = em.createQuery(
	    			"SELECT t.azonosito "
	    		  + "FROM Tipus t "
	    		  + "WHERE t.azonosito = :adottAzonosito"
	    			); //Kell-e ellenorizni ugyanazt a fajta ne legyen benne? Le kell-e z�rni?
	    	
	    	azonositok.setParameter("adottAzonosito", azonosito);
	    	
	    	List<String> temp = azonositok.getResultList();
	    	
	    	if (!(temp.isEmpty()))
	    		throw new InvalidAttributeValueException("Ilyen azonositoju mozdony mar van!");
	    	
	    	// Felt�lt�s
	    	ujEntity(new Tipus(azonosito, fajta));
	    	
	    	// Nincs adott azonosit�j� tipus
    	} catch (InvalidAttributeValueException iave) {
    		throw new Exception("Hiba uj Tipus beszurasanal: " + iave.getMessage());
    	} catch (Exception e) {
    		throw new Exception(e.toString());
    	}	
    }

    // Uj mozdony felvetele
    public void ujMozdony(String sorszam, String tipusID, String futottkm) throws Exception {
        //TODO
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket.
        //Ellen�rizze a t�pus l�tez�s�t
    	//Hozza l�tre az �j "Mozdony" entit�st �s r�gz�tse adatb�zisban az "ujEntity" met�dussal.
    	
    	try {
    		int id = Integer.parseInt(sorszam);
    		int km = Integer.parseInt(futottkm);
    		
    		Query tipusAzonosito = em.createQuery(
	    			"SELECT t "
	    		  + "FROM Tipus t "
	    		  + "WHERE t.azonosito = :adottAzonosito"
	    			); //Kell-e ellenorizni ugyanazt a fajta ne legyen benne? Le kell-e z�rni?
	    	
	    	tipusAzonosito.setParameter("adottAzonosito", tipusID);
	    	
	    	List temp = tipusAzonosito.getResultList();
	    	
	    	if (temp.isEmpty())
	    		throw new InvalidAttributeValueException("Nincs ilyen Azonosito");
	    	
	    	Query mozdonyAzonosito = em.createQuery(
	    			"SELECT m.id "
	    		  + "FROM Mozdony m "
	    		  + "WHERE m.id = :adottAzonosito"
	    			);
	    	mozdonyAzonosito.setParameter("adottAzonosito", id);
	    	
	    	// Ha nem ures, akkor mar van ilyen, az baj
	    	if (!(mozdonyAzonosito.getResultList().isEmpty()))
	    		throw new InvalidAttributeValueException("Mar van ilyen azonositoju mozdony!");
	    	
	    	System.out.println(temp.size());
	    	Tipus t = (Tipus)temp.get(0);
	    	
	    	// Felt�lt�s
	    	ujEntity(new Mozdony(id, t, km));
    		 		
    	} catch (NumberFormatException nfe) {
    		throw new Exception("A parameterben kapott tipus nem megfelelo: " + nfe.getMessage());
    	} catch (InvalidAttributeValueException iave) {
    		throw new Exception("Hiba: "+iave.getMessage());
    	} catch (Exception e) {
    		throw new Exception(e.toString());
    	}  	
    }

    // Uj vonatszam felvetele
    public void ujVonatszam(String sorszam, String uthossz) throws Exception {
        //TODO
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket.
        //Ellen�rizze, hogy van-e m�r ilyen vonatsz�m
    	//Hozza l�tre az �j "Vonatsz�m" entit�st �s r�gz�tse adatb�zisban az "ujEntity" met�dussal.
    	try {
    		int szam = Integer.parseInt(sorszam);
    		long hossz = Long.parseLong(uthossz);
    		
    		Query vonatszam = em.createQuery(
	    			"SELECT v.szam "
	    		  + "FROM Vonatszam v "
	    		  + "WHERE v.szam = :adottSzam"
	    			); //Kell-e ellenorizni ugyanazt a fajta ne legyen benne? Le kell-e z�rni?
	    	
    		vonatszam.setParameter("adottSzam", szam);
	    	
	    	List<String> temp = vonatszam.getResultList();
	    	
	    	if (!(temp.isEmpty()))
	    		throw new InvalidAttributeValueException("Ilyen azonositoju vonatszam mar van!");
	    	
	    	// Felt�lt�s
	    	ujEntity(new Vonatszam(szam, hossz));
    		 		
    	} catch (NumberFormatException iave) {
    		throw new Exception("A parameterben kapott tipus nem megfelelo: " + iave.getMessage());
    	} catch (Exception e) {
    		throw new Exception(e.toString());
    	}  	
    }

    // Uj vonat felvetele
    public void ujVonat(String vonatszamAzonosito, String datum, String mozdonySorszam, String keses) throws Exception {
       	//TODO
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket. Tipp: haszn�lja a SimpleDateFormat-ot
    	//Form�tum: "yyyy.MM.dd"
        //Ellen�rizze, hogy �rv�nyes-e a vonatsz�m, �s l�tezik a mozdony.
        //Ellen�rizze, hogy az adott napon nincs m�sik vonat ugyanezzel a vonatsz�mmal.		
    	//Hozza l�tre az �j "Vonat" entit�st �s r�gz�tse adatb�zisban az "ujEntity" met�dussal.
        //N�velje a mozdony futottkm-�t a vonatsz�m szerinti �thosszal. 
    	try {
    		int szam = Integer.parseInt(vonatszamAzonosito);
    		int id = Integer.parseInt(mozdonySorszam);
    		int kes = Integer.parseInt(keses);
    		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    		String formated_date = (format.parse(datum)).toString();
    		
    	} catch (NumberFormatException iave) {
    		throw new Exception("A parameterben kapott tipus nem megfelelo: " + iave.getMessage());
    	} catch (Exception e) {
    		throw new Exception(e.toString());
    	}  	
    }

    //Listazasi szolgaltatasok
    public void listazEntity(List list) {
        for (Object o : list) {
            System.out.println(o);
        }
    }

    //Tipusok listazasa
    public void listazTipus() throws Exception {
        listazEntity(em.createQuery("SELECT t FROM Tipus t").getResultList());
    }

    //Mozdonyok listazasa
    public void listazMozdony() throws Exception {
    	listazEntity(em.createQuery("SELECT m FROM Mozdony m").getResultList());
    }

    //Vonatszamok listazasa
    public void listazVonatszam() throws Exception {
    	listazEntity(em.createQuery("SELECT vsz FROM Vonatszam vsz").getResultList());
    }

    //Vonatok listazasa
    public void listazVonat() throws Exception {
    	listazEntity(em.createQuery("SELECT v FROM Vonat v").getResultList());
    }

    //Egyedi lekerdezes
    public void lekerdezes(String datum) throws Exception {
    	//TODO    	
        //�rja ki a param�terk�nt kapott napra (INPUTNAP) vonatkoz�an, hogy az
        //egyes mozdony-fajt�k az adott napon �sszesen h�ny kilom�tert futottak.    	
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket. Tipp: haszn�lja a SimpleDateFormat-ot
        //Tipp: N�zzen ut�na a "t�bbsz�r�s SELECT" kezel�s�nek
    }
}
