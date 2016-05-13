create view kategoria_nezet
as
select a.Nev as Kategoria, b.Nev as Szulo_kategoria
from Kategoria a left outer join
	 Kategoria b on a.SzuloKategoria = b.ID
