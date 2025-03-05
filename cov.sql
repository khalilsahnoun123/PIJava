-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 05 mars 2025 à 13:33
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `cov`
--

-- --------------------------------------------------------

--
-- Structure de la table `covoiturage`
--

CREATE TABLE `covoiturage` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `point_de_depart` varchar(255) NOT NULL,
  `point_de_destination` varchar(255) NOT NULL,
  `prix` float NOT NULL,
  `nbr_place` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `covoiturage`
--

INSERT INTO `covoiturage` (`id`, `id_user`, `point_de_depart`, `point_de_destination`, `prix`, `nbr_place`) VALUES
(4, 1, 'tunis', 'benzart', 20, 3),
(5, 1, 'hhh', 'hhhh', 8, 2),
(6, 1, 'soulayma', 'hahaha', 5445, 5),
(7, 1, 'manouba', 'esprit', 6, 2),
(8, 1, 'jjfdjhf', 'jfdhdjf', 55, 2),
(9, 1, 'manzah', 'esprit', 8, 2);

-- --------------------------------------------------------

--
-- Structure de la table `reservationvelo`
--

CREATE TABLE `reservationvelo` (
  `id_reservation_velo` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_velo` int(11) NOT NULL,
  `date_debut` timestamp NOT NULL DEFAULT current_timestamp(),
  `date_fin` date NOT NULL,
  `statut` varchar(255) NOT NULL DEFAULT 'reserved',
  `paiement_effectue` tinyint(1) NOT NULL DEFAULT 0,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reservationvelo`
--

INSERT INTO `reservationvelo` (`id_reservation_velo`, `id_user`, `id_velo`, `date_debut`, `date_fin`, `statut`, `paiement_effectue`, `price`) VALUES
(1, 1, 1, '2025-02-20 23:00:32', '2025-02-21', 'reserved', 0, 45.00),
(2, 1, 2, '2025-02-21 00:00:52', '2025-02-21', 'reserved', 0, 47.50),
(3, 1, 3, '2025-02-21 00:45:55', '2025-02-21', 'reserved', 0, 17.50),
(4, 1, 8, '2025-02-26 11:34:45', '2025-02-27', 'reserved', 0, 245.00),
(5, 1, 9, '2025-02-27 20:56:13', '2025-02-28', 'reserved', 0, 232.50),
(6, 1, 10, '2025-02-28 08:44:25', '2025-02-28', 'reserved', 0, 17.50),
(7, 1, 10, '2025-02-28 08:44:25', '2025-02-28', 'reserved', 0, 17.50),
(8, 1, 10, '2025-02-28 08:44:25', '2025-02-28', 'reserved', 0, 17.50),
(9, 1, 10, '2025-02-28 08:44:25', '2025-02-28', 'reserved', 0, 17.50),
(10, 1, 10, '2025-02-28 08:44:25', '2025-02-28', 'reserved', 0, 17.50);

-- --------------------------------------------------------

--
-- Structure de la table `reservation_cov`
--

CREATE TABLE `reservation_cov` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_cov` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `nbr_place` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reservation_cov`
--

INSERT INTO `reservation_cov` (`id`, `id_user`, `id_cov`, `status`, `nbr_place`) VALUES
(4, 1, 4, 'Accepté', 3),
(6, 1, 4, 'Refusé', 1),
(7, 1, 4, 'Refusé', 5),
(8, 1, 6, 'En Attente', 2),
(9, 1, 7, 'En Attente', 2),
(10, 1, 7, 'En Attente', 3),
(11, 1, 6, 'En Attente', 6);

-- --------------------------------------------------------

--
-- Structure de la table `reservation_taxi`
--

CREATE TABLE `reservation_taxi` (
  `id` int(11) NOT NULL,
  `id_vehicule` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `stationvelo`
--

CREATE TABLE `stationvelo` (
  `id_station` int(11) NOT NULL,
  `name_station` varchar(255) NOT NULL,
  `gouvernera` varchar(255) NOT NULL,
  `municapilite` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `id_admin` int(11) NOT NULL,
  `station_image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `stationvelo`
--

INSERT INTO `stationvelo` (`id_station`, `name_station`, `gouvernera`, `municapilite`, `adresse`, `id_admin`, `station_image`) VALUES
(5, 'manouba', 'manouba', 'oued ellil', 'rue diar el melk', 1, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\images\\1740099688953_1739747285117_station3.jpg'),
(6, 'ariana soghra', 'ariana', 'la petite ariana', 'rue habib thameur', 1, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\images\\1740099720558_1739744715760_1739743512008_1739742325793_station3.jpg'),
(7, 'khayrdin', 'mnouba', 'manouba', 'rue21', 1, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\images\\1740125586835_1739874910747_bmx.jpg'),
(8, 'kjh', 'hbh', 'jh', 'jhjh', 1, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\images\\1740690352731_1740066159147_bike.png'),
(9, 'manoub', 'mnouba', 'maouba', 'manouba', 1, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\images\\1740732510306_pliant.jpg'),
(10, 'mestir', 'mestir', 'mestir', 'kjj', 1, 'C:\\Users\\jihen\\Desktop\\PIJava\\images\\1740836885674_1739743512008_1739742325793_station3.jpg');

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `gouvernorat` varchar(255) DEFAULT NULL,
  `municipalite` varchar(255) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `nom`, `gouvernorat`, `municipalite`, `adresse`) VALUES
(1, 'hh', 'manouba', NULL, NULL),
(2, 'jihen', 'ariana', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `vehicule`
--

CREATE TABLE `vehicule` (
  `id` int(11) NOT NULL,
  `id_chauffeur` int(11) NOT NULL,
  `marque` varchar(255) NOT NULL,
  `modele` varchar(255) NOT NULL,
  `immatriculation` varchar(255) NOT NULL,
  `numero_de_serie` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `vehicule`
--

INSERT INTO `vehicule` (`id`, `id_chauffeur`, `marque`, `modele`, `immatriculation`, `numero_de_serie`) VALUES
(4, 1, 'hhhh', 'hhhh', 'hhhh', 'hhh');

-- --------------------------------------------------------

--
-- Structure de la table `velo`
--

CREATE TABLE `velo` (
  `id_velo` int(11) NOT NULL,
  `id_station` int(11) NOT NULL,
  `dispo` int(11) NOT NULL,
  `id_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `velo`
--

INSERT INTO `velo` (`id_velo`, `id_station`, `dispo`, `id_type`) VALUES
(6, 6, 1, 1),
(7, 6, 1, 4),
(8, 5, 0, 1),
(9, 5, 0, 7),
(10, 5, 0, 2),
(11, 9, 1, 7),
(12, 9, 1, 4),
(13, 5, 1, 9),
(14, 7, 1, 9),
(15, 7, 1, 8),
(16, 8, 1, 9),
(17, 8, 1, 5),
(18, 5, 1, 3),
(19, 5, 1, 2),
(20, 6, 1, 4),
(21, 6, 1, 6),
(22, 7, 1, 3),
(23, 7, 1, 2),
(24, 8, 1, 6),
(25, 8, 1, 6),
(26, 9, 1, 4),
(27, 9, 1, 6),
(28, 5, 1, 5);

-- --------------------------------------------------------

--
-- Structure de la table `velo_type`
--

CREATE TABLE `velo_type` (
  `id_type` int(11) NOT NULL,
  `type_name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `velo_image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `velo_type`
--

INSERT INTO `velo_type` (`id_type`, `type_name`, `description`, `price`, `velo_image`) VALUES
(1, 'Vélo de route', 'Rapide et léger, idéal pour le bitume.', 7.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\veloDeRoute.jpg'),
(2, 'VTT', 'Conçu pour les terrains accidentés et les sentiers.', 14.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\VTT.jpg'),
(3, 'Vélo hybride', 'Un mélange de vélo de route et de VTT, idéal pour les déplacements.', 15.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\hybride.jpg'),
(4, 'Vélo électrique', 'Équipé d’un moteur électrique pour une assistance au pédalage.', 20.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\electrique.jpg'),
(5, 'Vélo pliant', 'Compact et facile à ranger ou à transporter.', 12.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\pliant.jpg'),
(6, 'Vélo de ville', 'Confortable, conçu pour des balades détendues sur des surfaces plates.', 9.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\veloDeRoute.jpg'),
(7, 'BMX', 'Conçu pour les figures, les acrobaties et la course sur pistes de terre.', 10.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\bmx.jpg'),
(8, 'Vélo de randonnée', 'Conçu pour les longues distances avec une capacité de chargement.', 14.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\randonne.jpg'),
(9, 'Vélo cargo', 'Conçu pour transporter de lourdes charges ou des passagers.', 16.00, 'C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\image_velo\\cargo.jpg');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `covoiturage`
--
ALTER TABLE `covoiturage`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_cov` (`id_user`);

--
-- Index pour la table `reservationvelo`
--
ALTER TABLE `reservationvelo`
  ADD PRIMARY KEY (`id_reservation_velo`);

--
-- Index pour la table `reservation_cov`
--
ALTER TABLE `reservation_cov`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_res_cov` (`id_user`),
  ADD KEY `fk_res_cov` (`id_cov`);

--
-- Index pour la table `reservation_taxi`
--
ALTER TABLE `reservation_taxi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_res` (`id_user`),
  ADD KEY `fk_res_vec` (`id_vehicule`);

--
-- Index pour la table `stationvelo`
--
ALTER TABLE `stationvelo`
  ADD PRIMARY KEY (`id_station`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `vehicule`
--
ALTER TABLE `vehicule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_vec` (`id_chauffeur`);

--
-- Index pour la table `velo`
--
ALTER TABLE `velo`
  ADD PRIMARY KEY (`id_velo`),
  ADD KEY `id_station` (`id_station`),
  ADD KEY `id_type` (`id_type`);

--
-- Index pour la table `velo_type`
--
ALTER TABLE `velo_type`
  ADD PRIMARY KEY (`id_type`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `covoiturage`
--
ALTER TABLE `covoiturage`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `reservationvelo`
--
ALTER TABLE `reservationvelo`
  MODIFY `id_reservation_velo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `reservation_cov`
--
ALTER TABLE `reservation_cov`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT pour la table `reservation_taxi`
--
ALTER TABLE `reservation_taxi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `stationvelo`
--
ALTER TABLE `stationvelo`
  MODIFY `id_station` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `vehicule`
--
ALTER TABLE `vehicule`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `velo`
--
ALTER TABLE `velo`
  MODIFY `id_velo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT pour la table `velo_type`
--
ALTER TABLE `velo_type`
  MODIFY `id_type` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `covoiturage`
--
ALTER TABLE `covoiturage`
  ADD CONSTRAINT `fk_user_cov` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `reservation_cov`
--
ALTER TABLE `reservation_cov`
  ADD CONSTRAINT `fk_res_cov` FOREIGN KEY (`id_cov`) REFERENCES `covoiturage` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user_res_cov` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `reservation_taxi`
--
ALTER TABLE `reservation_taxi`
  ADD CONSTRAINT `fk_res_vec` FOREIGN KEY (`id_vehicule`) REFERENCES `vehicule` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user_res` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `vehicule`
--
ALTER TABLE `vehicule`
  ADD CONSTRAINT `fk_user_vec` FOREIGN KEY (`id_chauffeur`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `velo`
--
ALTER TABLE `velo`
  ADD CONSTRAINT `velo_ibfk_1` FOREIGN KEY (`id_station`) REFERENCES `stationvelo` (`id_station`),
  ADD CONSTRAINT `velo_ibfk_2` FOREIGN KEY (`id_type`) REFERENCES `velo_type` (`id_type`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
