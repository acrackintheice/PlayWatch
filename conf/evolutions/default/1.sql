# Users schema

# --- !Ups

CREATE TABLE `todos`.`todo` (
  `idTodo` INT NOT NULL AUTO_INCREMENT,
  `text` TINYTEXT NOT NULL,
  PRIMARY KEY (`idtodo`));

# --- !Downs

DROP TABLE todo;