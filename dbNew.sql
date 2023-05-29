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
    data timestamp default current_timestamp,
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

create table informazioni(
    codice_ordine int not null,
    codice_prodotto int not null,
    nome VARCHAR(255) not null,
    foto VARCHAR(255) not null,
    categoria VARCHAR(255) not null,
    quantita int not null check (quantita > 0),
    prezzo_unitario int not null check (prezzo_unitario > 0),
    descrizione VARCHAR(255) not null,
    primary key (codice_ordine, codice_prodotto),
    foreign key (codice_ordine) references ordini(codice_ordine)
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
VALUES
    ('Smartphone Galaxy S21', 'Informatica', 'smartphone_galaxy_s21.jpg', 'Potente smartphone con schermo AMOLED e fotocamera di alta qualità.'),
    ('Laptop ThinkPad X1 Carbon', 'Informatica', 'laptop_thinkpad_x1_carbon.jpg', 'Notebook leggero e performante con display ad alta risoluzione.'),
    ('Scarpe da running Nike Air Zoom Pegasus 38', 'Sport', 'scarpe_running_nike_pegasus_38.jpg', 'Scarpe da corsa con ammortizzazione reattiva e comfort duraturo.'),
    ('Orologio automatico Rolex Submariner', 'Orologi', 'orologio_rolex_submariner.jpg', 'Iconico orologio subacqueo con movimento automatico e impermeabilità fino a 300 metri.'),
    ('Macchina fotografica Canon EOS R5', 'Informatica', 'macchina_fotografica_canon_eos_r5.jpg', 'Fotocamera mirrorless ad alta risoluzione con registrazione video in 8K.'),
    ('TV OLED LG CX', 'Informatica', 'tv_oled_lg_cx.jpg', 'TV con schermo OLED, colori vibranti e neri profondi per una esperienza visiva immersiva.'),
    ('Borsa a mano Gucci GG Marmont', 'Moda', 'borsa_gucci_gg_marmont.jpg', 'Elegante borsa in pelle con il classico motivo GG e chiusura a doppia G.'),
    ('Guitarra acustica Martin D-28', 'Strumenti musicali', 'chitarra_acustica_martin_d28.jpg', 'Chitarra acustica di alta qualità con suono ricco e proiezione potente.'),
    ('Libro "Il signore degli anelli" di J.R.R. Tolkien', 'Libri', 'libro_il_signore_degli_anelli.jpg', 'Celebre romanzo fantasy che narra le avventure nella Terra di Mezzo.'),
    ('Aspirapolvere robot Roomba i7+', 'Elettrodomestici', 'aspirapolvere_robot_roomba_i7.jpg', 'Aspirapolvere robot con mappatura intelligente e sistema di pulizia avanzato.'),
    ('Occhiali da sole Ray-Ban Aviator', 'Accessori', 'occhiali_sole_rayban_aviator.jpg', 'Iconici occhiali da sole con montatura sottile e lenti a goccia.'),
    ('Gioco da tavolo Catan', 'Giochi', 'gioco_tavolo_catan.jpg', 'Classico gioco strategico in cui i giocatori competono per colonizzare l isola di Catan.'),
    ('Lavatrice Samsung Eco Bubble', 'Elettrodomestici', 'lavatrice_samsung_eco_bubble.jpg', 'Lavatrice con tecnologia Eco Bubble per un lavaggio efficace e delicato.'),
    ('Auricolari senza fili Apple AirPods Pro', 'Elettronica', 'auricolari_apple_airpods_pro.jpg', 'Auricolari wireless con cancellazione attiva del rumore e suono di alta qualità.'),
    ('Felpa con cappuccio Adidas Originals', 'Abbigliamento', 'felpa_adidas_originals.jpg', 'Comoda felpa con cappuccio e logo Adidas per un look sportivo.'),
    ('Telecomando universale Logitech Harmony Elite', 'Elettrodomestici', 'telecomando_universale_logitech_harmony_elite.jpg', 'Telecomando avanzato per il controllo di tutti i dispositivi audio e video.'),
    ('Set di pentole in acciaio inox Lagostina', 'Cucina', 'pentole_acciaio_inox_lagostina.jpg', 'Set di pentole di alta qualità in acciaio inox per cucinare con precisione e stile.');

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
       ('Fornitore B', '2023-05-21', 360, 'utente2@example.com'),
       ('Fornitore B', '2023-05-22', 3499, 'utente3@example.com');

-- Popolazione della tabella "visualizza"
INSERT INTO visualizza (email, codice_prodotto, data)
VALUES ('utente1@example.com', 1, 20090521153614),
       ('utente2@example.com', 2, 20090521153614),
       ('utente3@example.com', 3, 20050528183614);

-- Popolazione della tabella "vende"
INSERT INTO vende (codice_fornitore, codice_prodotto, prezzo, scnto)
VALUES (1, 1, 100, 0.1),
       (1, 2, 80, 0.05),
       (2, 2, 70, 0.15),
       (2, 3, 60, 0.2);

-- Popolazione della tabella "informazioni"
INSERT INTO informazioni (codice_ordine, codice_prodotto, nome, foto, categoria, quantita, prezzo_unitario, descrizione)
VALUES
    (1, 1, 'Smartphone Galaxy S21', 'smartphone_galaxy_s21.jpg', 'Elettronica', 2, 899, 'Potente smartphone con schermo AMOLED e fotocamera di alta qualità.'),
    (1, 2, 'Laptop ThinkPad X1 Carbon', 'laptop_thinkpad_x1_carbon.jpg', 'Informatica', 1, 1599, 'Notebook leggero e performante con display ad alta risoluzione.'),
    (2, 3, 'Scarpe da running Nike Air Zoom Pegasus 38', 'scarpe_running_nike_pegasus_38.jpg', 'Sport', 1, 129, 'Scarpe da corsa con ammortizzazione reattiva e comfort duraturo.'),
    (2, 4, 'Orologio automatico Rolex Submariner', 'orologio_rolex_submariner.jpg', 'Orologi', 1, 12000, 'Iconico orologio subacqueo con movimento automatico e impermeabilità fino a 300 metri.'),
    (3, 5, 'Macchina fotografica Canon EOS R5', 'macchina_fotografica_canon_eos_r5.jpg', 'Fotografia', 1, 3499, 'Fotocamera mirrorless ad alta risoluzione con registrazione video in 8K.');












