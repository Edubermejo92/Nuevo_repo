# padelpulselive.es

Web oficial de **PadelPulse Live**, la app marcador de pádel con árbitro IA y
voz para móvil Android y reloj Wear OS.

Sitio estático (HTML + CSS + JS sin dependencias ni build) preparado para
desplegarse en **Netlify**.

---

## Estructura

```
.
├── index.html            One page principal
├── privacidad.html       Política de privacidad (RGPD / LOPDGDD)
├── cookies.html          Política de cookies (LSSI-CE art. 22.2)
├── terminos.html         Términos y condiciones (LSSI-CE art. 10)
├── 404.html              Página de error personalizada
├── netlify.toml          Redirecciones, cabeceras de seguridad y caché
├── robots.txt            Rastreo + sitemap
├── sitemap.xml           Sitemap XML
├── site.webmanifest      Manifiesto web
└── assets/
    ├── css/styles.css    Sistema de diseño completo
    ├── js/main.js        Navegación, revelado y consentimiento de cookies
    └── img/              logo.png (lockup), icon.png (pala) y og-image.png
```

---

## Datos de la app (verificados en el app bundle)

- **Package name:** `padelpulseapp2.netlify.app`
- **Ficha:** https://play.google.com/store/apps/details?id=padelpulseapp2.netlify.app
- **Plataformas:** Android + Wear OS (Jetpack Compose)
- **Modelo:** aplicación de pago, cobro único. **El precio no se muestra en la
  web**: se remite siempre a la ficha de Google Play, que es la fuente
  actualizada y evita tener que tocar el sitio si cambia.
- **12 idiomas:** Español, English, Français, Deutsch, Italiano, Português,
  Nederlands, Svenska, Suomi, Русский, 日本語, 한국어.
- **Wear OS:** marcador siempre activo en la muñeca; un toque suma el punto a
  favor y otro el punto en contra, con un segundo de margen antes de
  contabilizarlo. Control del brillo y del sonido del reloj desde la app.
- **Cuatro modos de control:** manda el móvil / manda el reloj / el reloj va
  por su cuenta / el reloj solo muestra. Vinculación por Bluetooth con código
  o sin código.
- **Puntuación:** al mejor de N sets, punto de oro o ventaja, doble falta,
  tie-break y super tie-break. Deshacer punto. Historial de partidos.

## Sistema de diseño

La paleta y la tipografía están tomadas directamente de la app (del
`tailwind.config` de su PWA), para que web y aplicación se vean igual:

| Token | Valor | Origen en la app |
|---|---|---|
| `--pulse` | `#00fd87` | `primary-container` |
| `--pulse-soft` | `#a4ffb9` | `primary` |
| `--on-pulse` | `#006532` | `on-primary` |
| `--court` | `#00cdee` | `tertiary-dim` |
| `--bg` | `#0e0e0e` | `background` / `surface` |
| `--bg-alt` | `#131313` | `surface-container-low` |
| `--surface` | `#1a1919` | `surface-container` |
| `--surface-strong` | `#201f1f` | `surface-container-high` |
| `--border-strong` | `#494847` | `outline-variant` |
| `--text` | `#ffffff` | `on-surface` |
| `--muted` | `#adaaaa` | `on-surface-variant` |

Tipografía: **Lexend**, la misma que usa la app.

El logotipo (`assets/img/logo.png`) y el icono (`assets/img/icon.png`) se han
extraído del propio bundle de la PWA y se les ha recortado el fondo negro con
un relleno por inundación desde los bordes, de modo que los negros interiores
del dibujo se conservan y el logotipo se integra sobre `#0e0e0e` sin caja.

El verde del logotipo (`#80d010`, lima) no es el mismo que el verde de la
interfaz de la app (`#00fd87`, mint). Se respeta esa dualidad: el mint manda
en la interfaz de la web y el lima se usa en la pelota del marcador, donde
además es el color realista de una bola de pádel.

Si cambias el diseño de la app, actualiza estos tokens en el bloque `:root`
de `assets/css/styles.css` y la web se adapta entera.

## Titular (páginas legales)

EBLDigital · NIF 05943392P · Calle Fermín Caballero 30, 3D, 28034 Madrid ·
ebldigital92@gmail.com (soporte y quejas).

Los tres documentos legales están completos, sin marcadores pendientes.

## Despliegue en Netlify

1. En Netlify: **Add new site → Import an existing project → GitHub** y
   selecciona este repositorio.
2. Build command: *(vacío)*. Publish directory: `.`
   Ya está configurado en `netlify.toml`, no hace falta tocar nada.
3. **Domain management → Add custom domain** → `padelpulselive.es`.
   Añade también `www.padelpulselive.es` y déjalo como redirección al dominio
   principal (Netlify lo hace automáticamente al marcar el dominio primario).
4. Activa **HTTPS** (certificado Let's Encrypt automático).
5. Recomendado: **Netlify Analytics** — es analítica de servidor, no usa
   cookies y por tanto no requiere consentimiento.

### Si añades una analítica con cookies

`assets/js/main.js` emite un evento cuando el usuario decide:

```js
window.addEventListener('ppl:consent', (e) => {
  if (e.detail.status === 'accepted') {
    // inicializar aquí el script de analítica
  }
});
```

Nada que use cookies debe cargarse fuera de ese bloque. Si lo haces, añade la
cookie a la tabla de `cookies.html`.

---

## SEO

Palabra clave principal: **app marcador de pádel**.
Secundarias: *marcador de pádel*, *contador de puntos pádel*, *marcador pádel
reloj*, *marcador pádel Wear OS*, *árbitro IA pádel*.

Ya implementado:

- `<title>` y meta description orientados a la keyword principal, con la
  palabra clave al inicio del title.
- H1 único por página; jerarquía H2/H3 con variantes semánticas de la keyword.
- Canonical en todas las páginas indexables; `noindex` solo en el 404.
- Datos estructurados JSON-LD: `Organization`, `WebSite`, `MobileApplication`
  (con `featureList` y `offers`) y `FAQPage` — este último opta a rich snippet
  de preguntas en Google.
- `BreadcrumbList` en las páginas legales.
- Open Graph y Twitter Card con imagen 1200×630 propia.
- `sitemap.xml` + `robots.txt` con referencia al sitemap.
- El JSON-LD **no declara `offers`**: al ser app de pago, publicar un precio
  obliga a mantenerlo sincronizado y un precio erróneo penaliza. Si quieres
  que Google muestre el precio en los resultados, hay que añadir el bloque
  `offers` con el importe real y actualizarlo cuando cambie.
- URLs limpias redirigidas con 301 hacia la URL canónica (`netlify.toml`).
- Cabeceras de seguridad (HSTS, CSP, `X-Content-Type-Options`…) y política de
  caché con revalidación.

**Sobre la caché de estáticos:** al no haber build, los ficheros de `assets/`
no llevan hash en el nombre, así que **no pueden marcarse como `immutable`** —
el navegador se quedaría con la versión antigua indefinidamente. Se sirven con
`max-age=3600, stale-while-revalidate=86400`. Además, las referencias a CSS,
JS, logo e icono llevan un parámetro `?v=AAAAMMDD`: **súbelo cuando cambies
uno de esos ficheros** para forzar la recarga en navegadores que ya tengan
copia.
- Sin dependencias JS externas: la página carga solo su propio CSS/JS y las
  tipografías de Google Fonts.

Después del despliegue:

1. Da de alta el dominio en **Google Search Console** y envía
   `https://padelpulselive.es/sitemap.xml`.
2. Comprueba los datos estructurados en la
   [prueba de resultados enriquecidos](https://search.google.com/test/rich-results).
3. Enlaza la web desde la ficha de Google Play (campo «Sitio web del
   desarrollador»): es el enlace de mayor autoridad que puedes conseguir para
   este proyecto.
4. Cuando la ficha esté indexada, revisa posiciones para «app marcador de
   pádel» y considera añadir contenido de apoyo (por ejemplo, una guía sobre
   cómo se cuentan los puntos en pádel) para reforzar la relevancia temática.

---

## Desarrollo local

No hay dependencias ni build. Basta con servir la carpeta:

```bash
python3 -m http.server 8000
# http://localhost:8000
```

## Accesibilidad

- Enlace «saltar al contenido», navegación con `aria-expanded`, foco visible.
- Contraste alto sobre fondo oscuro y respeto por
  `prefers-reduced-motion`.
- Sin JavaScript el contenido sigue siendo visible (fallback `<noscript>`).
