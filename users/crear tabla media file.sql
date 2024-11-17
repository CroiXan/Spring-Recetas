USE recetas;
DROP TABLE IF EXISTS media_file;

CREATE TABLE media_file (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_receta BIGINT DEFAULT 0,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(255),
    file_size BIGINT NOT NULL,
    file_data LONGBLOB NOT NULL,
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

