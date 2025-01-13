CREATE TABLE IF NOT EXISTS vurdering
(
    id VARCHAR(100),
    fnr VARCHAR(100),
    fom date,
    tom date,
    status VARCHAR(10)

    );

CREATE  INDEX IF NOT EXISTS fnr_idx ON vurdering (fnr);

CREATE TABLE IF NOT EXISTS konklusjoner
(
    id VARCHAR(100),
    regel_id VARCHAR(100)
    beskrivelse VARCHAR(100)

    );

CREATE  INDEX IF NOT EXISTS id_idx ON konklusjoner (id);