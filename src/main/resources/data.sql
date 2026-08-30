-- Users
INSERT OR IGNORE INTO
 user (username, name)
VALUES
 ('jdespinosa14', 'Juan Diego')
 ('geekpenguin', 'Jorge A. García');

-- Score
INSERT OR IGNORE INTO
 score (score, description, display_name)
VALUES
 (5.0, 'Top', 'Top'),
 (4.5, 'Top', 'Top'),
 (4.0, 'Very Good', 'Muy Buena'),
 (3.5, 'Very Good', 'Muy Buena'),
 (3.0, 'Good', 'Buena'),
 (2.5, 'Good', 'Buena'),
 (2.0, 'Weak', 'Floja'),
 (1.5, 'Weak', 'Floja'),
 (1.0, 'Bad', 'Mala'),
 (0.5, 'Bad', 'Mala');

-- MCU Movies
INSERT OR IGNORE INTO
 mcu_entry (id, original_title, alternative_title, phase, release_date, poster_url, created_at, updated_at)
VALUES
-- Phase 1
(1, 'Iron Man', 'Iron Man: El Hombre de Hierro', 1, '2008-05-02 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/0805.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'The Incredible Hulk', 'Hulk: El Hombre Increíble', 1, '2008-06-13 00:00:00.0', 'https://tiermaker.com/images//media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-1745960524gkzyx79y0aqtl4uak1cbqj3nvrm.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Iron Man 2', 'Iron Man 2', 1, '2010-05-07 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1005.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Thor', 'Thor', 1, '2011-05-06 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1105.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Captain America: The First Avenger', 'Capitán América: El Primer Vengador', 1, '2011-07-22 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1107.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'The Avengers', 'The Avengers: Los Vengadores', 1, '2012-05-04 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1205.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Phase 2
(7, 'Iron Man 3', 'Iron Man 3', 2, '2013-05-03 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1305.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Thor: The Dark World', 'Thor: Un Mundo Oscuro', 2, '2013-11-08 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1311.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Captain America: The Winter Soldier', 'Capitán América: El Soldado de Invierno', 2, '2014-04-04 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1404.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Guardians of the Galaxy', 'Guardianes de la Galaxia', 2, '2014-08-01 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1408.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Avengers: Age of Ultron', 'Avengers: Era de Ultrón', 2, '2015-05-01 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1505.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Ant-Man', 'Ant-Man: El Hombre Hormiga', 2, '2015-07-17 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1507.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Phase 3
(13, 'Captain America: Civil War', 'Capitán América: Civil War', 3, '2016-05-06 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1605.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'Doctor Strange', 'Doctor Strange: Hechicero Supremo', 3, '2016-11-04 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1611.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Guardians of the Galaxy Vol. 2', 'Guardianes de la Galaxia Vol. 2', 3, '2017-05-05 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1705.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 'Spider-Man: Homecoming', 'Spider-Man: De regreso a casa', 3, '2017-07-07 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1707.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(17, 'Thor: Ragnarok', 'Thor: Ragnarok', 3, '2017-11-03 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1711.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(18, 'Black Panther', 'Pantera Negra', 3, '2018-02-16 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1802.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, 'Avengers: Infinity War', 'Avengers: Infinity War', 3, '2018-04-27 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1804.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(20, 'Ant-Man and the Wasp', 'Ant-Man y la Avispa', 3, '2018-07-06 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1807.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, 'Captain Marvel', 'Capitana Marvel', 3, '2019-03-08 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1902.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(22, 'Avengers: Endgame', 'Avengers: Endgame', 3, '2019-04-26 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1904.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(23, 'Spider-Man: Far From Home', 'Spider-Man: Lejos de casa', 3, '2019-07-02 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/1907.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Phase 4
(24, 'Black Widow', 'Black Widow', 4, '2021-07-09 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459601982107.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(25, 'Shang-Chi and the Legend of the Ten Rings', 'Shang-Chi y la Leyenda de los Diez Anillos', 4, '2021-09-03 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459601992109.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(26, 'Eternals', 'Eternos', 4, '2021-11-05 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459601992111.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(27, 'Spider-Man: No Way Home', 'Spider-Man: Sin camino a casa', 4, '2021-12-17 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602002112.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(28, 'Doctor Strange in the Multiverse of Madness', 'Doctor Strange en el Multiverso de la Locura', 4, '2022-05-06 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602002205.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(29, 'Thor: Love and Thunder', 'Thor: Amor y Trueno', 4, '2022-07-08 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602012207.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(30, 'Black Panther: Wakanda Forever', 'Pantera Negra: Wakanda por Siempre', 4, '2022-11-11 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602022212-2.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Phase 5
(31, 'Ant-Man and the Wasp: Quantumania', 'Ant-Man y la Avispa: Quantumanía', 5, '2023-02-17 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602032302.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(32, 'Guardians of the Galaxy Vol. 3', 'Guardianes de la Galaxia Vol. 3', 5, '2023-05-05 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602042305.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(33, 'The Marvels', 'The Marvels', 5, '2023-11-10 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602052311.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(34, 'Deadpool & Wolverine', 'Deadpool & Wolverine', 5, '2024-07-26 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602062407.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(35, 'Captain America: Brave New World', 'Capitán América: Un nuevo mundo', 5, '2025-02-14 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602072502.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(36, 'Thunderbolts*', 'Thunderbolts*', 5, '2025-05-02 00:00:00.0', 'https://tiermaker.com/images/media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17459602082505.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Phase 6
(37, 'The Fantastic Four: First Steps', 'Los 4 Fantásticos: Primeros Pasos', 6, '2025-07-25 00:00:00.0', 'https://tiermaker.com/images//media/template_images/2024/15377717/all-mcu-movies-and-series-2025-thunderbolts-updated-15377717/zzzzz-17530672850725-1.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
INSERT INTO mcu_entry
(38, 'Spider-Man: Brand New Day', 'Spider-Man: Un Nuevo Día', 6, '2026-07-31 00:00:00', 'https://a.ltrbxd.com/resized/film-poster/8/7/2/8/7/1/872871-spider-man-brand-new-day-0-1000-0-1500-crop.jpg?v=ebe6beb4fc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);