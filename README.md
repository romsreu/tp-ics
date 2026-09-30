# Documentación del proyecto

## 8.a) ¿Qué documentaríamos en el README?

El README contiene la información necesaria para que cualquier integrante del equipo o persona externa pueda comprender y utilizar el proyecto.

Documentaríamos:

* **Descripción y objetivo del proyecto.**
* **Tecnologías y herramientas utilizadas.**
* **Requisitos e instalación.**
* **Estructura y funcionamiento del proyecto.**
* **Configuración y forma de uso.**
* **Integrantes y autores.**
* **Información relevante sobre las versiones y cambios importantes.**

El README también se versionaría junto con el código, ya que la documentación debe mantenerse actualizada con el estado del proyecto.

## 8.b) Modificaciones externas y Pull Requests

Si una persona externa realiza una modificación, solicitaríamos que cree un **Pull Request (PR)** con:

* **Título descriptivo del cambio.**
* **Descripción:** qué se modificó y por qué.
* **Issue relacionado**, si corresponde.
* **Pruebas realizadas y sus resultados.**
* **Capturas o evidencia**, cuando sea necesario.
* **Información sobre posibles dependencias o efectos en otras partes del proyecto.**

Estos datos permiten entender el propósito del cambio, verificar su funcionamiento y evaluar posibles impactos antes de incorporarlo al proyecto.

### ¿Qué ofrece GitHub?

GitHub nos ofrece una funcionalidad nativa llamada **Pull Request Templates**. Podemos crear un archivo oculto llamado `pull_request_template.md`.

Al configurarlo, cada vez que alguien abra un nuevo PR en el repositorio, la caja de descripción se completará automáticamente con la estructura, preguntas y el checklist que hayamos definido.

Esto estandariza la comunicación, actúa como guía obligatoria y evita que falte información crítica en las revisiones, conectándose además con los **Workflows de CI** que verifican automáticamente el estado del código.
