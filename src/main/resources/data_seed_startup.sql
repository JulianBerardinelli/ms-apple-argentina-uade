-- Seed idempotente para carga automática al iniciar.
-- No borra datos existentes. Usa INSERT IGNORE para evitar duplicados.

INSERT IGNORE INTO usuarios (id, username, email, password, nombre, apellido, role) VALUES
(1, 'admin',         'admin@apple-ar.com',    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',  'Admin',   'Sistema', 'ROLE_ADMIN'),
(2, 'carlos.mendez', 'carlos@email.com',      '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',   'Carlos',  'Mendez',  'ROLE_USER'),
(3, 'lucia.torres',  'lucia@email.com',       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',   'Lucia',   'Torres',  'ROLE_USER');

INSERT IGNORE INTO categorias (id, nombre, descripcion) VALUES
(1, 'Mac',          'Computadoras personales Mac: MacBook Air, MacBook Pro, Mac mini, iMac y Mac Pro.'),
(2, 'iPhone',       'Smartphones iPhone con chip Apple Silicon. La línea más avanzada del mercado.'),
(3, 'iPad',         'Tablets iPad: iPad mini, iPad Air, iPad Pro. Potencia y portabilidad en uno.'),
(4, 'Apple Watch',  'Relojes inteligentes Apple Watch: Series 10, Ultra 2 y SE. Salud y conectividad.'),
(5, 'AirPods',      'Auriculares inalámbricos Apple: AirPods 4, AirPods Pro y AirPods Max.'),
(6, 'Accesorios',   'Accesorios originales Apple: Apple Pencil, MagSafe, cables y más.');

INSERT IGNORE INTO productos (id, nombre, descripcion, precio, stock, activo, categoria_id, usuario_creador_id) VALUES
(1,  'MacBook Air 13" M4', 'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, pantalla Liquid Retina 13.6", batería 18 hs, Wi-Fi 6E.', 1099.00, 25, 1, 1, 1),
(2,  'MacBook Air 15" M4', 'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, pantalla Liquid Retina 15.3", batería 18 hs.', 1299.00, 20, 1, 1, 1),
(3,  'MacBook Pro 14" M4 Pro', 'Chip M4 Pro, CPU 14 núcleos, GPU 20 núcleos, 24 GB RAM, 512 GB SSD, pantalla XDR 14.2" 120 Hz, batería 22 hs.', 1999.00, 15, 1, 1, 1),
(4,  'Mac mini M4', 'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, 3 puertos Thunderbolt 4 frontales, diseño 5x5".', 599.00, 30, 1, 1, 1),
(5,  'iMac 24" M4', 'All-in-one pantalla Retina 4.5K 24". Chip M4, 16 GB RAM, 256 GB SSD, cámara 12 MP, Spatial Audio. 7 colores.', 1299.00, 12, 1, 1, 1),
(6,  'iPhone 16', 'Chip A18, pantalla Super Retina XDR 6.1", cámara Fusion 48 MP, Dynamic Island, acción de cámara y USB-C.', 799.00, 50, 1, 2, 1),
(7,  'iPhone 16 Plus', 'Chip A18, pantalla Super Retina XDR 6.7", cámara Fusion 48 MP, batería de todo el día, USB-C y MagSafe.', 899.00, 40, 1, 2, 1),
(8,  'iPhone 16 Pro', 'Chip A18 Pro, pantalla Super Retina XDR 6.3" ProMotion 120 Hz, triple cámara 48 MP, titanio, USB-C 3.', 999.00, 35, 1, 2, 1),
(9,  'iPhone 16 Pro Max', 'Chip A18 Pro, pantalla 6.9" ProMotion 120 Hz, triple cámara 48 MP + teleobjetivo 5x, titanio, batería líder.', 1199.00, 28, 1, 2, 1),
(10, 'iPad mini A17 Pro', 'Chip A17 Pro, pantalla Liquid Retina 8.3", compatible con Apple Intelligence, Apple Pencil Pro y USB-C.', 499.00, 22, 1, 3, 2),
(11, 'iPad Air 11" M3', 'Chip M3, pantalla Liquid Retina 11", compatible con Apple Pencil Pro y Magic Keyboard para iPad Air.', 599.00, 18, 1, 3, 2),
(12, 'iPad Pro 11" M4', 'Chip M4, pantalla Ultra Retina XDR OLED 11", nano-texture opcional, Thunderbolt 4, el más delgado de Apple.', 999.00, 15, 1, 3, 2),
(13, 'Apple Watch Series 10 GPS 42mm', 'Pantalla más grande, más delgado. Sensor de temperatura, ECG, detección de caídas. Hasta 18 hs de batería.', 399.00, 25, 1, 4, 2),
(14, 'Apple Watch Ultra 2', 'Titanio de grado aeroespacial, pantalla 2000 nits, GPS de doble frecuencia, hasta 60 hs de batería.', 799.00, 10, 1, 4, 2),
(15, 'Apple Watch SE GPS 40mm', 'El Apple Watch más accesible. Detección de caídas, rastreo de actividad, resistente al agua 50 m.', 249.00, 38, 1, 4, 2),
(16, 'AirPods 4', 'Diseño rediseñado, cancelación de ruido activa, audio espacial personalizado y chip H2. Estuche USB-C.', 129.00, 60, 1, 5, 3),
(17, 'AirPods Pro 2', 'Chip H2, cancelación de ruido líder, modo Transparencia adaptable, audio espacial y hasta 30 hs con estuche.', 249.00, 45, 1, 5, 3),
(18, 'AirPods Max USB-C', 'Auriculares over-ear premium. Chip H1, cancelación de ruido activa, audio espacial, aluminio y malla textil.', 549.00, 18, 1, 5, 3),
(19, 'Apple Pencil Pro', 'Latencia ultra baja, detección de inclinación, acción de apretón y girar para cambiar herramientas. MagSafe.', 129.00, 40, 1, 6, 3),
(20, 'Cargador MagSafe 25W', 'Carga inalámbrica MagSafe a 25 W para iPhone 16. Cable trenzado de 1 m, sin adaptador de corriente incluido.', 45.00, 100, 1, 6, 3);

INSERT IGNORE INTO fotos_producto (url_imagen, orden, producto_id) VALUES
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mba13-m4-midnight-select-202503', 1, 1),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mba13-m4-midnight-gallery-1-202503', 2, 1),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mbp14-m4-pro-space-black-select-202411', 1, 3),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mbp14-m4-pro-space-black-gallery-1-202411', 2, 3),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-16-pro-finish-select-202409-6-3inch-deserttitanium', 1, 8),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-16-pro-gallery-1-202409', 2, 8),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-16-pro-finish-select-202409-6-9inch-deserttitanium', 1, 9),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/MXK73', 1, 17),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/MXK73_AV1', 2, 17),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/ipad-pro-11-select-wifi-spacegray-202405', 1, 12),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/MQDY3ref_VW_34FR', 1, 14);

INSERT IGNORE INTO carritos (id, fecha_creacion, estado, usuario_id) VALUES
(1, '2026-04-20 10:00:00', 'ACTIVO',    2),
(2, '2026-04-20 09:00:00', 'CHECKOUT',  3);

INSERT IGNORE INTO items_carrito (id, cantidad, precio_unitario, subtotal, carrito_id, producto_id) VALUES
(1, 1, 799.00,  799.00,  1, 6),
(2, 2, 129.00,  258.00,  1, 16),
(3, 1, 1099.00, 1099.00, 2, 1),
(4, 1, 129.00,  129.00,  2, 19);

INSERT IGNORE INTO ordenes_compra (id, fecha_orden, total, estado, usuario_id, carrito_id) VALUES
(1, '2026-04-20 09:15:00', 1228.00, 'COMPLETADA', 3, 2);

INSERT IGNORE INTO detalles_orden (id, cantidad, precio_unitario, subtotal, orden_id, producto_id) VALUES
(1, 1, 1099.00, 1099.00, 1, 1),
(2, 1, 129.00,  129.00,  1, 19);
