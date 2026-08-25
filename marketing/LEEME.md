# Kit de marketing — PadelPulse Live

Todo listo para subir. Cada carpeta va a un sitio.

```
1-google-play/     la ficha de la app
2-web-y-redes/     banner, Instagram, historias, compartir enlaces
3-logotipos/       el logotipo suelto, para lo que haga falta
4-editable/        las plantillas, por si quieres cambiar textos
```

---

## 1 · Google Play

En **Play Console → Crecimiento → Presencia en Play Store → Ficha principal**:

| Dónde lo pide Play | Fichero |
|---|---|
| Icono de la aplicación | `icono-512.png` |
| Gráfico de la función | `grafico-destacado-1024x500.png` |
| Capturas de teléfono | los 6 de `capturas-movil/`, **en ese orden** |
| Capturas de Wear OS | los 3 de `capturas-wear-os/` |

El gráfico destacado es **obligatorio**: sin él la ficha no se publica.

El orden de las capturas importa. Las dos primeras son las únicas que se ven
sin desplazar, y por eso llevan los dos argumentos más fuertes: la voz y el
reloj.

### Antes de subirlas, léete esto

**Las pantallas de móvil y reloj de las capturas son maquetas, no capturas
reales de la app.** Están hechas con los colores, la tipografía y los textos
auténticos, pero se dibujaron en HTML.

Google espera que las capturas representen la experiencia real de la
aplicación. Lo recomendable:

1. Haz capturas de verdad en un móvil y en un reloj.
2. Abre `4-editable/piezas.html` y sustituye el bloque de pantalla
   (`<div class="screen">…</div>` o `<div class="watch__f">…</div>`) por
   `<img src="fuentes/mi-captura.png" style="width:100%;height:100%;object-fit:cover">`.
3. Vuelve a exportar (ver más abajo).

Conservas la composición, los titulares y la marca, pero con pantallas reales.

---

## 2 · Web y redes

| Fichero | Dónde |
|---|---|
| `banner-web-1600x600.png` | Cabecera de la web, boletines, notas de prensa |
| `instagram-1080x1080.png` | Publicación cuadrada |
| `historia-1080x1920.png` | Historia o reel |
| `compartir-enlace-1200x630.png` | Ya está puesta en la web: es la que se ve al pegar el enlace en WhatsApp, X o LinkedIn |

---

## 3 · Logotipos

`logotipo-completo.png` (pala + texto) y `pala-sola.png` tienen **fondo
transparente**: se pueden poner sobre cualquier color oscuro.

`icono-app-512.png` es el icono de la app, sin transparencia, tal y como lo
exige Google Play.

> Sobre fondos claros el logotipo pierde contraste, porque el contorno de la
> pala es blanco. Para fondo blanco hace falta una versión alternativa: dímelo
> y se prepara.

---

## 4 · Editable

Las piezas no son PNG sueltos: se generan desde `piezas.html`. Cambias un
titular y se reexportan las 13 con la misma línea gráfica.

Hace falta [Node.js](https://nodejs.org) instalado. Una sola vez:

```bash
cd 4-editable
npm install playwright
npx playwright install chromium
```

Y cada vez que quieras exportar:

```bash
cd 4-editable
python3 -m http.server 8099 &
node render.js
```

Los PNG aparecen en `4-editable/salida/`.

Para añadir una pieza nueva basta con copiar un bloque `<div class="art"
id="lo-que-sea" style="width:1080px;height:1920px">` dentro de `piezas.html`:
el script la detecta sola y la exporta con ese nombre.

`base.css` tiene los colores y los componentes. Los tonos salen de la propia
app: verde `#00fd87`, cian `#00cdee`, fondos de `#0e0e0e` a `#262626` y
tipografía Lexend.

---

## Los mensajes

Cada pieza defiende un solo argumento, de más a menos diferenciador:

1. **La voz canta los puntos** — lo que nadie más hace de forma tan central.
2. **Puntúas en la muñeca** — el reloj no es un accesorio.
3. **12 idiomas** — interfaz y voz. Abre mercado fuera de España.
4. **Árbitro IA** — punto de oro, tie-break, super tie-break, doble falta.
5. **Cuatro modos** — móvil, reloj, reloj autónomo o reloj como pantalla.
6. **Pago único, sin suscripción** — el cierre.

**El precio no aparece en ninguna pieza**, a propósito. Se dice «un solo pago»
y «sin suscripción», que es lo que vende, y la cifra la pone Google Play. Si
algún día lo cambias, no hay que rehacer nada.
