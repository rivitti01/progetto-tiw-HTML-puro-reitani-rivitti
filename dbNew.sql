drop schema if exists tiw;
create schema tiw;

CREATE TABLE IF NOT EXISTS tiw.fornitore(
    codice_fornitore int primary key auto_increment,
    nome_fornitore VARCHAR(255) not null,
    valutazione int not null check (valutazione > 0 and valutazione < 6),
    soglia int not null check (soglia > 0)
);
CREATE TABLE IF NOT EXISTS tiw.utente(
    email VARCHAR(255) primary key,
    nome VARCHAR(255) not null,
    cognome VARCHAR(255) not null,
    indirizzo VARCHAR(255) not null,
    password VARCHAR(255) not null
);

CREATE TABLE IF NOT EXISTS tiw.prodotto(
    codice_prodotto int primary key auto_increment,
    nome_prodotto VARCHAR(255) not null,
    categoria VARCHAR(255) not null,
    foto VARCHAR(255) not null,
    descrizione VARCHAR(255) not null
);
CREATE TABLE IF NOT EXISTS tiw.ordini (
    codice_ordine INT PRIMARY KEY AUTO_INCREMENT,
    nome_fornitore VARCHAR(255),
    data_spedizione DATE NOT NULL,
    indirizzo_spedizione VARCHAR(255) NOT NULL,
    prezzo_totale INT NOT NULL CHECK (prezzo_totale > 0),
    email VARCHAR(255),
    FOREIGN KEY (email) REFERENCES utente(email)
);
CREATE TABLE IF NOT EXISTS tiw.fasce(
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
create table IF NOT EXISTS tiw.visualizza(
    email VARCHAR(255),
    codice_prodotto int,
    data timestamp default current_timestamp,
    primary key (email, codice_prodotto),
    foreign key (email) references utente(email),
    foreign key (codice_prodotto) references prodotto(codice_prodotto)
);
create table IF NOT EXISTS tiw.vende(
    codice_fornitore int not null,
    codice_prodotto int not null,
    prezzo int not null check (prezzo > 0),
    sconto float not null check (sconto >= 0 and sconto < 1),
    primary key (codice_fornitore, codice_prodotto),
    foreign key (codice_fornitore) references fornitore(codice_fornitore),
    foreign key (codice_prodotto) references prodotto(codice_prodotto)
);

create table IF NOT EXISTS tiw.informazioni(
    codice_ordine int not null,
    codice_prodotto int not null,
    nome VARCHAR(255) not null,
    foto VARCHAR(255) not null,
    quantita int not null check (quantita > 0),
    prezzo_unitario int not null check (prezzo_unitario > 0),
    primary key (codice_ordine, codice_prodotto),
    foreign key (codice_ordine) references ordini(codice_ordine)
);







-- Popolazione della tabella "fornitore"
INSERT INTO tiw.fornitore (nome_fornitore, valutazione, soglia)
VALUES ('Fornitore A', 4, 20),
       ('Fornitore B', 5, 15),
       ('Fornitore C', 3, 10);

-- Popolazione della tabella "utente"
INSERT INTO tiw.utente (email, nome, cognome, indirizzo, password)
VALUES ('utente1@example.com', 'Mario', 'Rossi', 'Via Roma 1', 'password1'),
       ('utente2@example.com', 'Laura', 'Verdi', 'Via Milano 2', 'password2'),
       ('utente3@example.com', 'Giovanni', 'Bianchi', 'Via Napoli 3', 'password3');

-- Popolazione della tabella "prodotto"
INSERT INTO tiw.prodotto (nome_prodotto, categoria, foto, descrizione)
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
INSERT INTO tiw.fasce (codice_fornitore, min, max, prezzo)
VALUES (1, 1, 10, 30),
       (1, 11, 19, 20),
       (2, 1, 5, 50),
       (2, 6, 10, 45),
       (2, 11, 14, 40),
       (3,1,4,25),
       (3,5,9,15);

-- Popolazione della tabella "ordini"
INSERT INTO tiw.ordini (nome_fornitore, data_spedizione,indirizzo_spedizione, prezzo_totale, email)
VALUES ('Fornitore A', '2023-05-20','Via Roma 1', 440, 'utente1@example.com'),
       ('Fornitore B', '2023-05-21','Via Milano 2', 360, 'utente2@example.com'),
       ('Fornitore B', '2023-05-22','Via Napoli 3', 3499, 'utente3@example.com');

-- Popolazione della tabella "visualizza"
INSERT INTO tiw.visualizza (email, codice_prodotto, data)
VALUES ('utente1@example.com', 1, 20090521153614),
       ('utente1@example.com', 2, 20090522153614),
       ('utente1@example.com', 3, 20090523153614),
       ('utente1@example.com', 5, 20090524153614),
       ('utente1@example.com', 6, 20090525153614),
       ('utente1@example.com', 8, 20090526153614),
       ('utente1@example.com', 9, 20090527153614),
       ('utente2@example.com', 2, 20090528153614),
       ('utente3@example.com', 3, 20050528183614);

-- Popolazione della tabella "vende"
INSERT INTO tiw.vende (codice_fornitore, codice_prodotto, prezzo, sconto)
VALUES (1, 1, 100, 0.1),
       (1, 2, 80, 0.05),
       (1,4,15000,0),
       (1,7,620,0),
       (1,10,180,0),
       (1,12,30,0),
       (1,15,45,0),
       (1,17,78,0),
       (2, 2, 70, 0.15),
       (2, 3, 60, 0.2),
       (2,6,230,0),
       (2,8,650,0.2),
       (2,10,210,0),
       (2,11,90,0),
       (2,13,220,0.1),
       (2,16,30,0),
       (2,17,76,0),
       (3,5,1000,0.15),
       (3,6,400,0),
       (3,9,23,0),
       (3,10,200,0.1),
       (3,11,100,0),
       (3,12,25,0),
       (3,13,200,0);

-- Popolazione della tabella "informazioni"
INSERT INTO tiw.informazioni (codice_ordine, codice_prodotto, nome, foto, quantita, prezzo_unitario)
VALUES
    (1, 1, 'Smartphone Galaxy S21', 'smartphone_galaxy_s21.jpg', 2, 899),
    (1, 2, 'Laptop ThinkPad X1 Carbon', 'laptop_thinkpad_x1_carbon.jpg', 1, 1599),
    (2, 3, 'Scarpe da running Nike Air Zoom Pegasus 38', 'scarpe_running_nike_pegasus_38.jpg', 1, 129),
    (2, 4, 'Orologio automatico Rolex Submariner', 'orologio_rolex_submariner.jpg', 1, 12000),
    (3, 5, 'Macchina fotografica Canon EOS R5', 'macchina_fotografica_canon_eos_r5.jpg', 1, 3499);












