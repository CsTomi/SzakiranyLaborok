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

    //Uj entitÃ¡sok felvetelehez kapcsolodo szolgaltatások
    protected void ujEntity(Object o) throws Exception {
        em.getTransaction().begin();
        try {
            em.persist(o);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
    
    // Uj tipus felvetele
    public void ujTipus(String azonosito, String fajta) throws Exception {
    	try {
    		// van-e adott azonositoju tipus?
	    	Query azonositok = em.createQuery(
	    			"SELECT t.azonosito "
	    		  + "FROM Tipus t "
	    		  + "WHERE t.azonosito = :adottAzonosito"
	    			);	    	
	    	azonositok.setParameter("adottAzonosito", azonosito);
	    	
	    	// listaba az eredmenyekkel
	    	List<String> temp = azonositok.getResultList();
	    	
	    	// ha nem ures, akkor ilyen azonosito mar van
	    	if (!(temp.isEmpty()))
	    		throw new InvalidAttributeValueException("Ilyen azonositoju mozdony mar van!");
	    	
	    	// nincs akadalya a perzisztalasnak
	    	ujEntity(new Tipus(azonosito, fajta));
	    	
    	} catch (InvalidAttributeValueException iave) {
    		throw new Exception("Hiba uj Tipus beszurasanal: " + iave.getMessage()); // letezo azon.
    	} catch (Exception e) {
    		throw new Exception(e.toString()); // valami mas
    	}	
    }

    // Uj mozdony felvetele
    public void ujMozdony(String sorszam, String tipusID, String futottkm) throws Exception {   	
    	try {
    		int id = Integer.parseInt(sorszam);
    		int km = Integer.parseInt(futottkm);
    		
    		Query tipusAzonosito = em.createQuery(
	    			"SELECT t "
	    		  + "FROM Tipus t "
	    		  + "WHERE t.azonosito = :adottAzonosito"
	    			);
	    	
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
	    	
	    	Tipus t = (Tipus)temp.get(0);
	    	
	    	// Feltöltés
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
    	try {
    		// ha nem lehet castolni, NumberFormatException-t dobnak
    		int szam = Integer.parseInt(sorszam);
    		long hossz = Long.parseLong(uthossz);
    		
    		// van-e adott sorszamu mozdony
    		Query vonatszam = em.createQuery(
	    			"SELECT v.szam "
	    		  + "FROM Vonatszam v "
	    		  + "WHERE v.szam = :adottSzam"
	    			);
    		vonatszam.setParameter("adottSzam", szam);
	    	
	    	List temp = vonatszam.getResultList();
	    	
	    	if (!(temp.isEmpty()))
	    		throw new InvalidAttributeValueException("Ilyen azonositoju vonatszam mar van!");
	    	
	    	// perzisztalas
	    	ujEntity(new Vonatszam(szam, hossz));
    		 		
    	} catch (NumberFormatException iave) {
    		throw new Exception("A parameterben kapott tipus nem megfelelo: " + iave.getMessage());
    	} catch (Exception e) {
    		throw new Exception(e.toString());
    	}  	
    }

    // Uj vonat felvetele
    public void ujVonat(String vonatszamAzonosito, String datum, String mozdonySorszam, String keses) throws Exception {
    	try {
    		// hibat dobnak, ha nem lehet
    		int vonatSzam = Integer.parseInt(vonatszamAzonosito);
    		int mozdonyID = Integer.parseInt(mozdonySorszam);
    		int vonatKeses = Integer.parseInt(keses);
    		
    		
    		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    		Date formated_date = format.parse(datum);
   
    		// Vonatszam ellenorzese
    		Query vaneVonatszam = em.createQuery("SELECT vsz "
    											+ "FROM Vonatszam vsz "
    											+ "WHERE vsz.szam = :adottVonatSzam");
    		vaneVonatszam.setParameter("adottVonatSzam", vonatSzam);
    		
    		List<Vonatszam> pVSzam = vaneVonatszam.getResultList(); 
    		
    		// ha nincs ilyen szamu vonat, akkor hibat kell dobni
    		if (pVSzam.isEmpty())
    			throw new InvalidAttributeValueException("Ilyen azonositoju vonatszam nincs!");
    		
    		// Mozdony ellenorzese
    		Query vaneMozdony = em.createQuery("SELECT m "
											   + "FROM Mozdony m "
											   + "WHERE m.id = :adottMozdonyID");
    		vaneMozdony.setParameter("adottMozdonyID", mozdonyID);
    		
    		List<Mozdony> pMozdony = vaneMozdony.getResultList();
    		
    		if (pMozdony.isEmpty())
    			throw new InvalidAttributeValueException("Ilyen azonositoju mozdony nincs!");
    		
    		// Datum ellenorzese
    		Query vaneVonatAznap = em.createQuery("SELECT v "
					   							+ "FROM Vonat v "
					   							+ "WHERE v.datum = ?1 ");
    		vaneVonatAznap.setParameter(1, formated_date, TemporalType.DATE);
    		
    		List<Vonat> pVonat = vaneVonatAznap.getResultList();
    		
    		// ures, akkor nincs akadaly, de ha nem ures, akkor ellenorizni kell a vonatszamot
    		if (!(pVonat.isEmpty())) {
    			for (Vonat object : pVonat) {
					if ((object.getVSzam()).getSzam() == vonatSzam)
						throw new InvalidAttributeValueException("Mar van vonat beosztva a kijelolt napra!");						
				}
    		}
    		
    		Mozdony m = pMozdony.get(0);
    		Vonatszam vsz = pVSzam.get(0);
    		m.setFutottkm( m.getFutottkm() + vsz.getUthossz().intValue() );
    		
    		// Perzisztalas
    		// MozdonyID azonositja a mozdonyt
    		ujEntity(new Vonat(mozdonyID, vonatKeses, formated_date, vsz, m));
    		
    	} catch (NumberFormatException nfe) {
    		throw new Exception("A parameterben kapott tipus nem megfelelo: " + nfe.getMessage());
    	} catch (InvalidAttributeValueException iave) {
    		throw new Exception("Hiba tortent vonat beszurasakor: " + iave.getMessage());
    	} catch (IllegalStateException i) {
    		System.out.println("Hiba adatbazis oldalon: "+i.getLocalizedMessage()+" " + i.getMessage());
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
        //Írja ki a paraméterként kapott napra (INPUTNAP) vonatkozóan, hogy az
        //egyes mozdony-fajták az adott napon összesen hány kilométert futottak.    	
        //Alakítsa át a megfelelõ típusokra a kapott String paramétereket. Tipp: használja a SimpleDateFormat-ot
        //Tipp: Nézzen utána a "többszörös SELECT" kezelésének
    }
}
