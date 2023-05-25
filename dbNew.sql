CREATE TABLE fornitore(
    codice_fornitore int primary key auto_increment,
    nome_fornitore VARCHAR(255) not null,
    valutazione int not null check (valutazione > 0 and valutazione < 6),
    soglia int not null check (soglia > 0)
);
CREATE TABLE utente(
    email VARCHAR(255) primary key,
    nome VARCHAR(255) not null,
    cognome VARCHAR(255) not null,
    indirizzo VARCHAR(255) not null,
    password VARCHAR(255) not null
);

CREATE TABLE prodotto(
    codice_prodotto int primary key auto_increment,
    nome_prodotto VARCHAR(255) not null,
    categoria VARCHAR(255) not null,
    foto VARCHAR(255) not null,
    descrizione VARCHAR(255) not null
);
CREATE TABLE ordini (
    codice_ordine INT PRIMARY KEY AUTO_INCREMENT,
    nome_fornitore VARCHAR(255),
    data_spedizione DATE NOT NULL,
    prezzo_totale INT NOT NULL CHECK (prezzo_totale > 0),
    email VARCHAR(255),
    FOREIGN KEY (email) REFERENCES utente(email)
);
CREATE TABLE fasce(
    codice_fornitore int not null,
    min int not null check (min > 0),
    max int not null check (max > 0),
    prezzo int not null check (prezzo > 0),
    primary key (codice_fornitore, min),
    foreign key (codice_fornitore) references fornitore(codice_fornitore)


    /*,CONSTRAINT check_intervalli_unici CHECK (NOT EXISTS (
        SELECT 1 FROM fasce AS f2
        WHERE f2.codice_fornitore = fasce.codice_fornitore
        AND f2.min <> fasce.min
        AND ((f2.min <= fasce.min AND f2.max >= fasce.min) OR (f2.min <= fasce.max AND f2.max >= fasce.max)
                OR (f2.min >= fasce.min AND f2.max <= fasce.max))
    )),
    CONSTRAINT check_zero_spazi CHECK (EXISTS (
        SELECT 1 FROM fasce AS f2
        WHERE f2.codice_fornitore = fasce.codice_fornitore
        AND (f2.min = fasce.max + 1 OR f2.min = 1)
    ))*/
);
create table visualizza(
    email VARCHAR(255),
    codice_prodotto int,
    primary key (email, codice_prodotto),
    foreign key (email) references utente(email),
    foreign key (codice_prodotto) references prodotto(codice_prodotto)
);
create table vende(
    codice_fornitore int not null,
    codice_prodotto int not null,
    prezzo int not null check (prezzo > 0),
    scnto float not null check (scnto > 0 and scnto < 1),
    primary key (codice_fornitore, codice_prodotto),
    foreign key (codice_fornitore) references fornitore(codice_fornitore),
    foreign key (codice_prodotto) references prodotto(codice_prodotto)
);

create table contiene(
    codice_ordine int not null,
    codice_prodotto int not null,
    quantita int not null check (quantita > 0),
    prezzo_unitario int not null check (prezzo_unitario > 0),
    primary key (codice_ordine, codice_prodotto),
    foreign key (codice_ordine) references ordini(codice_ordine),
    foreign key (codice_prodotto) references prodotto(codice_prodotto)
);

-- Popolazione della tabella "fornitore"
INSERT INTO fornitore (nome_fornitore, valutazione, soglia)
VALUES ('Fornitore A', 4, 400),
       ('Fornitore B', 5, 350),
       ('Fornitore C', 3, 150);

-- Popolazione della tabella "utente"
INSERT INTO utente (email, nome, cognome, indirizzo, password)
VALUES ('utente1@example.com', 'Mario', 'Rossi', 'Via Roma 1', 'password1'),
       ('utente2@example.com', 'Laura', 'Verdi', 'Via Milano 2', 'password2'),
       ('utente3@example.com', 'Giovanni', 'Bianchi', 'Via Napoli 3', 'password3');

-- Popolazione della tabella "prodotto"
INSERT INTO prodotto (nome_prodotto, categoria, foto, descrizione)
VALUES ('Prodotto 1', 'Categoria 1', 'foto1.jpg', 'Descrizione prodotto 1'),
       ('Prodotto 2', 'Categoria 1', 'foto2.jpg', 'Descrizione prodotto 2'),
       ('Prodotto 3', 'Categoria 2', 'foto3.jpg', 'Descrizione prodotto 3');

-- Popolazione della tabella "fasce"
INSERT INTO fasce (codice_fornitore, min, max, prezzo)
VALUES (1, 1, 10, 100),
       (1, 11, 20, 90),
       (2, 1, 5, 50),
       (2, 6, 10, 45),
       (2, 11, 15, 40);

-- Popolazione della tabella "ordini"
INSERT INTO ordini (nome_fornitore, data_spedizione, prezzo_totale, email)
VALUES ('Fornitore A', '2023-05-20', 440, 'utente1@example.com'),
       ('Fornitore B', '2023-05-21', 360, 'utente2@example.com');

-- Popolazione della tabella "visualizza"
INSERT INTO visualizza (email, codice_prodotto)
VALUES ('utente1@example.com', 1),
       ('utente2@example.com', 2),
       ('utente3@example.com', 3);

-- Popolazione della tabella "vende"
INSERT INTO vende (codice_fornitore, codice_prodotto, prezzo, scnto)
VALUES (1, 1, 100, 0.1),
       (1, 2, 80, 0.05),
       (2, 2, 70, 0.15),
       (2, 3, 60, 0.2);

-- Popolazione della tabella "contiene"
INSERT INTO contiene (codice_ordine, codice_prodotto, quantita, prezzo_unitario)
VALUES (1, 1, 2, 100),
       (1, 2, 3, 80),
       (2, 2, 1, 70),
       (2, 3, 4, 60);









