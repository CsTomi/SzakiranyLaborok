declare cursor c is
  select m.id, sum(mt.nettoar * mt.mennyiseg) as ossz
  from megrendeles m
       join megrendelestetel mt on mt.megrendelesid = m.id
  group by m.id;
  
begin
  for rec in c loop
    update megrendeles
    set osszmegrendeles = rec.ossz
    where id = rec.id;
    end loop;
    commit;
end;
