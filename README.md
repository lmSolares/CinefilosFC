# CinéfilosFC 🎬

Aplicación móvil para Android desarrollada en Kotlin para la materia de **Programación de Dispositivos Móviles** de la **Facultad de Ciencias, UNAM** (Semestre 2026-2).

CinéfilosFC es una aplicación diseñada para los amantes del cine, permite explorar películas, series y otro tipo de producciones por género usando la API de TMDB (The Movie Database), así como gestionar perfiles de usuario únicos y almacenar reseñas de películas de forma local con persistencia en base de datos.

---
## Equipo 
1. Herrera Avalos Julio Alejandro
2. Solares Ramos Luis Mario

---

## Requisitos e Instalación

Para poder compilar y ejecutar correctamente la aplicación en Android Studio, es importante configurar la llave de la API de TMDB. 

> **IMPORTANTE:** Por motivos de seguridad, el archivo de configuración con la API Key no se sube al repositorio público de GitHub (como la anterior vez ajajaj). Este archivo se adjunta directamente en la entrega de **Google Classroom**.

### Instrucciones de configuración:
1. Descargar el archivo `local.properties` adjunto en Classroom.
2. Mover dicho archivo en la **raíz del proyecto** (al mismo nivel que el archivo `settings.gradle.kts` y la carpeta `/app`).
3. Sincronizar el proyecto con Gradle en Android Studio (`File > Sync Project with Gradle Files`).
4. Compilar y ejecutar la aplicación en su emulador o dispositivo físico.

---

## Guía de Uso de la Aplicación

La aplicación cuenta con un flujo completo y estructurado, a continuación se detalla el recorrido paso a paso:

### 1. Inicio de Sesión (Login)
* Al abrir la aplicación por primera vez, se mostrará una pantalla de bienvenida.
* Ingrese su **Nombre de Usuario** único y pulse el botón **"Iniciar Sesión"**. 
* Si es un usuario nuevo, el sistema creará automáticamente un registro vacío en la base de datos local SQLite para comenzar a guardar su información de forma personalizada.

### 2. Exploración de Contenido (Pantalla Principal)
* Una vez dentro, accederá a la cartelera interactiva.
* **Menú Lateral (Drawer):** Pulse el icono de hamburguesa para filtrar las películas por populares, series o géneros específicos (Acción, Terror, Romance).
* **Búsqueda:** Use la lupa en la barra superior para buscar producciones específicas por título en la base de datos de TMDB.
* **Barra de Navegación Inferior:** Permite alternar entre el Inicio, la sección central de Mis Reseñas y el Perfil del usuario.

### 3. Publicación de Reseñas
* Desde la cartelera de inicio, haga **clic en cualquier película** para abrir el formulario de reseñas.
* La aplicación cargará el póster utilizando la librería **Picasso**, junto con el título.
* Escriba su reseña en la caja de texto y asigne una calificación utilizando la barra de estrellas (**RatingBar**).
* Al pulsar **"Publicar"**, los datos se vincularán a la sesión actual y se guardarán de manera persistente con la fecha y el año correspondientes.

### 4. Sección "Mis Reseñas"
* Acceda a esta sección desde el botón central de la barra inferior.
* Aquí se desplegará una lista organizada en tarjetas con todas las críticas que ha redactado.
* **Filtros por Estrellas:** Utilice el menú desplegable (Spinner) superior para filtrar sus opiniones (por ejemplo, ver únicamente a las que le diste 5 estrellas).
* Cada tarjeta muestra el póster de la película, el título, el año, la puntuación, el texto de su crítica y la fecha exacta de publicación.

### 5. Configuración de Perfil
* En la sección **Perfil**, la aplicación mostrará los datos estáticos recuperados desde SQLite.
* Pulse el botón **"Editar"** en la barra superior para abrir la actividad de edición.
* Aquí podrá actualizar su Nombre Completo, su Género de Cine Favorito y su País mediante cajas de texto, así como su Sexo con un grupo de botones de opción (`RadioGroup`).
* Al presionar **"Guardar"**, la aplicación realiza un `UPDATE` persistente en la base de datos local.

### 6. Ajustes y Cierre de Sesión
* Acceda a la pantalla de Ajustes desde el menú de opciones de la barra superior de la app.
* Encontrará el botón destacado en color rojo **"Cerrar Sesión"**.
* Al pulsarlo, la aplicación utiliza un Intent configurado con las banderas `Intent.FLAG_ACTIVITY_NEW_TASK` o `Intent.FLAG_ACTIVITY_CLEAR_TASK`. Esto elimina todo el historial de pantallas acumuladas en la memoria para que el usuario regrese al Login inicial.
