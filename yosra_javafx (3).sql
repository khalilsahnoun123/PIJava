-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 05 mars 2025 à 13:48
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `yosra_javafx`
--

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(180) NOT NULL,
  `roles` longtext DEFAULT NULL COMMENT '(DC2Type:json)',
  `password` varchar(255) NOT NULL,
  `is_verified` tinyint(1) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `bloque` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `email`, `roles`, `password`, `is_verified`, `first_name`, `last_name`, `phone_number`, `image`, `status`, `bloque`) VALUES
(32, 'alimaloul534@gmail.com', 'admin', '$2a$10$OIbRqC0L7VPW2E6qIB/Hiu.D3iV2yof7CW7Vsvxlv0BWGFJogv17G', 0, 'John', 'Doe', '28860682', 'C:\\Users\\Motaz\\Pictures\\Capture d’écran 2024-10-06 192017.png', 'enable\r\n', NULL),
(42, 'yosraabdelkader44@gmail.com', 'user', '$2a$10$t.zpTfM20eZAfNQZkDEcwer4C2BWrgif0vBGdut/mUq6NRRuKeSQC', 0, 'yos', 'ab', '58280156', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125631.png', 'desable', b'1'),
(43, 'abdelkaderyosra7@gmail.com', 'user', '$2a$10$KRcbRAWYnI0Qq3mGeKa7Z.3Bw4VBDalh7uepZVyPJdtqMsoBl9PBy', 0, 'yasmina', 'yass', '58280278', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 113600.png', 'enabled', NULL),
(45, 'yosra@gmail.com', 'user', '$2a$10$GCjwDAN6Ai1BDRVlUXuuVu8g3qQ53V.ulCsE0GE2VlCZG4CSFSRGq', 0, 'yosra1123', 'yallaYYY', '58280156', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 113600.png', 'desable', b'0'),
(49, 'papa@gmail.com', 'user', '$2a$10$1G1Ty3hkKICkvp8D3boVwuQrSHxewdRKAX4LcECfQOWxQKnohm0j6', 0, 'papa', 'papa', '58280156', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125631.png', 'desable', b'1'),
(50, 'yos@gmail.com', 'admin', '$2a$10$0uLmqQHnjMqHg/iPXT6Lj.jnaa2ubmg39HB0mjRPS3cX92Ki5Mj8G', 0, 'yosra', 'yosra', '58280156', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125631.png', 'enabled', b'1'),
(52, 'soulaima@soulaima.com', 'user', '$2a$10$T5uCRZEAVQAqLaQn/iQyM.0eBhI7.QFLzTcNQwCdXF8gMEgo4kEQe', 0, 'soulaimaAAAA', 'soulaima', '21240155', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 113600.png', 'desable', NULL),
(55, 'mama@gmail.com', 'user', '$2a$10$QuLqGeC0WA4J0wmAsIV85eQG4s8hW.DGCph.ITNYbds1NEnZqKNSe', 0, 'mama', 'bahri', '28280155', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125735.png', 'enabled', NULL),
(58, 'yas@gmail.com', 'COVOITUREUR', '$2a$10$jD.xgpcyDQpx0tPVQHfFVe7ibwQOzLhNjQDWcbSe.MMHz7F8WgbOS', 0, 'yasmina', 'yasSSS', '58280156', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125936.png', 'enabled', NULL),
(59, 'chams@gmail.com', 'USER', '$2a$10$YRrbPVfHrbfd80kHYtb6UeVDc1jKh4LzU7MRHZ07czn58h/s3mRV6', 0, 'chamousa', 'abdelk', '58245678', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125906.png', 'enabled', NULL),
(62, 'Abdelkader.Yosra@esprit.tn', 'USER', '$2a$10$rps0bS5eh4RPz6j8YHIrgeZkCWoYQzacrHpY0Tlrtd/O5zvi.Hfmm', 0, 'hamidou', 'ab', '99999999', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125936.png', 'enabled', NULL),
(63, 'abdyosra79@gmail.com', 'USER', '$2a$10$GfaQQWUgRFzE5lp4LrHJNOLsp24rHjYdHwMDJBFD05WdXRtlItnO6', 0, 'hamidou', 'abdelka', '21240155', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125906.png', 'enabled', NULL),
(64, 'yasmineabdelkader44@gmail.com', 'USER', '$2a$10$uQk3rY5Vbesfto3ZC2R9d.nPUBb6Mc49NIZut24CtMCdogWvz/oVW', 0, 'yasmine', 'abd', '28456755', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125735.png', 'enabled', NULL),
(65, 'abdelyosra2@gmail.com', 'TAXI', '$2a$10$AUHXdTAtYmtd7vBsb8xOi.L.NV/D06PvwV8ANPOTjsdfI4NPXkTbS', 0, 'manel', 'ough', '24507632', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125936.png', 'enabled', NULL),
(66, 'manel@gmail', 'COVOITUREUR', '$2a$10$cT6B7W2eG7cI0K3h734z.u4rAsYU9do2TZo0bo6sAHwpn4JENGNU6', 0, 'manel', 'ou', '96714444', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125906.png', 'enabled', NULL),
(67, 'khadija@gmail.com', 'CHAUFFEUR', '$2a$10$vLmCKvw89cLCjiz0Mu8qqO3pB4/HgfZtmHxUfPXWTwh4mQb/SQzGO', 0, 'khadija', 'ba', '28280145', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 125906.png', 'enabled', NULL),
(68, 'name@example.com', 'ADMIN', '$2a$10$H8YQs5Lh5Z56mpVg1V03JuYrCUBY9Ihfdr58hdwD9Q9PWKh4oPtMa', 0, 'Yos Raa Abdelkader', 'Yos Raa Abdelkader', '28252525', '', 'enabled', NULL),
(70, 'ben@gmail.com', 'TAXI', '$2a$10$5w7lLDf559.lVEG6GyZgVu9EWYUo3KACIWnbF0XlIPbXcCLTx6G7C', 0, 'amine', 'ben', '58759789', 'C:\\Users\\Lenovo\\Pictures\\Screenshots\\Capture d\'écran 2024-10-09 130000.png', 'enabled', NULL);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UNIQ_8D93D649E7927C74` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
