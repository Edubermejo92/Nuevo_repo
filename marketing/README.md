# Kit de marketing — PadelPulse Live

Piezas gráficas para Google Play, la web y redes sociales. Todo se genera a
partir de plantillas HTML, así que se puede reeditar y volver a exportar sin
depender de ningún programa de diseño.

```
marketing/
├── plantillas/       las piezas, editables
│   ├── piezas.html   una sección .art por pieza, a tamaño real en píxeles
│   └── base.css      sistema de diseño (colores, tipografía, componentes)
├── fuentes/          logotipo, pala e icono de la app
├── salida/           los PNG exportados
└── render.js         exporta cada .art a salida/<id>.png
```

## Regenerar las piezas

```bash
# desde la raíz del repositorio
python3 -m http.server 8099 &
node marketing/render.js
```

Cada elemento con clase `.art` se captura a su tamaño exacto y se guarda con
el nombre de su `id`. Para añadir una pieza nueva basta con crear otro `.art`
con un `id` y un `width`/`height` en píxeles: el script la recoge sola.

Si Chromium está en otra ruta o sirves en otro puerto:

```bash
CHROMIUM_PATH=/ruta/chromium BASE_URL=http://localhost:3000 node marketing/render.js
```

## Qué hay y dónde va cada cosa

### Google Play

| Fichero | Tamaño | Uso |
|---|---|---|
| `fuentes/icono-app-512.png` | 512×512 | Icono de la ficha. Sin canal alfa, como exige Play |
| `play-grafico-destacado.png` | 1024×500 | *Feature graphic*. **Obligatorio** para publicar |
| `play-movil-1-voz.png` | 1080×1920 | Captura 1 — la voz canta los puntos |
| `play-movil-2-reloj.png` | 1080×1920 | Captura 2 — puntúas en la muñeca |
| `play-movil-3-idiomas.png` | 1080×1920 | Captura 3 — 12 idiomas |
| `play-movil-4-arbitro.png` | 1080×1920 | Captura 4 — árbitro IA |
| `play-movil-5-modos.png` | 1080×1920 | Captura 5 — cuatro modos móvil/reloj |
| `play-movil-6-sin-suscripcion.png` | 1080×1920 | Captura 6 — pago único |
| `play-wear-1-marcador.png` | 384×384 | Captura Wear OS — marcador |
| `play-wear-2-voz.png` | 384×384 | Captura Wear OS — voz |
| `play-wear-3-set.png` | 384×384 | Captura Wear OS — tie-break |

Play admite entre 2 y 8 capturas de teléfono, y exige capturas específicas
para Wear OS si publicas en ese canal. El orden importa: las dos primeras son
las que se ven sin desplazar, y por eso llevan los dos argumentos más fuertes
(la voz y el reloj).

### Web y redes

| Fichero | Tamaño | Uso |
|---|---|---|
| `web-banner-1600x600.png` | 1600×600 | Cabecera de web, boletines, prensa |
| `social-instagram-1080.png` | 1080×1080 | Publicación cuadrada |
| `social-story-1080x1920.png` | 1080×1920 | Historia o reel |

Para compartir enlaces de la web ya existe `assets/img/og-image.png`
(1200×630), que es la que leen WhatsApp, X y LinkedIn.

## Antes de subirlas a Google Play, léete esto

**Las pantallas de móvil y reloj de estas piezas son maquetas, no capturas
reales de la aplicación.** Están construidas con los colores, la tipografía y
los textos reales de la app (`Pareja A`, `Punto de oro`, `Toca para sumar`…),
pero se han dibujado en HTML: no salen de un dispositivo.

Google Play espera que las capturas representen la experiencia real de la
aplicación. Lo correcto es:

1. Capturar las pantallas de verdad en un móvil y en un reloj.
2. Sustituir en `piezas.html` el bloque `.phone .screen` o `.watch__f` por un
   `<img src="...">` con esa captura.
3. Volver a ejecutar `node marketing/render.js`.

Así conservas la composición, los titulares y la marca, pero con pantallas
auténticas. Las plantillas están pensadas para ese cambio.

## Los mensajes, y por qué están en ese orden

Cada pieza defiende un solo argumento. De más a menos diferenciador:

1. **La voz canta los puntos.** Es lo que ninguna otra app del sector hace de
   forma tan central, y se entiende sin explicación.
2. **Puntúas en la muñeca.** El reloj no es un accesorio: el marcador sigue
   activo y el punto se contabiliza tras un segundo, por si te equivocas.
3. **12 idiomas.** Interfaz y voz. Multiplica el mercado fuera de España.
4. **Árbitro IA.** Punto de oro, tie-break, super tie-break y doble falta.
5. **Cuatro modos.** Manda el móvil, manda el reloj, el reloj va por su cuenta
   o el reloj solo muestra.
6. **Pago único, sin suscripción.** Argumento de cierre.

**El precio no aparece en ninguna pieza**, a propósito: si cambia, no hay que
rehacer nada. Se dice «un solo pago» y «sin suscripción», que es el argumento
de venta, y la cifra la pone Google Play.

## Sistema de diseño

Los colores salen de la propia aplicación:

| Token | Valor | Origen |
|---|---|---|
| `--pulse` | `#00fd87` | `primary-container` de la app |
| `--court` | `#00cdee` | `tertiary-dim` de la app |
| `--lima` | `#8fd117` | verde del logotipo, usado en la pelota |
| `--bg` | `#0e0e0e` | `background` de la app |
| `--surf` | `#1a1919` | `surface-container` de la app |

Tipografía **Lexend**, la de la app. `base.css` es autónomo a propósito: no
depende de `assets/css/styles.css`, para que un cambio en la web no altere
piezas ya aprobadas.
