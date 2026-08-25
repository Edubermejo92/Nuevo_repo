# PadelPulse Live

Marcador de pádel en dos apps que trabajan juntas: el **móvil** lleva la app
completa (marcador, estadísticas, historial, voz) y el **reloj Wear OS** te deja
puntuar desde la muñeca y aporta pulso, calorías y distancia reales.

| Carpeta | Qué es |
|---|---|
| `PadelPulse-Movil/` | App Android de móvil, tablet y Chromebook. WebView + Kotlin |
| `PadelPulse-WearOS/` | App de reloj. Kotlin + Jetpack Compose for Wear OS |
| `web/` | La misma app del móvil, lista para desplegar en Netlify |
| `tools/` | Scripts para regenerar los assets web |
| `docs/` | Protocolo de sincronización, firma y guía de Play Store |

Las dos apps comparten `applicationId` (`padelpulseapp2.netlify.app`) porque son
**una sola ficha de Play Store con dos formatos**. Eso obliga a firmarlas con el
mismo keystore y a darles `versionCode` distintos.

## Empezar

- **Compilar y firmar** → [`docs/FIRMAR_Y_COMPILAR.md`](docs/FIRMAR_Y_COMPILAR.md)
- **Subir a Play Store y repartir a testers** → [`docs/PLAY_STORE_TESTERS.md`](docs/PLAY_STORE_TESTERS.md)
- **Cómo hablan entre ellas** → [`docs/PROTOCOLO_SINCRONIZACION.md`](docs/PROTOCOLO_SINCRONIZACION.md)

## Modos de sincronización

| Modo | Quién lleva el marcador |
|---|---|
| **Solo** | Cada dispositivo va por su cuenta |
| **Móvil** | El móvil manda; el reloj muestra y manda pulso/calorías |
| **Reloj** | Puntúas en la muñeca; el móvil sigue el marcador |

El pulso, las calorías y la distancia van **siempre** del reloj al móvil, en
cualquier modo: los sensores están en la muñeca y el móvil nunca los inventa.

## Sin conexión

La app del móvil no depende de internet para funcionar. Tailwind, la tipografía
Lexend y los iconos van empaquetados en `assets/` (~195 KB): en una pista sin
cobertura la app arranca y se ve igual.

Si tocas clases de Tailwind en `assets/code.html`, ejecuta después:

```bash
./tools/build-web-assets.sh
```
