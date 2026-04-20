-- ============================================================
-- SEED: Productos Apple en ecommerce_db4
-- Basado en el catálogo Apple 2025 (precios en USD)
-- Tabla: productos (id, nombre, descripcion, precio, stock)
-- ============================================================

USE ecommerce_db4;

INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
(
    'MacBook Air 13" M4',
    'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, pantalla Liquid Retina 13.6", batería 18 hs, cámara 12 MP, Wi-Fi 6E. Disponible en Medianoche, Blanco Estelar, Azul Cielo y Verde Salvia.',
    1099.00,
    25
),
(
    'MacBook Air 15" M4',
    'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, pantalla Liquid Retina 15.3", batería 18 hs, cámara 12 MP, dos puertos Thunderbolt 4. Ideal para productividad en pantalla grande.',
    1299.00,
    20
),
(
    'MacBook Pro 14" M4 Pro',
    'Chip M4 Pro, CPU 14 núcleos, GPU 20 núcleos, 24 GB RAM, 512 GB SSD, pantalla XDR 14.2" 120 Hz, Thunderbolt 5, HDMI, lector SD, batería 22 hs. Para profesionales de video, diseño y desarrollo.',
    1999.00,
    15
),
(
    'Mac mini M4',
    'Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, 3 puertos Thunderbolt 4 frontales, diseño compacto 5x5". Soporta hasta 3 monitores externos. Perfecto para hogar y oficina.',
    599.00,
    30
),
(
    'iMac 24" M4',
    'All-in-one con pantalla Retina 4.5K de 24". Chip M4, CPU 10 núcleos, GPU 10 núcleos, 16 GB RAM, 256 GB SSD, cámara 12 MP, Spatial Audio, triple micrófono. Disponible en 7 colores.',
    1299.00,
    18
);

-- Verificar registros insertados
SELECT id, nombre, precio, stock FROM productos;
