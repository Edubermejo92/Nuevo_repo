# Cat Health Tracker 🐈

App de gestión, cuidados y salud para gatos. Misma arquitectura y sistema de diseño que
**TestudoTracker**: un único `index.html` autocontenido (HTML + CSS + JS + datos) que se
sirve dentro de un WebView (Median.co, Capacitor…) o se usa directamente como PWA.

Sin backend, sin cuentas y sin conexión: todo se guarda en el `localStorage` del dispositivo.

## Contenido

| Pestaña | Qué incluye |
|---|---|
| **Inicio** | Recordatorios del día con interruptor y alarma · selector de mes con consejos estacionales (primavera, verano, otoño e invierno, 8 consejos por estación) |
| **Razas** | 54 razas agrupadas por región de origen, con peso ideal por sexo, longitud, esperanza de vida, escalas de cepillado/actividad/sociabilidad/vocalidad y fichas de pelaje, alimentación, salud, convivencia y curiosidades |
| **Comida** | 151 alimentos en 6 categorías (pienso, húmedo, carne y pescado, vegetal, lácteos y huevo, snacks y suplementos) con 4 niveles de idoneidad: apto / ocasional / no dar / **tóxico** · buscador, filtros y calculadora de ración diaria |
| **Salud** | Perfil del gato con foto · peso, longitud, edad y edad humana · condición corporal frente al rango de su raza · **evaluación automática de si necesita veterinario** · gráfico de peso · últimos registros |
| **Cuidados** | 9 bloques con más de 70 fichas: alimentación y agua, pelaje y cepillado, arenero, salud preventiva, señales de alarma, conducta, seguridad en casa, viajes, gatitos y senior |
| **Historial** | Dashboards (peso, longitud y BCS con banda de rango ideal, conteo de vacunas y visitas al veterinario) · **calendario mensual** con próximos avisos · línea de tiempo filtrable |

## Funciones

- **Hasta 20 gatos**, cada uno con su ficha, su foto y su historial independiente.
- **Registros**: peso, visita veterinaria, vacuna, antiparasitario, aseo, malestar y nota.
- **Dictado por voz**: botón 🎤 en las notas que transcribe automáticamente lo que dices
  (Web Speech API; requiere permiso de micrófono).
- **Registro de malestar**: 22 síntomas frecuentes en chips + nivel de gravedad.
- **Calendario con alarmas dentro de la app**: comprobación cada 20 segundos, aviso a
  pantalla completa con sonido, opción de posponer 10 minutos y notificación del sistema
  si el usuario da permiso. Frecuencias: diaria, varias veces al día, días concretos de
  la semana, cada N días, mensual, anual y una sola vez.
- **Avisos automáticos deducidos del historial**: próxima vacuna (+365 d), próximo
  antiparasitario (+30 d) y próxima revisión (+365 d, o +182 d a partir de los 8 años).
- **Evaluación de salud**: cruza peso con el rango de la raza y el sexo, tendencia del
  peso, BCS, edad, última vacuna, último antiparasitario, última revisión y malestares
  recientes, y devuelve un veredicto en tres niveles con los motivos concretos.
- **Calculadora de ración**: RER = 70 × peso^0,75 con factores por estado
  (gatito, adulto, esterilizado, senior, dieta, gestación, lactancia) y reparto
  seco/húmedo en gramos.
- **Bilingüe** español / inglés, con detección del idioma del dispositivo.
- **Exportación** de todos los datos a JSON.

## Archivos

```
index.html        La app completa (~4.000 líneas, sin dependencias externas salvo la fuente)
manifest.json     Manifiesto PWA
sw.js             Service worker (caché del shell para uso sin conexión)
```

## Uso

Ábrelo directamente en el navegador o sírvelo:

```bash
npx http-server -p 8080 .
# http://localhost:8080/index.html
```

Para empaquetar como app Android/iOS, apunta el WebView (Median.co u otro) a `index.html`,
igual que en TestudoTracker.

## Personalización

- **Color:** los cuatro tonos de la marca están en `:root` (`--g1` … `--g4`) al principio
  del `<style>`. El comentario de esa línea incluye los valores verdes originales de
  TestudoTracker por si quieres volver a ellos.
- **Afiliados:** la constante `AMAZON_TAG` al principio del `<script>` está vacía. Si pones
  tu etiqueta, se añade automáticamente a todos los enlaces de compra.
- **Idiomas:** añade el código nuevo a cada entrada de `I18N`, a `LANG_FLAGS` / `LANG_CODES`
  y un botón en `#lang-menu`.

## Aviso

Herramienta informativa y de seguimiento. No sustituye el diagnóstico ni el criterio de un
veterinario colegiado. Ante cualquier señal de alarma, acude a tu veterinario.
