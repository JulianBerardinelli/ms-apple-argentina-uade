-- ============================================================
-- SEED COMPLETO — E-Commerce Apple Argentina
-- Base de datos: ecommerce_db4
-- ============================================================
-- INSTRUCCIONES:
--   1. Levantá la app Spring Boot al menos una vez para que
--      Hibernate cree todas las tablas automáticamente.
--   2. Luego ejecutá este script en MySQL Workbench.
-- ============================================================

USE ecommerce_db4;

-- Desactivar FK checks para limpiar sin conflictos
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE detalles_orden;
TRUNCATE TABLE items_carrito;
TRUNCATE TABLE ordenes_compra;
TRUNCATE TABLE carritos;
TRUNCATE TABLE fotos_producto;
TRUNCATE TABLE productos;
TRUNCATE TABLE categorias;
TRUNCATE TABLE usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. USUARIOS (3 usuarios de prueba)
-- ============================================================
INSERT INTO usuarios (id, username, email, password, nombre, apellido) VALUES
(1, 'admin',         'admin@apple-ar.com',    'admin123',  'Admin',   'Sistema'),
(2, 'carlos.mendez', 'carlos@email.com',      'pass123',   'Carlos',  'Mendez'),
(3, 'lucia.torres',  'lucia@email.com',       'pass123',   'Lucia',   'Torres');

-- ============================================================
-- 2. CATEGORÍAS (6 categorías del catálogo Apple)
-- ============================================================
INSERT INTO categorias (id, nombre, descripcion) VALUES
(1, 'Mac',          'Computadoras personales Mac: MacBook Air, MacBook Pro, Mac mini, iMac y Mac Pro.'),
(2, 'iPhone',       'Smartphones iPhone con chip Apple Silicon. La línea más avanzada del mercado.'),
(3, 'iPad',         'Tablets iPad: iPad mini, iPad Air, iPad Pro. Potencia y portabilidad en uno.'),
(4, 'Apple Watch',  'Relojes inteligentes Apple Watch: Series 10, Ultra 2 y SE. Salud y conectividad.'),
(5, 'AirPods',      'Auriculares inalámbricos Apple: AirPods 4, AirPods Pro y AirPods Max.'),
(6, 'Accesorios',   'Accesorios originales Apple: Apple Pencil, MagSafe, cables y más.');

-- ============================================================
-- 3. PRODUCTOS (20 productos de apple.com)
-- Columnas: id, nombre, descripcion, precio, stock, activo,
--           categoria_id, usuario_creador_id
-- ============================================================
INSERT INTO productos (id, nombre, descripcion, precio, stock, activo, categoria_id, usuario_creador_id) VALUES

-- MAC (5 productos)
(1,  'MacBook Air 13" M4',
     'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, pantalla Liquid Retina 13.6", batería 18 hs, Wi-Fi 6E.',
     1099.00, 25, 1, 1, 1),

(2,  'MacBook Air 15" M4',
     'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, pantalla Liquid Retina 15.3", batería 18 hs.',
     1299.00, 20, 1, 1, 1),

(3,  'MacBook Pro 14" M4 Pro',
     'Chip M4 Pro, CPU 14 núcleos, GPU 20 núcleos, 24 GB RAM, 512 GB SSD, pantalla XDR 14.2" 120 Hz, batería 22 hs.',
     1999.00, 15, 1, 1, 1),

(4,  'Mac mini M4',
     'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, 3 puertos Thunderbolt 4 frontales, diseño 5x5".',
     599.00,  30, 1, 1, 1),

(5,  'iMac 24" M4',
     'All-in-one pantalla Retina 4.5K 24". Chip M4, 16 GB RAM, 256 GB SSD, cámara 12 MP, Spatial Audio. 7 colores.',
     1299.00, 12, 1, 1, 1),

-- iPHONE (4 productos)
(6,  'iPhone 16',
     'Chip A18, pantalla Super Retina XDR 6.1", cámara Fusion 48 MP, Dynamic Island, acción de cámara y USB-C.',
     799.00,  50, 1, 2, 1),

(7,  'iPhone 16 Plus',
     'Chip A18, pantalla Super Retina XDR 6.7", cámara Fusion 48 MP, batería de todo el día, USB-C y MagSafe.',
     899.00,  40, 1, 2, 1),

(8,  'iPhone 16 Pro',
     'Chip A18 Pro, pantalla Super Retina XDR 6.3" ProMotion 120 Hz, triple cámara 48 MP, titanio, USB-C 3.',
     999.00,  35, 1, 2, 1),

(9,  'iPhone 16 Pro Max',
     'Chip A18 Pro, pantalla 6.9" ProMotion 120 Hz, triple cámara 48 MP + teleobjetivo 5x, titanio, batería líder.',
     1199.00, 28, 1, 2, 1),

-- iPAD (3 productos)
(10, 'iPad mini A17 Pro',
     'Chip A17 Pro, pantalla Liquid Retina 8.3", compatible con Apple Intelligence, Apple Pencil Pro y USB-C.',
     499.00,  22, 1, 3, 2),

(11, 'iPad Air 11" M3',
     'Chip M3, pantalla Liquid Retina 11", compatible con Apple Pencil Pro y Magic Keyboard para iPad Air.',
     599.00,  18, 1, 3, 2),

(12, 'iPad Pro 11" M4',
     'Chip M4, pantalla Ultra Retina XDR OLED 11", nano-texture opcional, Thunderbolt 4, el más delgado de Apple.',
     999.00,  15, 1, 3, 2),

-- APPLE WATCH (3 productos)
(13, 'Apple Watch Series 10 GPS 42mm',
     'Pantalla más grande, más delgado. Sensor de temperatura, ECG, detección de caídas. Hasta 18 hs de batería.',
     399.00,  25, 1, 4, 2),

(14, 'Apple Watch Ultra 2',
     'Titanio de grado aeroespacial, pantalla 2000 nits, GPS de doble frecuencia, hasta 60 hs de batería.',
     799.00,  10, 1, 4, 2),

(15, 'Apple Watch SE GPS 40mm',
     'El Apple Watch más accesible. Detección de caídas, rastreo de actividad, resistente al agua 50 m.',
     249.00,  38, 1, 4, 2),

-- AIRPODS (3 productos)
(16, 'AirPods 4',
     'Diseño rediseñado, cancelación de ruido activa, audio espacial personalizado y chip H2. Estuche USB-C.',
     129.00,  60, 1, 5, 3),

(17, 'AirPods Pro 2',
     'Chip H2, cancelación de ruido líder, modo Transparencia adaptable, audio espacial y hasta 30 hs con estuche.',
     249.00,  45, 1, 5, 3),

(18, 'AirPods Max USB-C',
     'Auriculares over-ear premium. Chip H1, cancelación de ruido activa, audio espacial, aluminio y malla textil.',
     549.00,  18, 1, 5, 3),

-- ACCESORIOS (2 productos)
(19, 'Apple Pencil Pro',
     'Latencia ultra baja, detección de inclinación, acción de apretón y girar para cambiar herramientas. MagSafe.',
     129.00,  40, 1, 6, 3),

(20, 'Cargador MagSafe 25W',
     'Carga inalámbrica MagSafe a 25 W para iPhone 16. Cable trenzado de 1 m, sin adaptador de corriente incluido.',
     45.00,   100, 1, 6, 3);

-- ============================================================
-- 4. FOTOS DE PRODUCTOS (2 fotos por producto seleccionado)
-- ============================================================
INSERT INTO fotos_producto (url_imagen, orden, producto_id) VALUES
-- MacBook Air 13" M4
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mba13-m4-midnight-select-202503', 1, 1),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mba13-m4-midnight-gallery-1-202503', 2, 1),
-- MacBook Pro 14" M4 Pro
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mbp14-m4-pro-space-black-select-202411', 1, 3),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mbp14-m4-pro-space-black-gallery-1-202411', 2, 3),
-- iPhone 16 Pro
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-16-pro-finish-select-202409-6-3inch-deserttitanium', 1, 8),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-16-pro-gallery-1-202409', 2, 8),
-- iPhone 16 Pro Max
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-16-pro-finish-select-202409-6-9inch-deserttitanium', 1, 9),
-- AirPods Pro 2
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/MXK73', 1, 17),
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/MXK73_AV1', 2, 17),
-- iPad Pro 11" M4
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/ipad-pro-11-select-wifi-spacegray-202405', 1, 12),
-- Apple Watch Ultra 2
('https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/MQDY3ref_VW_34FR', 1, 14);

-- ============================================================
-- 5. CARRITOS DE PRUEBA
-- ============================================================
INSERT INTO carritos (id, fecha_creacion, estado, usuario_id) VALUES
-- Carrito activo de Carlos (tiene items pendientes de checkout)
(1, '2026-04-20 10:00:00', 'ACTIVO',    2),
-- Carrito de Lucia ya procesado (checkout completado)
(2, '2026-04-20 09:00:00', 'CHECKOUT',  3);

-- ============================================================
-- 6. ITEMS DEL CARRITO
-- ============================================================
INSERT INTO items_carrito (cantidad, precio_unitario, subtotal, carrito_id, producto_id) VALUES
-- Carrito 1 (ACTIVO - Carlos): iPhone 16 x1 + AirPods 4 x2
(1, 799.00,  799.00,  1, 6),
(2, 129.00,  258.00,  1, 16),
-- Carrito 2 (CHECKOUT - Lucia): MacBook Air 13" x1 + Apple Pencil Pro x1
(1, 1099.00, 1099.00, 2, 1),
(1, 129.00,  129.00,  2, 19);

-- ============================================================
-- 7. ORDEN DE COMPRA (resultado del checkout del carrito 2)
-- ============================================================
INSERT INTO ordenes_compra (id, fecha_orden, total, estado, usuario_id, carrito_id) VALUES
(1, '2026-04-20 09:15:00', 1228.00, 'COMPLETADA', 3, 2);

-- ============================================================
-- 8. DETALLES DE LA ORDEN
-- ============================================================
INSERT INTO detalles_orden (cantidad, precio_unitario, subtotal, orden_id, producto_id) VALUES
(1, 1099.00, 1099.00, 1, 1),
(1, 129.00,  129.00,  1, 19);

-- ============================================================
-- VERIFICACIÓN FINAL
-- ============================================================
SELECT 'usuarios'       AS tabla, COUNT(*) AS registros FROM usuarios
UNION ALL
SELECT 'categorias',    COUNT(*) FROM categorias
UNION ALL
SELECT 'productos',     COUNT(*) FROM productos
UNION ALL
SELECT 'fotos_producto',COUNT(*) FROM fotos_producto
UNION ALL
SELECT 'carritos',      COUNT(*) FROM carritos
UNION ALL
SELECT 'items_carrito', COUNT(*) FROM items_carrito
UNION ALL
SELECT 'ordenes_compra',COUNT(*) FROM ordenes_compra
UNION ALL
SELECT 'detalles_orden',COUNT(*) FROM detalles_orden;
