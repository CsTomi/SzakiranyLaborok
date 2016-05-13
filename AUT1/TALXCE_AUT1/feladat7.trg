create or replace trigger AIUD_megrendeles
  after insert or update or delete
  on megrendelestetel
  for each row
declare
  -- local variables here
begin
  update megrendeles m
  set osszmegrendeles = nvl( osszmegrendeles, 0) 
                      - nvl( :old.mennyiseg * :old.nettoar, 0)
                      + nvl( :new.mennyiseg * :new.nettoar, 0)
  where id = nvl(:old.megrendelesid, :new.megrendelesid);
  
end AIUD_megrendeles;