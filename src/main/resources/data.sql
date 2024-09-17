-- Insert the route
INSERT INTO Route (start_location_x, start_location_y, end_location_x, end_location_y, distance, transport)
VALUES (4.6278, 43.6766, -0.5543911405630629, 42.8722875079131, 0, 'driving-car');

-- Get the ID of the inserted route
SET @routeId = LAST_INSERT_ID();

-- Insert waypoints
INSERT INTO Waypoint (name, waypoint_x, waypoint_y, route_id)
VALUES
    ('Saint-Gilles', 4.43263770410886, 43.677144558350356, @routeId),
    ('Vauvert', 4.27695838000437, 43.69258385521888, @routeId),
    ('Saturagues', 4.110810064305818, 43.72050279751931, @routeId),
    ('Vendargues', 3.969794596922247, 43.65599534860676, @routeId),
    ('Montpellier', 3.876056933718259, 43.61087883875206, @routeId),
    ('Montarnaud', 3.6977408031102756, 43.64975650947599, @routeId),
    ('Saint-Guilhem-le-Désert', 3.548504239012267, 43.733597095770016, @routeId),
    ('Saint-Jean-de-la-Blaquière', 3.4232005273580164, 43.71431782032238, @routeId),
    ('Lodève', 3.3138598519494082, 43.73363735864589, @routeId),
    ('Lunas', 3.1954490322341513, 43.708861885909826, @routeId),
    ('Saint-Gervais-sur-Mare', 3.042288410625734, 43.65271683172834, @routeId),
    ('Murat-sur-Vèbre', 2.8525436908103683, 43.68377143120918, @routeId),
    ('La Salvetat-sur-Agout', 2.7026888243872955, 43.600456013479175, @routeId),
    ('Anglès', 2.560610884176348, 43.56356502627557, @routeId),
    ('Boissezon', 2.3792827461035055, 43.57572433030902, @routeId),
    ('Castres', 2.241298687916271, 43.606213692042516, @routeId),
    ('Dourgne', 2.1363901999037083, 43.48594013201416, @routeId),
    ('Revel', 2.004597938863144, 43.45861191133415, @routeId),
    ('Les Cassés', 1.868440415617034, 43.42673986509556, @routeId),
    ('Montferrand', 1.8214581726550438, 43.3653053043694, @routeId),
    ('Villenouvelle', 1.6622826131089374, 43.43522438049033, @routeId),
    ('Donneville', 1.550599108405531, 43.47274103091458, @routeId),
    ('Toulouse', 1.4442038715324408, 43.60462519792864, @routeId),
    ('Pibrac', 1.2834111268641326, 43.618934501655396, @routeId),
    ('L Isle-Jourdain', 1.0817649496040231, 43.6138333425867, @routeId),
    ('Gimont', 0.8769696148196591, 43.62635965002825, @routeId),
    ('Montégut', 0.6885183881846337, 43.63194207339614, @routeId),
    ('Auch', 0.5867380783475539, 43.64646341827505, @routeId),
    ('L Isle-de-Noé', 0.41301949961585305, 43.58673692947587, @routeId),
    ('Montesquiou', 0.32748050051234556, 43.57860064887584, @routeId),
    ('Marciac', 0.16093644562303341, 43.5243600544492, @routeId),
    ('Maubourguet', 0.03551514497488054, 43.46831408338187, @routeId),
    ('Vidouze', -0.0495594196032337, 43.443546792641875, @routeId),
    ('Morlaàs', -0.2626483017115797, 43.34530458699531, @routeId),
    ('Pau', -0.3707595281103349, 43.29508826097283, @routeId),
    ('Monein', -0.579492556496488, 43.3223536786683, @routeId),
    ('Oloron-Sainte-Marie', -0.6053130744770818, 43.19441274475645, @routeId),
    ('Sarrance', -0.602101589901281, 43.05092514110344, @routeId),
    ('Accous', -0.5987573813864142, 42.97466250683634, @routeId);