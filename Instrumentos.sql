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
     
CREATE TABLE Calibraciones (
    id int AUTO_INCREMENT not null,  /* provoca que no tengamos que ponerlo en values */
    fecha DATE not null,   /* formato de fecha */
    mediciones int,
    instrumento_serie varchar(10) not null,
    Primary Key (id)
);

CREATE TABLE Mediciones (
     medida varchar(10) not null,
     referencia varchar(10) not null,
     lectura varchar (10) not null,
     calibracion_id int,
     Primary Key (medida)
);
ALTER TABLE Instrumento ADD Foreign Key (tipo) REFERENCES TipoInstrumento(codigo);
Alter TABLE Calibraciones ADD Foreign key (instrumento_serie) REFERENCES Instrumento(serie);
Alter TABLE Mediciones ADD Foreign Key (calibracion_id) REFERENCES Calibraciones(id);
     