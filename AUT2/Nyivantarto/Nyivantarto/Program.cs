using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Nyivantarto
{
    class Program
    {
        //2.
        static void TermekListazas()
        {
            using (var context = new TALXCEEntities())
            {

                foreach (var termek in context.Termek)
                {
                    Console.WriteLine("{0}", termek.Nev);
                }
                Console.WriteLine(context.GetType());
                Console.WriteLine();
                Console.WriteLine(context.Vevo.GetType());

            }//Lezarja a kapcsolatot
        }

        //3.
        static void HatekonyTermekListazas()
        {
            using (var context = new TALXCEEntities())
            {
                IOrderedQueryable<Termek> query = from t in context.Termek
                            where t.Raktarkeszlet > 0
                            orderby t.Nev
                            select t;

                foreach (var termek in query)
                {
                    Console.WriteLine("{0}", termek.Nev);
                }

            }//Lezarja a kapcsolatot
        }

        //4.
        static void TermekListazasBrutto()
        {
            using (var context = new TALXCEEntities())
            {
                var query = from t in context.Termek
                            orderby t.Nev
                            select new // Anonymus típus
                            {
                                Nev = t.Nev,
                                Brutto = t.NettoAr*(100+t.AFA.Kulcs)/100
                            };

                foreach (var termek in query)
                {
                    Console.WriteLine("{0, -25} {1, 15}", termek.Nev, termek.Brutto);
                }

            }
        }

        //5.
        static void Megrendelesek()
        {
            using (var context = new TALXCEEntities())
            {
                var query = from t in context.Termek
                            orderby t.Nev
                            select new
                            {
                                Nev = t.Nev,
                                Ossz = t.MegrendelesTetel.Sum(m => m.Mennyiseg) // join helyett
                            };

                foreach (var termek in query)
                {
                    Console.WriteLine("{0, -25} {1, 15} db", termek.Nev, termek.Ossz);
                }

            }//Lezarja a kapcsolatot
        }

        //6.
        static void Eladas()
        {
            using (var context = new TALXCEEntities())
            {
                //SQL: update utasítás lenne
                Termek termek = context.Termek.Single(t => t.ID == 3); //kivétel, ha nem tud elemmel visszatérni.

                Console.WriteLine(termek.Raktarkeszlet);
                termek.Raktarkeszlet--;
                Console.WriteLine(termek.Raktarkeszlet);

                context.SaveChanges();// explicit nekünk kell megadni!

            }//Lezarja a kapcsolatot
        }

        //7.
        static void Beszallitas()
        {
            using (var context = new TALXCEEntities())
            {
                Kategoria kategoria = new Kategoria();
                kategoria.Nev = "Csonthejasok";

                Termek termek = new Termek();
                termek.Nev = "Nagy kakas";
                termek.NettoAr = 420;

                termek.Kategoria = kategoria; // navigation property-k mentén belementi a kategóriát is

                context.Termek.Add(termek);
                context.SaveChanges();

                Console.WriteLine(kategoria.ID);
                Console.WriteLine(termek.ID);
            }
        }

        //8.
        static void AfaMentesTetelek()
        {
            using (var context = new TALXCEEntities())
            {
                foreach (var tetel in context.SzamlaTetel)
                {
                    Console.WriteLine("{0}:", tetel.Nev);
                    Console.WriteLine(tetel.GetType());
                    Console.WriteLine();
                }
            }
        }

        //9.
        static void VevoListazas()
        {
            using (var context = new TALXCEEntities())
            {

                foreach (var vevo in context.Vevo)
                {
                    Console.WriteLine("{0}", vevo.Nev);
                }

            }//Lezarja a kapcsolatot
        }

        //10.
        static void HatekonyVevoListazas()
        {
            using (var context = new TALXCEEntities())
            {
                var query = from v in context.Vevo
                            where v.Nev.Contains("o")
                            orderby v.Nev
                            select v.Nev;

                foreach (var vevo in query)
                {
                    Console.WriteLine("{0}", vevo);
                }

            }
        }

        //11.
        static void VevoTelephelyListazas()
        {
            using (var context = new TALXCEEntities())
            {
                var query = from v in context.Vevo
                            orderby v.Nev
                            select new
                            {
                                Nev = v.Nev,
                                Telep = v.KozpontiTelephely.IR + " " +
                                        v.KozpontiTelephely.Varos + ", " +
                                        v.KozpontiTelephely.Utca
                            };

                foreach (var vevo in query)
                {
                    Console.WriteLine("{0, -25} {1, 15}", vevo.Nev, vevo.Telep);
                }

            }
        }

        //12.
        static void VevoMegrendelesek()
        {
            using (var context = new TALXCEEntities())
            {
                var query = from v in context.Vevo
                            orderby v.Nev
                            select new
                            {
                                Nev = v.Nev,
                                Ossz = v.KozpontiTelephely.Megrendeles.Count() // join helyett
                            };

                foreach (var vevo in query)
                {
                    Console.WriteLine("{0, -25} {1, 15} db", vevo.Nev, vevo.Ossz);
                }

            }//Lezarja a kapcsolatot
        }

        //13.
        static void Rendeles()
        {
            using (var context = new TALXCEEntities())
            {
                Telephely NorbertTelepe = context.Telephely.Single(t => t.Vevo.Nev == "Puskás Norbert");

                var RendelesQuery = from m in context.Megrendeles
                                    where m.TelephelyID == NorbertTelepe.ID
                                    select m;

                Console.WriteLine("Előtte:\n");

                List<MegrendelesTetel> ujTetelek = new List<MegrendelesTetel>();

                foreach (var rendeles in RendelesQuery)
                {
                    foreach (var tetel in rendeles.MegrendelesTetel)
                    {
                        Console.WriteLine("{0}:\t{1}db\t{2}", tetel.Termek.Nev, tetel.Mennyiseg, tetel.Statusz.Nev);

                        MegrendelesTetel ujTetel = new MegrendelesTetel();
                        ujTetel.StatuszID = 1;
                        ujTetel.NettoAr = tetel.NettoAr;
                        ujTetel.Mennyiseg = 1;
                        ujTetel.Termek = tetel.Termek;
                        ujTetel.Megrendeles = tetel.Megrendeles;

                        ujTetelek.Add(ujTetel);
                    }                    
                }

                foreach (var rendeles in RendelesQuery)
                {
                    foreach (var tetel in ujTetelek)
                    {
                        rendeles.MegrendelesTetel.Add(tetel);
                    }
                    NorbertTelepe.Megrendeles.Add(rendeles);
                }

                context.SaveChanges();

                Console.WriteLine("\nUtána:\n");
                
                foreach (var rendeles in RendelesQuery)
                {
                    foreach (var tetel in rendeles.MegrendelesTetel)
                    {
                        Console.WriteLine("{0}:\t{1}db\t{2}", tetel.Termek.Nev, tetel.Mennyiseg, tetel.Statusz.Nev);
                    }
                }
            }
        }

        //14.
        static void Kiszallitas()
        {
            using (var context = new TALXCEEntities())
            {
                var query = from m in context.Megrendeles
                            where m.StatuszID != 5
                            select m;

                foreach (var rendeles in query)
                {
                    Console.WriteLine("{0}:\t{1}", rendeles.ID, rendeles.StatuszID);
                    rendeles.StatuszID = 5;
                    foreach(var tetel in rendeles.MegrendelesTetel)
                    {
                        tetel.StatuszID = 5;
                    }
                }

                context.SaveChanges();
            }
        }
        

        static void Main(string[] args)
        {
            /*
                Jzkba:
                -Leírás
                -Kód
                -Screenshot, hogy működik
                -Csak egy PDF kell. Nem kell a program maga!
                .Köv hétőf 14:00-ig!!
            */


            //2. - kész
            //TermekListazas(); // Meg is hivjuk... What a day!

            //3. - kész
            //HatekonyTermekListazas();

            //4. - kész
            //TermekListazasBrutto();

            //5. - 
            //Megrendelesek();

            //6.
            /*Console.WriteLine("Elso futtatas: ");
            Eladas();
            Console.WriteLine("\nMasodik futtatas:");
            Eladas();*/

            //7.
            //Beszallitas();

            //8. // leszármaztottattuk a számlatételt és a mappinget módosítottuk
            //AfaMentesTetelek();

            //9.
            //VevoListazas();

            //10.
            //VevoListazas();
            //Console.WriteLine("\n------------------------------\n");
            //HatekonyVevoListazas();

            //11.
            //VevoTelephelyListazas();

            //12.
            //VevoMegrendelesek();

            //13.
            Rendeles();

            //14.
            /*Console.WriteLine("{0, -25} {1, 15}", "Rendeles ID", "Rendeles Statusza");
            Console.WriteLine();

            Kiszallitas();

            Console.WriteLine();
            using (var cnt = new TALXCEEntities())
            {
                var query = from m in cnt.Megrendeles
                            where m.StatuszID == 5
                            select m;

                foreach (var rendeles in query)
                {
                    Console.WriteLine("{0}:\t{1}", rendeles.ID, rendeles.StatuszID);
                }
           }*/

            //15.


            //16.

            Console.ReadKey();

        }
    }
}
