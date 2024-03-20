create database test_karana;
\c test_karana;

create table matiere (
    id varchar(10) primary key,
    matiere varchar(50) unique
);

create table categorie (
    id varchar(10) primary key,
    categorie varchar(50) unique
);

create table style (
    id varchar(10) primary key,
    style varchar(50) unique
);

create table matiere_style (
    id serial primary key,
    idmatiere varchar(10) references matiere(id),
    idstyle varchar(10) references style(id)
);

create table produit (
    id varchar(10) primary key,
    nom varchar(50),
    idstyle varchar(10) references style(id),
    idCategorie varchar(10) references categorie(id)
);

insert into matiere values 
('MT0001', 'Fantsika'),
('MT0002', 'Hazo'),
('MT0003', 'Lamba');

insert into categorie values
('CT0001', 'Seza'),
('CT0002', 'Latabatra'),
('CT0003', 'Fandrina');

insert into style values
('ST0001', 'Boheme'),
('ST0002', 'Royale');

insert into produit values
('P0001', 'Taboure', 'ST0001', 'CT0001'),
('P0002', 'Table Basse', 'ST0002', 'CT0002'),
('P0003', 'Fandrina tsara', 'ST0001', 'CT0003');

create table personne (
    id serial primary key,
    nom varchar(50)
);