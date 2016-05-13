create or replace procedure uj_szamlakiallito_sp
(pnev nvarchar2,
pirszam char,
pvaros nvarchar2,
putca nvarchar2,
padoszam nvarchar2,
pszamlaszam nvarchar2
)

is

begin
  insert into szamlakiallito
  values (kiallito_seq.nextval, pnev,pirszam,pvaros,putca,padoszam,pszamlaszam);
end;
