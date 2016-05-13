create or replace procedure megrendeles_lista_sp
(pstatus nvarchar2)
is
cursor c1 is select m.id, m.datum, m.hatarido,
                    (t.ir || '' || t.varos || '' t.utca) as t_cim,
                    f.mod
from megrendeles m, telephely t, fizetesmod f, statusz s
where m.telephelyid = t.id
and m.fizetesmodid = f.id
and m.statuszid = s.id
and upper(s.nev) = upper(pstatus);

begin
  
end;
