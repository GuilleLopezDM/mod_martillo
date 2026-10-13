# Martillo Reparador — Minecraft Java 26.3 / Fabric

## Estado del proyecto

**Código fuente de prototipo** (versión 0.1.0). **NO ES un `.jar` compilado ni se ha probado dentro de Minecraft.**
No incluye ni modifica archivos del mod de Sorac Dev.

## Mecánica incluida

- Sostén el martillo en una mano y un objeto gastado en la otra; mantén clic derecho para repararlo.
- Shift + clic derecho: repara las piezas de tu propia armadura, una por vez.
- Clic derecho sobre otro jugador con el martillo: un golpe de reparación a una pieza de su armadura (si la opción está habilitada).
- Gasta puntos de experiencia y desgaste del propio martillo; conserva los encantamientos del equipo reparado.
- Sonido de yunque y barra de estado del progreso.
- Fabric 26.3; Java 25.
- El martillo tiene 5000 puntos de durabilidad; netherita como material de reparación en yunque.

## Receta

```
Bloque hierro | Netherita      | Bloque hierro
             | Amatista       |
             | Palo           |
```

## Para compilar con Gradle y Java 25

1. Instala el JDK 25 y Gradle 9.7.1 (o añade el Gradle Wrapper oficial). Requiere acceso a Internet en el primer intento.
2. Abre la carpeta de este proyecto en IntelliJ IDEA como proyecto Gradle.
3. En la terminal de la carpeta, ejecuta `gradle build`.
4. Busca el archivo compilado en `build/libs/martillo-reparador-26-3-0.1.0.jar`.
5. Instala Fabric Loader 0.19.5 + Fabric API 0.162.0+26.3 para Minecraft 26.3.
6. Copia el `.jar` **compilado** en la carpeta `mods` de Minecraft.

El archivo `.zip` de este proyecto **no** se coloca en `mods`. Si el compilador indica errores, corregir antes de instalar.

## Configuración

El juego crea `config/martilloreparador26.json` en el primer inicio.
Reinicia Minecraft o el servidor tras modificarlo.

| Opción | Valor inicial | Qué controla |
|---|---:|---|
| ticksPerHit | 8 | Intervalo entre reparaciones |
| durabilityRestored | 12 | Puntos de daño restaurados por reparación |
| xpPointsPerHit | 1 | Puntos de XP por reparación |
| repairArmor | true | Permitir reparar armadura propia |
| repairOtherPlayers | true | Permitir reparar jugadores |
| anvilSound | true | Efectos de sonido |

## Limitaciones

- Implementación de mecánicas inspiradas en una idea general, no actualización del código original.
- Menús y partículas avanzadas no incluidos en esta versión inicial.
- No se pudo ejecutar la compilación aquí porque este entorno solo tiene Java 21 y no pudo alcanzar los repositorios de Gradle/Minecraft.
- Puede requerir ajustes de API al compilar; validar primero en un mundo de prueba.

## Licencia

El código y la textura creados para esta implementación están bajo la licencia MIT (ver `LICENSE`).
El mod original `Repair Hammer` (Sorac Dev) **no** está incluido y declara `All Rights Reserved`.
