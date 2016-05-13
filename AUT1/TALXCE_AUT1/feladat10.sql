create trigger uj_kategoria
on kategoria_nezet
instead of insert

as

declare c1 cursor for
select kategoria, szulo_kategoria
from inserted

declare @kat nvarchar(50), @szul_kat nvarchar(50)

open c1
fetch c1 into @kat, @szul_kat
while @@FETCH_STATUS = 0
begin

	declare @szulkatid int
	select @szulkatid = id
	from kategoria
	where nev = @szul_kat

	if @szulkatid is not null
	begin
		insert into kategoria
		values(@kat, @szulkatid)
	end
	else
		raiserror('Hibas szulo kategoria!', 16, 1)

	fetch c1 into @kat, @szul_kat
end
