# v1 Fate of Olympus - Projet génie logiciel
**Prototype Technique – Version 1**  
**Date de rendu: 12/02/2026**

Ce dépôt contient les fichiers java de **Fate of Olympus**, un jeu de stratégie en temps réel (RTS) développé dans le cadre du cours de génie logiciel.  
Cette premiére version fait office de démo technique et permet d'illustrer les principales fonctionnalités du projet.
Réalisé par Illias ATCHAOUI, Romain POSE et Yann LERAY.

---


##  Fonctionnement général

Une partie test se lance au lancement de TestGame.
L'écran se compose de quatres parties:
**haut de page**:
	-indique le temps de jeu
	-indique les ressources du joueur
**Carte**: un tableau de cellules sur la gauche
	La carte est composée:
		-d'un **QG** (magenta) où les travailleurs déposent des ressources.
		-de **dépots de ressource** (en orange) où les travailleurs collectent des ressources.
		-de **travailleurs** (en jaune) qui récupèrent des ressources aux dernier dépot visité, et les déposent au QG du joueur.
		-d'**unités alliées** (en vert) qui se déplacent et entrent en combat avec les unités adverses.
		-d'**unités adverses** (en rouge) qui entre en combat avec les unités aliées.
	Le joueur peut:
		-placer des bâtiments via les boutons situés en bas de l'écran.
		-déplacer ses unités en les séléctionnant dans une zone à l'aide du clique gauche.
	Les batiments placés peuvent générer des unités lors ce que le joueur clique sur ces derniers(attention, il faut cliquer en haut a gauche du batiments sinon le clique ne sera pas détecté), 
		avec un délai de 10 secondes pour le temps de génération d'une unité.
	
**Panneau d'information** à droite indique les statiques pertinents liés à la séléction, comme les points de vie des unités.
**Panneau de test** en bas de l'écran permet de tester le placement d'unités aliés ou ennemies ainsi que des batiments.


---


## Équipe

- **Yann LE RAY** – Interface, affichage, factions  
- **Ilias ATCHAOUI** – Bâtiments, combat, IA  
- **Romain POSE** – Unités, ressources, optimisation  

Étudiants en **DL MI – CY Cergy Paris Université**.

---

