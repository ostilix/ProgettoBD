-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ChatMultiCanale
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ChatMultiCanale` ;

-- -----------------------------------------------------
-- Schema ChatMultiCanale
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ChatMultiCanale` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `ChatMultiCanale` ;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Utente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Utente` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Utente` (
  `Username` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  `Ruolo` ENUM('Amministratore', 'Capo progetto', 'Dipendente base') NOT NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Impiegato`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Impiegato` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Impiegato` (
  `CodiceFiscale` VARCHAR(20) NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Cognome` VARCHAR(45) NOT NULL,
  `Ruolo` ENUM('Amministratore', 'Capo progetto', 'Dipendente base') NOT NULL,
  `Username` VARCHAR(45) NULL,
  PRIMARY KEY (`CodiceFiscale`),
  INDEX `Username_idx` (`Username` ASC) VISIBLE,
  CONSTRAINT `Username`
    FOREIGN KEY (`Username`)
    REFERENCES `ChatMultiCanale`.`Utente` (`Username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Progetto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Progetto` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Progetto` (
  `CodiceProgetto` SMALLINT(4) NOT NULL AUTO_INCREMENT,
  `Nome` VARCHAR(45) NOT NULL,
  `DataScadenza` DATE NOT NULL,
  `CapoProgetto` VARCHAR(20) NULL,
  PRIMARY KEY (`CodiceProgetto`),
  INDEX `CapoProgetto_idx` (`CapoProgetto` ASC) VISIBLE,
  CONSTRAINT `CapoProgetto`
    FOREIGN KEY (`CapoProgetto`)
    REFERENCES `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Canale`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Canale` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Canale` (
  `CodiceCanale` SMALLINT(5) NOT NULL AUTO_INCREMENT,
  `Nome` VARCHAR(100) NOT NULL,
  `Tipo` ENUM('Pubblico', 'Privato') NOT NULL,
  `Progetto` SMALLINT(4) NOT NULL,
  PRIMARY KEY (`CodiceCanale`),
  INDEX `Progetto_idx` (`Progetto` ASC) VISIBLE,
  CONSTRAINT `Progetto`
    FOREIGN KEY (`Progetto`)
    REFERENCES `ChatMultiCanale`.`Progetto` (`CodiceProgetto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Messaggio`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Messaggio` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Messaggio` (
  `CodiceMSG` INT NOT NULL AUTO_INCREMENT,
  `Parole` VARCHAR(500) NOT NULL,
  `DataInvio` DATE NOT NULL,
  `OraInvio` TIME NOT NULL,
  `Mittente` VARCHAR(20) NOT NULL,
  `Destinatario` SMALLINT(5) NOT NULL,
  PRIMARY KEY (`CodiceMSG`),
  INDEX `Mittente_idx` (`Mittente` ASC) VISIBLE,
  INDEX `Destinatario_idx` (`Destinatario` ASC) VISIBLE,
  CONSTRAINT `Mittente`
    FOREIGN KEY (`Mittente`)
    REFERENCES `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `Destinatario`
    FOREIGN KEY (`Destinatario`)
    REFERENCES `ChatMultiCanale`.`Canale` (`CodiceCanale`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Appartiene`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Appartiene` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Appartiene` (
  `Impiegato` VARCHAR(20) NOT NULL,
  `Canale` SMALLINT(5) NOT NULL,
  PRIMARY KEY (`Impiegato`, `Canale`),
  INDEX `Canale_idx` (`Canale` ASC) VISIBLE,
  CONSTRAINT `Impiegato`
    FOREIGN KEY (`Impiegato`)
    REFERENCES `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `Canale`
    FOREIGN KEY (`Canale`)
    REFERENCES `ChatMultiCanale`.`Canale` (`CodiceCanale`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Partecipazione`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Partecipazione` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Partecipazione` (
  `ImpiegatoPartecipazione` VARCHAR(20) NOT NULL,
  `ProgettoPartecipazione` SMALLINT(4) NOT NULL,
  PRIMARY KEY (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`),
  INDEX `Progetto_idx` (`ProgettoPartecipazione` ASC) VISIBLE,
  CONSTRAINT `ImpiegatoPartecipazione`
    FOREIGN KEY (`ImpiegatoPartecipazione`)
    REFERENCES `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ProgettoPartecipazione`
    FOREIGN KEY (`ProgettoPartecipazione`)
    REFERENCES `ChatMultiCanale`.`Progetto` (`CodiceProgetto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `ChatMultiCanale`.`Riferimento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChatMultiCanale`.`Riferimento` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `ChatMultiCanale`.`Riferimento` (
  `CodiceMSGMittente` INT NOT NULL,
  `CodiceMSGRiferito` INT NOT NULL,
  `TipoRiferimento` ENUM('Pubblico', 'Privato') NULL,
  PRIMARY KEY (`CodiceMSGMittente`, `CodiceMSGRiferito`),
  INDEX `CodiceMSGDestinatario_idx` (`CodiceMSGRiferito` ASC) VISIBLE,
  CONSTRAINT `CodiceMSGMittente`
    FOREIGN KEY (`CodiceMSGMittente`)
    REFERENCES `ChatMultiCanale`.`Messaggio` (`CodiceMSG`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `CodiceMSGDestinatario`
    FOREIGN KEY (`CodiceMSGRiferito`)
    REFERENCES `ChatMultiCanale`.`Messaggio` (`CodiceMSG`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;
USE `ChatMultiCanale` ;

-- -----------------------------------------------------
-- procedure leggi_msg
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`leggi_msg`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `leggi_msg` (in var_codice_canale SMALLINT(5), in var_CF_impiegato VARCHAR(20))
BEGIN
    declare var_membro INT;
    declare var_capo_progetto INT;
    
	declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
    start transaction;
        
    select count(*) into var_membro
	from Appartiene
	where Canale = var_codice_canale and Impiegato= var_CF_impiegato;
    
    select count(*) into var_capo_progetto
    from Canale join Progetto on Canale.Progetto = Progetto.CodiceProgetto
    where Canale.CodiceCanale = var_codice_canale and Progetto.CapoProgetto = var_CF_impiegato;
        
	if var_membro = 0 and var_capo_progetto = 0 then
		signal sqlstate '45000'
		set message_text = 'Non è possibile visualizzare i messaggi di un canale di cui non si fa parte';
	end if;
    
    select Messaggio.DataInvio, Messaggio.OraInvio, Messaggio.CodiceMSG as CodiceMessaggio, concat(Impiegato.Nome, ' ', Impiegato.Cognome) AS Mittente, Messaggio.Parole as Testo,
			(select Parole
            from Messaggio
            where Messaggio.CodiceMSG = Riferimento.CodiceMSGRiferito
            ) as RispostaA
	from Messaggio
		left join Riferimento on Messaggio.CodiceMSG = Riferimento.CodiceMSGMittente
        join Impiegato on Messaggio.Mittente = Impiegato.CodiceFiscale
	where Messaggio.Destinatario = var_codice_canale
    order by Messaggio.DataInvio ASC, Messaggio.OraInvio ASC;
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure invia_msg
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`invia_msg`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `invia_msg` (in var_mittente VARCHAR(20), in var_destinatario SMALLINT(5), in var_parole VARCHAR(500))
BEGIN
	declare var_membro INT;
	declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;	
    start transaction;
    
    select count(*) into var_membro
    from Appartiene
    where Canale = var_destinatario and Impiegato = var_mittente;
    
    if var_membro = 0 then
        signal sqlstate '45000' 
		set message_text = 'Il mittente non è membro del canale';
    end if;
    
    insert into Messaggio(Parole, DataInvio, OraInvio, Mittente, Destinatario) values (var_parole, curdate(), curtime(), var_mittente, var_destinatario);
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure aggiungi_al_canale
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`aggiungi_al_canale`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `aggiungi_al_canale` (in var_codice_fiscale VARCHAR(20), in var_codice_canale SMALLINT(5))
BEGIN
	declare var_controllo INT UNSIGNED;
    
    declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;	
    start transaction;

    select count(*)
    into var_controllo
    from Impiegato
    join Partecipazione on Impiegato.CodiceFiscale = Partecipazione.ImpiegatoPartecipazione
    join Canale on Partecipazione.ProgettoPartecipazione = Canale.Progetto
    where Impiegato.CodiceFiscale = var_codice_fiscale
      and Canale.CodiceCanale = var_codice_canale;

    if var_controllo = 0 then
        signal sqlstate '45000'
		set message_text = 'Errore: Il dipendente non esiste, o non appartiene al progetto associato al canale';
    end if;

    insert into Appartiene (Impiegato, Canale) values (var_codice_fiscale, var_codice_canale);

    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure aggiungi_al_progetto
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`aggiungi_al_progetto`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `aggiungi_al_progetto` (in var_codice_fiscale VARCHAR(20), in var_codice_progetto SMALLINT(5))
BEGIN
	declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
    start transaction;
    
	if not exists(
        select 1
        from Impiegato
        where CodiceFiscale = var_codice_fiscale
    ) then
        signal sqlstate '45000' 
        set message_text = 'Dipendente non trovato';
    end if;

    if not exists(
        select 1
        from Progetto
        where CodiceProgetto = var_codice_progetto
    ) then
        signal sqlstate '45000' 
        set message_text = 'Progetto non trovato';
	end if;

    if exists (
        select 1
        from Partecipazione
        where ImpiegatoPartecipazione = var_codice_fiscale
          and ProgettoPartecipazione = var_codice_progetto
    ) then
        signal sqlstate '45000' 
        set message_text = 'Dipendente già assegnato al progetto';
    end if;

    insert into Partecipazione (ImpiegatoPartecipazione, ProgettoPartecipazione) values (var_codice_fiscale, var_codice_progetto);

    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure assegna_capo
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`assegna_capo`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `assegna_capo` (in var_capo_progetto VARCHAR(20), in var_codice_progetto SMALLINT(5))
BEGIN
	declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
    start transaction;
    
    if exists(
		select 1
        from Impiegato
        where CodiceFiscale = var_capo_progetto and Ruolo = 'Capo progetto'
    ) then 
		update Progetto
        set CapoProgetto = var_capo_progetto
        where CodiceProgetto = var_codice_progetto;
		insert into Partecipazione (ImpiegatoPartecipazione, ProgettoPartecipazione) values (var_capo_progetto, var_codice_progetto);
	else
		signal sqlstate '45000'
        set message_text = 'L\'impiegato non esiste o non ha il ruolo di capo progetto';
	end if;
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure crea_canale_pubblico
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`crea_canale_pubblico`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `crea_canale_pubblico` (in var_nome VARCHAR(45), in var_codice_progetto SMALLINT(4))
BEGIN
	declare var_progetto INT UNSIGNED;
    declare var_capo_progetto VARCHAR(20);
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
    start transaction;
    
    select count(*) into var_progetto
    from Progetto
    where CodiceProgetto = var_codice_progetto;
    
    if var_progetto = 0 then
        signal sqlstate '45000'
        set message_text = 'Impossibile creare il canale poichè il progetto non esiste';
    else
		select CapoProgetto into var_capo_progetto 
		from Progetto
		where CodiceProgetto = var_codice_progetto;
		
		insert into Canale(Nome, Tipo, Progetto) values (var_nome, 'Pubblico', var_codice_progetto);
		insert into Appartiene(Impiegato, Canale) values (var_capo_progetto, last_insert_id());
			
		commit;
	end if;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure crea_progetto
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`crea_progetto`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `crea_progetto` (in var_nome_progetto VARCHAR(45), in var_data_scadenza DATE)
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
    start transaction;
    
	insert into Progetto(Nome, DataScadenza, CapoProgetto) values (var_nome_progetto, var_data_scadenza, NULL);
	commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure login
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`login`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `login` (in var_username varchar(45), in var_pass varchar(45), out var_role INT, out var_CodiceFiscale varchar(20))
BEGIN
	declare var_user_role ENUM('Amministratore','Capo progetto', 'Dipendente base');
    
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
	set transaction read only;
	start transaction;
    
		select Ruolo
        from Utente
		where Username = var_username and Password = md5(var_pass)
		into var_user_role;
			
		if var_user_role = 'Amministratore' then
			set var_role = 1;
            select CodiceFiscale 
            from Impiegato
            where Username = var_username
            into var_CodiceFiscale;
		elseif var_user_role = 'Capo progetto' then
			set var_role = 2;
            select CodiceFiscale 
            from Impiegato
            where Username = var_username
            into var_CodiceFiscale;
		elseif var_user_role = 'Dipendente base' then
			set var_role = 3;
            select CodiceFiscale 
            from Impiegato
            where Username = var_username
            into var_CodiceFiscale;
		else
			set var_role = 0;
		end if;
	commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure nuovo_impiegato
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`nuovo_impiegato`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `nuovo_impiegato` (in var_impiegato VARCHAR(20), in var_nome VARCHAR(45), in var_cognome VARCHAR(45), in var_ruolo ENUM('Amministratore', 'Capo progetto', 'Dipendente base'), in var_username VARCHAR(45), in var_password VARCHAR(45))
BEGIN
	declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read uncommitted;
    start transaction;
    
    insert into Utente(Username, Password, Ruolo) values (var_username, md5(var_password), var_ruolo);
    insert into Impiegato(CodiceFiscale, Nome, Cognome, Ruolo, Username) values (var_impiegato, var_nome, var_cognome, var_ruolo, var_username);
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure rispondi_a_msg
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`rispondi_a_msg`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `rispondi_a_msg` (in var_codice_msg_riferito INT, in var_CF_mittente VARCHAR(20), in var_testo VARCHAR(500), in var_tipo ENUM('Pubblico','Privato'))
BEGIN
	declare var_destinatario_originale SMALLINT(5);
    declare var_mittente_originale VARCHAR(20);
    declare var_codice_canale_privato SMALLINT(5);
    declare var_codice_nuovo_canale SMALLINT(5);
    declare var_nome_canale_privato VARCHAR(45);
    declare var_codice_progetto SMALLINT(5);
    declare var_nome_mittente VARCHAR(45);
    declare var_cognome_mittente VARCHAR(45);
    declare var_nome_destinatario VARCHAR(45);
    declare var_cognome_destinatario VARCHAR(45);
	
	declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
    start transaction;
    
    select Mittente, Destinatario into var_mittente_originale, var_destinatario_originale
    from Messaggio
    where CodiceMSG = var_codice_msg_riferito;
    
    select Progetto into var_codice_progetto
    from Canale
    where CodiceCanale = var_destinatario_originale;
    
    if var_codice_progetto is NULL then
		signal sqlstate '45000' set message_text = 'Impossibile trovare il progetto per il canale.';
	end if;
    
    if var_tipo = 'Pubblico' then
		insert into Messaggio(Parole, DataInvio, OraInvio, Mittente, Destinatario) values (var_testo, curdate(), curtime(), var_CF_mittente, var_destinatario_originale);
        insert into Riferimento(CodiceMSGMittente, CodiceMSGRiferito, TipoRiferimento) values (last_insert_id(), var_codice_msg_riferito, 'Pubblico');
	elseif var_tipo = 'Privato' then
		select Canale.CodiceCanale into var_codice_canale_privato
        from Canale
        join Appartiene as A1 on Canale.CodiceCanale= A1.Canale
        join Appartiene as A2 on Canale.CodiceCanale = A2.Canale
        where Canale.Tipo = 'Privato' and ((A1.Impiegato = var_CF_mittente and A2.Impiegato = var_mittente_originale) or (A1.Impiegato = var_mittente_originale and A2.Impiegato = var_CF_mittente))
        limit 1;
        
        if var_codice_canale_privato is not null then
			insert into Messaggio(Parole, DataInvio, OraInvio, Mittente, Destinatario) values (var_testo, curdate(), curtime(), var_CF_mittente, var_codice_canale_privato);
            insert into Riferimento(CodiceMSGMittente, CodiceMSGRiferito, TipoRiferimento) values (last_insert_id(), var_codice_msg_riferito, 'Privato');
		else 
			select Nome, Cognome into var_nome_mittente, var_cognome_mittente
            from Impiegato
            where CodiceFiscale = var_CF_mittente;

            select Nome, Cognome into var_nome_destinatario, var_cognome_destinatario
            from Impiegato
            where CodiceFiscale = var_mittente_originale;
            
			set var_nome_canale_privato = concat_ws('_', var_nome_mittente, var_cognome_mittente, var_nome_destinatario, var_cognome_destinatario, 'privato');
            
            insert into Canale(Nome, Tipo, Progetto) values (var_nome_canale_privato, 'Privato', var_codice_progetto);
            
            set var_codice_nuovo_canale = last_insert_id();
            
            insert into Appartiene(Impiegato, Canale) values (var_CF_mittente, var_codice_nuovo_canale), (var_mittente_originale, var_codice_nuovo_canale);
            
            insert into Messaggio(Parole, DataInvio, OraInvio, Mittente, Destinatario) values (var_testo, curdate(), curtime(), var_CF_mittente, var_codice_nuovo_canale);
            
            insert into Riferimento(CodiceMSGMittente, CodiceMSGRiferito, TipoRiferimento) values (last_insert_id(), var_codice_msg_riferito, 'Privato');
		end if;
	end if;
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure visualizza_canali
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`visualizza_canali`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `visualizza_canali` (in var_CF_impiegato VARCHAR(20))
BEGIN
	declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;
	set transaction read only;
    start transaction;
    
    if exists(
		select 1
        from Progetto
        where CapoProgetto = var_CF_impiegato
    )then 
		select Canale.CodiceCanale, Canale.Nome as NomeCanale, Canale.Tipo, Progetto.CodiceProgetto, Progetto.Nome as NomeProgetto
        from Canale join Progetto on Canale.Progetto = Progetto.CodiceProgetto
        where Progetto.CapoProgetto = var_CF_impiegato or 
			(Canale.Tipo = 'Privato' and Canale.Progetto in
				(select Progetto2.CodiceProgetto
                from Progetto as Progetto2
                where Progetto2.CapoProgetto = var_CF_impiegato));
    else
		select Canale.CodiceCanale, Canale.Nome as NomeCanale, Canale.Tipo, Progetto.CodiceProgetto, Progetto.Nome as NomeProgetto
		from Canale
		join Progetto on Canale.Progetto = Progetto.CodiceProgetto
		join Appartiene on Canale.CodiceCanale = Appartiene.Canale
		where Appartiene.Impiegato = var_CF_impiegato;
	end if;

    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure visualizza_membri_canale
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`visualizza_membri_canale`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `visualizza_membri_canale` (in var_codice_canale SMALLINT(5))
BEGIN
	declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;	
    set transaction read only;
    start transaction;
    
    select Canale.Nome as NomeCanale, Impiegato.CodiceFiscale, Impiegato.Nome, Impiegato.Cognome, Impiegato.Ruolo
    from Impiegato
    join Appartiene on Impiegato.CodiceFiscale = Appartiene.Impiegato
    join Canale on Appartiene.Canale = Canale.CodiceCanale
    where Appartiene.Canale = var_codice_canale;
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure visualizza_impiegati
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`visualizza_impiegati`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `visualizza_impiegati` ()
BEGIN
	
	declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    
    set transaction isolation level read committed;
    set transaction read only;
    start transaction;
    
    
	select CodiceFiscale, Nome, Cognome, Ruolo
	from Impiegato
    order by Ruolo ASC;
    
    commit;    
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure visualizza_progetti
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`visualizza_progetti`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `visualizza_progetti` (in var_codice_fiscale VARCHAR(20))
BEGIN
	declare var_ruolo VARCHAR(20);
	declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;	
    set transaction read only;
    start transaction;

    select Ruolo into var_ruolo
    from Impiegato
    where CodiceFiscale = var_codice_fiscale;
    
    if var_ruolo = 'Amministratore' then
		 select CodiceProgetto, Nome as NomeProgetto, DataScadenza, CapoProgetto
         from Progetto
         order by CodiceProgetto;
	else
		select CodiceProgetto, Nome as NomeProgetto, DataScadenza, CapoProgetto
		from Progetto
        where CapoProgetto = var_codice_fiscale
		order by CodiceProgetto;
	end if;
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure visualizza_membri_progetto
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`visualizza_membri_progetto`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `visualizza_membri_progetto` (in var_codice_progetto SMALLINT(5))
BEGIN
	declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;	
    set transaction read only;
    start transaction;
    
    select Progetto.Nome as NomeProgetto, Impiegato.CodiceFiscale, Impiegato.Nome, Impiegato.Cognome, Impiegato.Ruolo
    from Impiegato
    join Partecipazione on Impiegato.CodiceFiscale = Partecipazione.ImpiegatoPartecipazione
    join Progetto on Partecipazione.ProgettoPartecipazione = Progetto.CodiceProgetto
    where Partecipazione.ProgettoPartecipazione = var_codice_progetto;
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure visualizza_assenti_canale
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`visualizza_assenti_canale`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `visualizza_assenti_canale` (in var_codice_canale SMALLINT(5))
BEGIN
	declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;
	set transaction read only;
    start transaction;
    
    select Progetto.Nome as NomeProgetto, Impiegato.CodiceFiscale, Impiegato.Nome, Impiegato.Cognome, Impiegato.Ruolo
    from Impiegato
    join Partecipazione on Impiegato.CodiceFiscale = Partecipazione.ImpiegatoPartecipazione
    join Canale on Canale.Progetto = Partecipazione.ProgettoPartecipazione
    join Progetto on Progetto.CodiceProgetto = Canale.Progetto
    where Canale.CodiceCanale = var_codice_canale
		and Impiegato.CodiceFiscale not in 
			(select Appartiene.Impiegato
            from Appartiene
            where Appartiene.Canale = var_codice_canale);
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;

-- -----------------------------------------------------
-- procedure visualizza_assenti_progetto
-- -----------------------------------------------------

USE `ChatMultiCanale`;
DROP procedure IF EXISTS `ChatMultiCanale`.`visualizza_assenti_progetto`;
SHOW WARNINGS;

DELIMITER $$
USE `ChatMultiCanale`$$
CREATE PROCEDURE `visualizza_assenti_progetto` (in var_codice_progetto SMALLINT(5))
BEGIN
	declare exit handler for sqlexception
    begin
        rollback;
        resignal;
    end;

    set transaction isolation level read committed;	
    set transaction read only;
    start transaction;
    
    select Impiegato.CodiceFiscale, Impiegato.Nome, Impiegato.Cognome, Impiegato.Ruolo
    from Impiegato
    where Impiegato.Ruolo = 'Dipendente base' 
		and Impiegato.CodiceFiscale not in 
			(select Partecipazione.ImpiegatoPartecipazione
            from Partecipazione
            where Partecipazione.ProgettoPartecipazione = var_codice_progetto);
    
    commit;
END$$

DELIMITER ;
SHOW WARNINGS;
USE `ChatMultiCanale`;

DELIMITER $$

USE `ChatMultiCanale`$$
DROP TRIGGER IF EXISTS `ChatMultiCanale`.`Impiegato_BEFORE_INSERT` $$
SHOW WARNINGS$$
USE `ChatMultiCanale`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ChatMultiCanale`.`Impiegato_BEFORE_INSERT` BEFORE INSERT ON `Impiegato` FOR EACH ROW
BEGIN
	IF NOT NEW.Nome REGEXP '^[a-zA-Z]+$' THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Il nome contiene numeri e/o caratteri speciali.';
	ELSEIF NOT NEW.Cognome REGEXP '^[a-zA-Z]+$' THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Il cognome contiene numeri e/o caratteri speciali.';
	ELSEIF NOT NEW.CodiceFiscale REGEXP '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$' THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Il codice fiscale non è valido.';
	END IF;
END$$

SHOW WARNINGS$$

USE `ChatMultiCanale`$$
DROP TRIGGER IF EXISTS `ChatMultiCanale`.`Progetto_BEFORE_INSERT` $$
SHOW WARNINGS$$
USE `ChatMultiCanale`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ChatMultiCanale`.`Progetto_BEFORE_INSERT` BEFORE INSERT ON `Progetto` FOR EACH ROW
BEGIN
	IF EXISTS (SELECT 1 FROM Progetto WHERE Nome = NEW.Nome) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Nome progetto già esistente';
	ELSEIF NEW.DataScadenza <= CURDATE() THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'La data di scadenza deve essere successiva alla data odierna';
	END IF;
END$$

SHOW WARNINGS$$

USE `ChatMultiCanale`$$
DROP TRIGGER IF EXISTS `ChatMultiCanale`.`Canale_BEFORE_INSERT` $$
SHOW WARNINGS$$
USE `ChatMultiCanale`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ChatMultiCanale`.`Canale_BEFORE_INSERT` BEFORE INSERT ON `Canale` FOR EACH ROW
BEGIN
	IF EXISTS (
		SELECT 1 
        FROM Canale 
        WHERE Nome = NEW.Nome
	) THEN
		SIGNAL SQLSTATE '45000' 
		SET MESSAGE_TEXT = 'Nome canale già esistente';
	END IF;
END$$

SHOW WARNINGS$$

USE `ChatMultiCanale`$$
DROP TRIGGER IF EXISTS `ChatMultiCanale`.`Messaggio_BEFORE_INSERT` $$
SHOW WARNINGS$$
USE `ChatMultiCanale`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ChatMultiCanale`.`Messaggio_BEFORE_INSERT` BEFORE INSERT ON `Messaggio` FOR EACH ROW
BEGIN
	IF LENGTH(NEW.Parole) > 1000 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Messaggio troppo lungo';
    END IF;

END$$

SHOW WARNINGS$$

DELIMITER ;
SET SQL_MODE = '';
DROP USER IF EXISTS login;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
SHOW WARNINGS;
CREATE USER 'login' IDENTIFIED BY 'login';

GRANT EXECUTE ON procedure `ChatMultiCanale`.`login` TO 'login';
SHOW WARNINGS;
SET SQL_MODE = '';
DROP USER IF EXISTS amministratore;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
SHOW WARNINGS;
CREATE USER 'amministratore' IDENTIFIED BY 'amministratore';

GRANT EXECUTE ON procedure `ChatMultiCanale`.`assegna_capo` TO 'amministratore';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`crea_progetto` TO 'amministratore';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`nuovo_impiegato` TO 'amministratore';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_impiegati` TO 'amministratore';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_progetti` TO 'amministratore';
SHOW WARNINGS;
SET SQL_MODE = '';
DROP USER IF EXISTS capo_progetto;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
SHOW WARNINGS;
CREATE USER 'capo_progetto' IDENTIFIED BY 'capo_progetto';

GRANT EXECUTE ON procedure `ChatMultiCanale`.`leggi_msg` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`aggiungi_al_canale` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`aggiungi_al_progetto` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`crea_canale_pubblico` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`invia_msg` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`rispondi_a_msg` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_canali` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_membri_canale` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_impiegati` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_progetti` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_membri_progetto` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_assenti_canale` TO 'capo_progetto';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_assenti_progetto` TO 'capo_progetto';
SHOW WARNINGS;
SET SQL_MODE = '';
DROP USER IF EXISTS dipendente_base;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
SHOW WARNINGS;
CREATE USER 'dipendente_base' IDENTIFIED BY 'dipendente_base';

GRANT EXECUTE ON procedure `ChatMultiCanale`.`leggi_msg` TO 'dipendente_base';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`invia_msg` TO 'dipendente_base';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`rispondi_a_msg` TO 'dipendente_base';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_canali` TO 'dipendente_base';
GRANT EXECUTE ON procedure `ChatMultiCanale`.`visualizza_membri_canale` TO 'dipendente_base';
SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Utente`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('marco.rossi', '823da4223e46ec671a10ea13d7823534', 'Capo progetto');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('luigi.verdi', '823da4223e46ec671a10ea13d7823534', 'Dipendente base');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('carlo.bianchi', '823da4223e46ec671a10ea13d7823534', 'Capo progetto');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('francesca.gennari', '823da4223e46ec671a10ea13d7823534', 'Dipendente base');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('laura.battisti', '823da4223e46ec671a10ea13d7823534', 'Dipendente base');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('manuela.porta', '823da4223e46ec671a10ea13d7823534', 'Dipendente base');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('gabriele.sartori', '823da4223e46ec671a10ea13d7823534', 'Dipendente base');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('lucia.montanari', '823da4223e46ec671a10ea13d7823534', 'Dipendente base');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('ludovico.conti', '823da4223e46ec671a10ea13d7823534', 'Capo progetto');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('marica.ferri', '823da4223e46ec671a10ea13d7823534', 'Dipendente base');
INSERT INTO `ChatMultiCanale`.`Utente` (`Username`, `Password`, `Ruolo`) VALUES ('ginevra.ferrari', '823da4223e46ec671a10ea13d7823534', 'Amministratore');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Impiegato`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('RSSMRA85M01H501Z', 'Marco', 'Rossi', 'Capo progetto', 'marco.rossi');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('VRDLGI90F01H501C', 'Luigi', 'Verdi', 'Dipendente base', 'luigi.verdi');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('BNCCRL88M15H501Q', 'Carlo', 'Bianchi', 'Capo progetto', 'carlo.bianchi');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('GNNFRN92F20H501N', 'Francesca', 'Gennari', 'Dipendente base', 'francesca.gennari');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('BTTLRA87M10H501P', 'Laura', 'Battisti', 'Dipendente base', 'laura.battisti');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('PRTMNL89F30H501K', 'Manuela', 'Porta', 'Dipendente base', 'manuela.porta');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('SRTGMR91M11H501Y', 'Gabriele', 'Sartori', 'Dipendente base', 'gabriele.sartori');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('MNTLUC93F25H501L', 'Lucia', 'Montanari', 'Dipendente base', 'lucia.montanari');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('CNTLDS86M12H501R', 'Ludovico', 'Conti', 'Capo progetto', 'ludovico.conti');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('FRRMRC94M18H501T', 'Marica', 'Ferri', 'Dipendente base', 'marica.ferri');
INSERT INTO `ChatMultiCanale`.`Impiegato` (`CodiceFiscale`, `Nome`, `Cognome`, `Ruolo`, `Username`) VALUES ('FRNGNE92T20A123B', 'Ginevra', 'Ferrari', 'Amministratore', 'ginevra.ferrari');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Progetto`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Progetto` (`CodiceProgetto`, `Nome`, `DataScadenza`, `CapoProgetto`) VALUES (1, 'Progetto1', '2026-10-10', 'RSSMRA85M01H501Z');
INSERT INTO `ChatMultiCanale`.`Progetto` (`CodiceProgetto`, `Nome`, `DataScadenza`, `CapoProgetto`) VALUES (2, 'Progetto2', '2025-10-05', 'BNCCRL88M15H501Q');
INSERT INTO `ChatMultiCanale`.`Progetto` (`CodiceProgetto`, `Nome`, `DataScadenza`, `CapoProgetto`) VALUES (3, 'Progetto3', '2025-09-09', 'CNTLDS86M12H501R');

COMMIT;


-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Canale`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Canale` (`CodiceCanale`, `Nome`, `Tipo`, `Progetto`) VALUES (1, 'CanaleProgetto1', 'Pubblico', 1);
INSERT INTO `ChatMultiCanale`.`Canale` (`CodiceCanale`, `Nome`, `Tipo`, `Progetto`) VALUES (2, 'CanaleProgetto2', 'Pubblico', 2);
INSERT INTO `ChatMultiCanale`.`Canale` (`CodiceCanale`, `Nome`, `Tipo`, `Progetto`) VALUES (3, 'CanaleProgetto3', 'Pubblico', 3);
INSERT INTO `ChatMultiCanale`.`Canale` (`CodiceCanale`, `Nome`, `Tipo`, `Progetto`) VALUES (4, 'Marco_Rossi_Luigi_Verdi_privato', 'Privato', 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Messaggio`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Messaggio` (`CodiceMSG`, `Parole`, `DataInvio`, `OraInvio`, `Mittente`, `Destinatario`) VALUES (1, 'Primo messaggio inviato', '2025-01-16', '00:00:00', 'RSSMRA85M01H501Z', 1);
INSERT INTO `ChatMultiCanale`.`Messaggio` (`CodiceMSG`, `Parole`, `DataInvio`, `OraInvio`, `Mittente`, `Destinatario`) VALUES (2, 'Secondo messaggio inviato', '2025-01-16', '00:02:00', 'VRDLGI90F01H501C', 1);
INSERT INTO `ChatMultiCanale`.`Messaggio` (`CodiceMSG`, `Parole`, `DataInvio`, `OraInvio`, `Mittente`, `Destinatario`) VALUES (3, 'Risposta Pubblica al secondo messaggio', '2025-01-16', '00:05:00', 'RSSMRA85M01H501Z', 1);
INSERT INTO `ChatMultiCanale`.`Messaggio` (`CodiceMSG`, `Parole`, `DataInvio`, `OraInvio`, `Mittente`, `Destinatario`) VALUES (4, 'Risposta privato al terzo messaggio', '2025-01-16', '00:08:00', 'VRDLGI90F01H501C', 4);
INSERT INTO `ChatMultiCanale`.`Messaggio` (`CodiceMSG`, `Parole`, `DataInvio`, `OraInvio`, `Mittente`, `Destinatario`) VALUES (5, 'Ciao come stai?', '2025-01-16', '00:10:00', 'RSSMRA85M01H501Z', 4);
INSERT INTO `ChatMultiCanale`.`Messaggio` (`CodiceMSG`, `Parole`, `DataInvio`, `OraInvio`, `Mittente`, `Destinatario`) VALUES (6, 'Bene tu?', '2025-01-16', '00:10:00', 'VRDLGI90F01H501C', 4);

COMMIT;


-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Appartiene`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('RSSMRA85M01H501Z', 1);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('BNCCRL88M15H501Q', 2);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('CNTLDS86M12H501R', 3);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('VRDLGI90F01H501C', 1);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('GNNFRN92F20H501N', 1);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('BTTLRA87M10H501P', 2);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('PRTMNL89F30H501K', 2);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('SRTGMR91M11H501Y', 3);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('MNTLUC93F25H501L', 3);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('FRRMRC94M18H501T', 3);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('VRDLGI90F01H501C', 4);
INSERT INTO `ChatMultiCanale`.`Appartiene` (`Impiegato`, `Canale`) VALUES ('RSSMRA85M01H501Z', 4);

COMMIT;


-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Partecipazione`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('RSSMRA85M01H501Z', 1);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('BNCCRL88M15H501Q', 2);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('CNTLDS86M12H501R', 3);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('VRDLGI90F01H501C', 1);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('GNNFRN92F20H501N', 1);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('BTTLRA87M10H501P', 2);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('PRTMNL89F30H501K', 2);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('SRTGMR91M11H501Y', 3);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('MNTLUC93F25H501L', 3);
INSERT INTO `ChatMultiCanale`.`Partecipazione` (`ImpiegatoPartecipazione`, `ProgettoPartecipazione`) VALUES ('FRRMRC94M18H501T', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `ChatMultiCanale`.`Riferimento`
-- -----------------------------------------------------
START TRANSACTION;
USE `ChatMultiCanale`;
INSERT INTO `ChatMultiCanale`.`Riferimento` (`CodiceMSGMittente`, `CodiceMSGRiferito`, `TipoRiferimento`) VALUES (3, 2, 'Pubblico');
INSERT INTO `ChatMultiCanale`.`Riferimento` (`CodiceMSGMittente`, `CodiceMSGRiferito`, `TipoRiferimento`) VALUES (4, 3, 'Privato');
INSERT INTO `ChatMultiCanale`.`Riferimento` (`CodiceMSGMittente`, `CodiceMSGRiferito`, `TipoRiferimento`) VALUES (6, 5, 'Pubblico');

COMMIT;

-- begin attached script 'script'
SET GLOBAL event_scheduler = ON;

DELIMITER //
CREATE EVENT IF NOT EXISTS rimozione_messaggi_vecchi
ON SCHEDULE EVERY 2 WEEK
STARTS CURRENT_TIMESTAMP + INTERVAL 12 MONTH
ON COMPLETION PRESERVE
COMMENT 'Rimozione dei messaggi relativi a canali associati a progetti scaduti da più di 12 mesi'
DO
BEGIN
    DELETE FROM Messaggio
    WHERE Destinatario IN (
        SELECT CodiceCanale
        FROM Canale
        WHERE Progetto IN (
			SELECT CodiceProgetto
            FROM Progetto
            WHERE DataScadenza < DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
		)
	);
END
//

CREATE EVENT IF NOT EXISTS rimozione_canali_vecchi
ON SCHEDULE EVERY 2 WEEK
STARTS CURRENT_TIMESTAMP
ON COMPLETION PRESERVE
COMMENT 'Rimozione dei canali relativi a progetti scaduti da più di 12 mesi'
DO
BEGIN
    DELETE FROM Canale
    WHERE Progetto IN (
        SELECT CodiceProgetto
        FROM Progetto
        WHERE DataScadenza < DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
    );
END;
//
-- end attached script 'script'
