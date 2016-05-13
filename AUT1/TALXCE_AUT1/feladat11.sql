alter procedure szamla_check_ps
	@SzamlaId int
as

begin tran

declare c cursor fast_forward read_only for
	select szt.mennyiseg as Szamla_mennyiseg, szt.nettoar as Szamla_nettoar, szt.afakulcs as Szamla_afa,
		   mt.Mennyiseg as Tetel_mennyiseg, mt.NettoAr as Tetel_nettoar,
		   a.Kulcs as Afa_kulcs
	from  SzamlaTetel szt
	join MegrendelesTetel mt on mt.ID = szt.MegrendelesTetelID
	join AFA a on szt.AFAKulcs = a.Kulcs
	where @SzamlaId = szt.id

	DECLARE @Szamla_Mennyiseg int
	DECLARE @Tetel_Mennyiseg int
	
	DECLARE @Szamla_NettoAr int
	DECLARE @Tetel_NettoAr int
	
	DECLARE @Szamla_AFA int
	DECLARE @Tetel_AFA int

	open c
		FETCH NEXT FROM c
		INTO @Szamla_Mennyiseg, @Szamla_NettoAr, @Szamla_AFA, @Tetel_Mennyiseg, @Tetel_NettoAr, @Tetel_AFA

		WHILE @@FETCH_STATUS = 0
		BEGIN
			--print kell még --egyeztetés kell még
			if (@Szamla_Mennyiseg <> @Tetel_Mennyiseg) or 
			   (@Szamla_NettoAr <> @Tetel_NettoAr) or 
			   (@Szamla_AFA <> @Tetel_AFA)
			begin
				print 'Eltérés az adatok között: '+
				'Mennyiség: '+convert(varchar(2),@Szamla_Mennyiseg)+' <--> '+convert(varchar(2), @Tetel_Mennyiseg)+
				'Nettó ár: '+convert(varchar(7), @Szamla_NettoAr)+' <--> '+convert(varchar(7), @Tetel_NettoAr)+
				'Áfa: '+convert(varchar(7), @Szamla_AFA)+' <--> '+convert(varchar(7), @Tetel_AFA)
			end

			FETCH NEXT FROM c
			INTO @Szamla_Mennyiseg, @Szamla_NettoAr, @Szamla_AFA, @Tetel_Mennyiseg, @Tetel_NettoAr, @Tetel_AFA
		END

	close c

	deallocate c

commit