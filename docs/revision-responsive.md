# Revisión responsive y cierre de la web — padelpulselive.com

Fecha: 27 de agosto de 2026

## Contexto

`padelpulselive.com` funciona sobre **WordPress** (tema de bloques
Twenty Twenty-Three). El contenido de las páginas es el porte del sitio
estático original (`web/`) dentro de bloques `wp:html`, con la hoja de
estilos incrustada en cada página.

Páginas en producción:

| ID | Página | URL |
|----|--------|-----|
| 25 | Inicio (portada) | `/` |
| 17 | Política de Privacidad | `/politica-de-privacidad/` |
| 18 | Política de Cookies | `/politica-de-cookies/` |
| 19 | Términos y Condiciones | `/terminos-y-condiciones/` |
| 38 | Plantilla 404 (`wp_template`) | se sirve en cualquier URL inexistente |

## 1. Permalinks

`permalink_structure` está en `/%postname%/`, es decir, **los permalinks
ya funcionan limpios**; WordPress genera las URLs sin `/index.php/`.

Sin embargo, los enlaces internos escritos dentro del contenido seguían
apuntando a la forma antigua `https://padelpulselive.com/index.php/…`,
que era la decisión tomada cuando el servidor aún no reescribía URLs.
Al estar ya resuelto, se han reescrito **36 enlaces** en las cuatro
páginas a su forma canónica:

```
https://padelpulselive.com/index.php/politica-de-cookies/
  ->  https://padelpulselive.com/politica-de-cookies/
```

Reparto: 9 en Inicio, 10 en Privacidad, 8 en Cookies, 9 en Términos.

## 2. Revisión responsive

Medida en Chromium a 320, 360, 390, 412, 540, 768, 820, 900, 901, 1024,
1280, 1440 y 1920 px, comprobando desbordamiento horizontal
(`documentElement.scrollWidth` frente al ancho del viewport), elementos
que salen del viewport y tamaño de las áreas táctiles.

### Fallo 1 — las páginas legales desbordaban en cualquier móvil

**Impacto: alto.** Privacidad y Términos desbordaban 194–264 px y
Cookies hasta 423 px en pantallas de 320–390 px. La página entera salía
más ancha que la pantalla: se podía desplazar en horizontal y el texto
quedaba cortado.

Causa: `.legal-layout` es una cuadrícula y, por debajo de 900 px, pasa a
una sola columna `1fr`. Un ítem de cuadrícula tiene `min-width:auto`, así
que su ancho mínimo lo marca el contenido. Dentro está la tabla, con
`min-width:560px`. Aunque la tabla vive en `.table-wrap{overflow-x:auto}`,
ese `min-width` sigue contando para el tamaño intrínseco del ítem, de modo
que la columna se resolvía a 562 px (privacidad/términos) o 721 px
(cookies, tabla de 5 columnas) en vez de a los ~316 px disponibles.

Corrección:

```css
.legal-layout > *{min-width:0}
.table-wrap{max-width:100%}
```

La tabla conserva su `min-width:560px` y sigue desplazándose en
horizontal dentro de su propio contenedor, que es el comportamiento
buscado; lo que ya no hace es arrastrar la página entera.

### Fallo 2 — la portada desbordaba 32 px a 320 px de ancho

**Impacto: bajo** (solo afecta a pantallas muy estrechas, tipo iPhone SE
de primera generación o Galaxy Fold cerrado).

Causa: las cuadrículas usan `repeat(auto-fit, minmax(NNNpx, 1fr))` con
mínimos fijos. A 320 px el ancho útil es 276 px, así que `.grid--2`
(330 px) y `.split` (320 px) no caben.

Corrección, aplicada a las seis cuadrículas:

```css
.grid--2{grid-template-columns:repeat(auto-fit,minmax(min(330px,100%),1fr))}
```

`min(NNNpx, 100%)` no cambia nada por encima del umbral y deja que la
columna se encoja por debajo.

### Mejora 3 — áreas táctiles del pie

Los enlaces del pie medían 18 px de alto. Se les ha dado
`padding-block:4px` para llegar a ~26 px, sin cambio visual apreciable.

### Resultado

Tras las correcciones, ninguna de las cinco vistas (portada, tres
legales y 404) desborda en ninguno de los anchos medidos.

## 3. Lo que faltaba por crear

- **Página 404.** El sitio estático tenía su propio `404.html`, pero en
  WordPress cualquier URL inexistente caía en la plantilla por defecto de
  Twenty Twenty-Three, sin la identidad del sitio. Se ha creado la
  plantilla de bloques `404` (`wordpress/404-template.html`, publicada
  como `wp_template` #38 asignada al tema), con la misma cabecera, pie y
  banner de cookies que el resto y los CTA «Volver al inicio» y
  «Preguntas frecuentes».

  Nota de implementación: en una plantilla, `<header>` y `<footer>`
  cuelgan directamente de `.wp-site-blocks`, así que la regla que oculta
  la cabecera del tema (`body>.wp-site-blocks>header`) ocultaba también
  la propia. En la plantilla 404 esa regla se limita a
  `.wp-block-template-part`.

- **Entrada «Hello world!»** de la instalación de WordPress: estaba
  publicada y era indexable. Se ha enviado a la papelera.

## 4. Pendiente / recomendaciones

- **Purgar la caché.** LiteSpeed Cache está activo; hasta que se purgue
  (LiteSpeed Cache → Purge All) el front puede seguir sirviendo el HTML
  anterior.
- **Redirigir `/index.php/…` a la URL limpia** con un 301 en `.htaccess`,
  para que las viejas direcciones no queden como contenido duplicado.
  No es accesible desde aquí; hay que hacerlo en el hosting.
- **Comentarios**: conviene cerrarlos en todo el sitio, no hay ninguna
  entrada de blog que los necesite.
- La revisión responsive se hizo sobre una reconstrucción local del HTML
  que sirve WordPress, porque `padelpulselive.com` no es alcanzable desde
  el entorno de ejecución (bloqueado por el proxy de salida). El CSS y el
  marcado medidos son los mismos que hay publicados; lo que no se ha
  podido comprobar en vivo es la interacción con las hojas de estilo que
  inyectan el tema y los plugins.

## Contenido del repositorio

- `web/` — fuente estática del sitio, ya con las correcciones responsive
  (`assets/css/styles.css`) y el `?v=` de caché subido a `20260827`.
- `wordpress/404-template.html` — plantilla 404 publicada en WordPress.
- `docs/revision-responsive.md` — este documento.
