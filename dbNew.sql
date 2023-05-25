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








