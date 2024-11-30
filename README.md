# CY Virtuel SpringBoot
CY Virtuel est le projet noté de Java EE de l'année 2024-2025 de CY Tech, réalisé par:
- Gaétan Retel
- Julien Guyot
- Matéo Lopez
- Guillaume Androny
- Ulrich Sautreuil

## Installation

### Cloner le projet

Vous pouvez cloner le projet depuis `git` via la commande suivante:

```sh
git clone https://github.com/na-teag/JEE-project-SpringBoot
```

### Installer Maven (Linux)

Si vous avez déjà Maven installé sur votre système, vous pouvez passer cette étape, car vous avez déjà les dépendances
nécessaires.

Maven est l'outil qui permet d'installer les dépendances du projet. 
Pour les installer sous Ubuntu (et distributions similaires), il faut exécuter :

```sh
sudo apt install maven
```

### Environnement

Le projet utilise Hibernate, les variables sont stockées dans le fichier `src/main/resources/application.properties`.

Le projet utilise Mysql, pour l'installer il faudra taper les commandes suivantes :

```sh
sudo apt install mysql-server
sudo mysql_secure_installation
```

Le projet est maintenant prêt à être utilisé.

### Mettre en place la base de données
Pour initialiser la base de données, il vous faut d'abord la créer en tapant

```sh
mysql -u root -p < resetDatabase.sql
```

Le projet dispose d'un fichier pour mettre en place et peupler la base de données, avec des données par défaut.
Pour ce faire, il va falloir compiler le projet et lancer l'application avec l'argument "reset". Cette commande lance également le serveur sur [localhost:8080/JEE-project-SpringBoot/](localhost:8080/JEE-project-SpringBoot/)

```sh
mvn spring-boot:run -Dspring-boot.run.arguments=reset
```

Par défaut, trois utilisateurs sont déjà présents:
- John Doe
	- Admin
	- Nom d'utilisateur `admin`
	- Mot de passe: `admin`
- Alex Smith
	- Professeur
	- Nom d'utilisateur: `prof`
	- Mot de passe: `prof`
- Emma Johnson
	- Elève
	- Nom d'utilisateur: `student`
	- Mot de passe: `student`

### Gestion des mails

Par défaut, les mails sont affichés dans le terminal, et son envoyé sur un serveur SMTP de [DebugMail](https://debugmail.io).

## Lancer le serveur

Pour lancer le projet sans remettre à zéro la bse de donnée, vous pouvez taper la commande suivante :
```sh
mvn spring-boot:run
```
Cette commande lancera également le serveur sur le serveur sur [localhost:8080/JEE-project-SpringBoot/](localhost:8080/JEE-project-SpringBoot/)

## Architecture du projet

Le projet utilise Spring Boot et Hibernate. Les fichiers sont séparés en repositories, services et controllers.

### Front-end

Le front-end du projet est fait avec le trio HTML/CSS/JS et JSP.

### Back-end

Le back-end du projet est fait en Java