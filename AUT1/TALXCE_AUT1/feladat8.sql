select mt.id as MT_ID,m.osszmegrendeles, mt.nettoar, mt.mennyiseg
from megrendeles m join megrendelestetel mt on mt.megrendelesid = m.id
where m.id = 2;

insert into megrendelestetel
values(1200, 2, 100, 2, 3, 1);

UPDATE megrendelestetel
SET mennyiseg = 3, nettoar = 700
WHERE id = 1200;

DELETE FROM megrendelestetel
WHERE id = 1200;

commit;
