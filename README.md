# padelpulselive.es

Web oficial de **Padel Pulse Live**, la app marcador de pádel con árbitro IA y
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
    └── img/              Logo, favicon e imagen Open Graph (1200×630)
```

---

## Antes de publicar: 3 cosas pendientes

### 1. Enlace real de Google Play

Todos los CTA apuntan hoy a una **búsqueda** en Google Play, porque la ficha de
la app no era accesible al construir la web. En cuanto tengas la URL definitiva
(`https://play.google.com/store/apps/details?id=TU.PACKAGE.NAME`), sustitúyela
en todo el sitio con un único comando:

```bash
grep -rl "play.google.com/store/search" . --include="*.html" --include="*.toml" \
  | xargs sed -i 's|https://play.google.com/store/search?q=Padel%20Pulse%20Live&amp;c=apps|https://play.google.com/store/apps/details?id=TU.PACKAGE.NAME|g; s|https://play.google.com/store/search?q=Padel%20Pulse%20Live&c=apps|https://play.google.com/store/apps/details?id=TU.PACKAGE.NAME|g'
```

Revisa también `installUrl` y `downloadUrl` dentro del JSON-LD de `index.html`.

### 2. Datos del titular en las páginas legales

Las tres páginas legales llevan marcadores entre corchetes que **hay que
rellenar** para que tengan validez:

```bash
grep -rn "\[RAZÓN SOCIAL\|\[NIF\|\[DIRECCIÓN" *.html
```

- `[RAZÓN SOCIAL O NOMBRE Y APELLIDOS DEL TITULAR]`
- `[NIF O CIF]`
- `[DIRECCIÓN POSTAL COMPLETA]`

Los correos usados son `info@padelpulselive.es` y `privacidad@padelpulselive.es`:
crea esos buzones o cámbialos por los tuyos.

### 3. Verificar las capacidades descritas

Los textos describen la app según lo previsto (historial, formatos
configurables, sincronización móvil ↔ reloj, funcionamiento sin conexión).
Ajusta cualquier frase que no corresponda con la versión publicada, sobre todo
en `index.html` y en el bloque FAQ (que también alimenta el JSON-LD `FAQPage`).

---

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
- URLs limpias redirigidas con 301 hacia la URL canónica (`netlify.toml`).
- Cabeceras de seguridad (HSTS, CSP, `X-Content-Type-Options`…) y caché larga
  para estáticos: ambas cosas puntúan en Core Web Vitals y en confianza.
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
