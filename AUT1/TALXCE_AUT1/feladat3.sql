create or replace procedure uj_szamlakiallito_sp
(pnev nvarchar2,
pirszam char,
pvaros nvarchar2,
putca nvarchar2,
padoszam nvarchar2,
pszamlaszam nvarchar2
)

is

dbnev nvarchar2(50);
hossz int;
karakter1 char;
karakter2 char;

begin
  select nev into dbnev
  from szamlakiallito
  where upper(nev) = upper(pnev);
  raise_application_error(-20101, 'Ilyen nevu mar van!');
  
  exception
    when no_data_found then
      hossz:= length(padoszam);
      if hossz!= 13 then
        raise_application_error(-20102, 'Nem 13 hosszu az adoszam!');
      end if;
      
      karakter1 := substr(padoszam,9,1);
      karakter2 := substr(padoszam,11,1);
      if(karakter1 = '-' and karakter2 = '-') then
         insert into szamlakiallito
         values (kiallito_seq.nextval, pnev,pirszam,pvaros,putca,padoszam,pszamlaszam);
      else
        raise_application_error(-20103, 'Rossz formatum!');
      end if;
end;
