CREATE DATABASE Instrumentos;

use Instrumentos;

create table TipoInstrumento (
       codigo  varchar(10)  not null,
       nombre varchar(30) not null,
	   unidad  varchar(20),
       Primary Key (codigo)         
     );

insert into TipoInstrumento (codigo ,nombre, unidad) values('TER','Termómetro','Grados Celcius');

create table Instrumento(
	   serie  varchar(10)  not null,
	   tipo  varchar(10) not null,
       descripcion varchar(30) not null,
	   minimo int,
	   maximo int,
	   tolerancia int,
       Primary Key (serie)         
     );
     
ALTER TABLE Instrumento ADD Foreign Key (tipo) REFERENCES TipoInstrumento(codigo);
     