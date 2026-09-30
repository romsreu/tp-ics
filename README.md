# WMS - Sistema de Gestión de Depósito

Trabajo Práctico Integrador — Ingeniería y Calidad del Software, UTN FRSF.
Tema: **Sistemas de Gestión de la Configuración** (Git + GitHub + Gitflow).

**Integrantes:** Roman Scarabino, Gino Scarabino, Lautaro Bernal, Tomas Passadore.

---

## Descripción del proyecto

Versión básica de un **WMS (Warehouse Management System)** basada en el caso de estudio
*"Implementación de una plataforma de Gestión de Depósitos y de Gestión de Envíos en una empresa
dedicada a e-Commerce"*. Permite:

- Recibir mercadería de proveedores.
- Despachar pedidos, validando que el producto exista y que haya stock suficiente.
- Registrar devoluciones de clientes.
- Consultar el stock e imprimir el inventario del depósito.

### Tecnologías

- Java 21
- Maven (build y dependencias)
- JUnit 5 (tests unitarios)
- GitHub Actions (integración continua)

### Cómo compilar y correr

```bash
mvn verify                                          # compila y corre los tests
java -cp target/classes ar.edu.utn.frsf.wms.Main    # corre la demo
```

### Estructura

```
src/main/java/ar/edu/utn/frsf/wms/
├── Producto.java      # producto con SKU y nombre
├── Deposito.java      # recepción, despacho, devoluciones y stock
└── Main.java          # demo
src/test/java/...      # tests unitarios (JUnit 5)
.github/
├── CODEOWNERS               # responsables de revisión
├── workflows/ci.yml         # CI: compila y testea en cada PR y push a main/develop
└── pull_request_template.md # plantilla de los Pull Requests
.gitignore                   # archivos locales que no se versionan
```

---

## Flujo de trabajo: Gitflow

| Rama | Sale de | Se mergea a | Uso |
|------|---------|-------------|-----|
| `main` | — | — | Código en producción. Cada merge lleva un tag de versión (`v1.0`, `v1.0.1`, ...). |
| `develop` | `main` | — | Integración del desarrollo. Rama por defecto del repositorio. |
| `feature/*` | `develop` | `develop` | Nuevas funcionalidades. |
| `release/*` | `develop` | `main` y `develop` | Preparación de una versión para producción. |
| `hotfix/*` | `main` | `main` y `develop` | Corrección urgente de un error en producción. |

Los merges se hacen siempre con **merge commit** (`--no-ff` / "Create a merge commit" en GitHub),
para que en el historial quede registrada cada rama y qué cambios trajo.

Historial de versiones:

| Versión | Tipo | Cambios |
|---------|------|---------|
| `v1.0` | Release | Recepción, despacho y consulta de stock. |
| `v1.0.1` | Hotfix | Despachar un producto inexistente daba un error confuso de stock insuficiente. |
| `v1.1` | Release | Registro de devoluciones. |

---

## Respuestas del Trabajo Práctico

### 1) Repositorio en GitHub

Creamos el repositorio `romsreu/tp-ics` y dimos permiso de lectura a los docentes de la cátedra.
Los integrantes del equipo tienen permiso de escritura, necesario para revisar y mergear.

### 2) CODEOWNERS

El archivo `.github/CODEOWNERS` asigna responsables de revisión. Cuando se abre un Pull Request,
GitHub agrega automáticamente como revisores a los dueños de los archivos modificados. Asignamos
a los cuatro integrantes como responsables de todo el repositorio, y la carpeta `.github/`
(configuración de CI y del repositorio) tiene un responsable específico.

### 3) Integración continua (CI)

El workflow `.github/workflows/ci.yml` corre en **cada Pull Request** y en **cada push a `main` y
`develop`**. Instala Java 21 y ejecuta `mvn verify`, que compila el proyecto y corre los tests.
Si falla, el PR queda marcado en rojo y no debería mergearse.

Un ejemplo real: en el primer PR, un revisor sugirió cambiar `HashMap` por `TreeMap` y la sugerencia
se aplicó desde GitHub, pero faltaba el `import`. El CI falló, se corrigió el import en un nuevo
commit y recién ahí se aprobó el PR. Sin CI, ese error hubiera llegado a `develop`.

### 4, 5 y 6) Pull Requests y participación del equipo

Cada cambio se hizo en su propia rama y se integró con un Pull Request:

```bash
git checkout develop && git pull
git checkout -b feature/nombre        # a. crear la rama local
git push -u origin feature/nombre     # b. publicarla en el remoto
# c. crear el PR en GitHub hacia develop (o main en releases/hotfixes)
# d. revisar, aprobar y mergear desde GitHub
```

En los PR dejamos comentarios en líneas específicas, sugerencias aplicables desde GitHub
(*suggestions*), pedidos de cambio (*Request changes*) y aprobaciones. Mergeamos PRs **desde GitHub**
(ej. la feature inicial, el `.gitignore`, el hotfix hacia `main`) y también **por comandos**
(la release 1.0 y el hotfix hacia `develop`):

```bash
git checkout main
git merge --no-ff release/1.0
git push origin main
```

### 7a) Modificación y publicación (push)

Cada modificación se commitea en su rama y se publica con `git push`. Por ejemplo, el agregado del
`.gitignore` en la rama `feature/gitignore`.

### 7b) ¿Cómo evitamos subir configuraciones locales?

Con el archivo **`.gitignore`** en la raíz del repositorio. Git ignora los archivos que coinciden
con sus patrones, así que no aparecen en `git status` ni se suben por error. Ignoramos:

- `target/`: archivos generados por Maven al compilar (se regeneran, no son código fuente).
- `.idea/`, `*.iml`, `.vscode/`, `.settings/`, `.project`, `.classpath`: configuración de cada IDE,
  que es personal de cada integrante y pisaría la de los demás.
- `.DS_Store`, `Thumbs.db`: archivos del sistema operativo.
- `*.log`, `.env`: logs y variables de entorno locales, que pueden tener datos sensibles.

Si un archivo ya estaba versionado antes de agregarlo al `.gitignore`, hay que dejar de trackearlo
con `git rm --cached <archivo>`.

### 7c y 7d) Rama de Release 1

Siguiendo Gitflow, la release sale de `develop` cuando tiene todo lo que queremos liberar. En la
rama de release solo se hacen ajustes para salir a producción (versión, correcciones menores),
no funcionalidades nuevas. Nuestra modificación fue fijar la versión en el `pom.xml`
(`0.1.0-SNAPSHOT` → `1.0.0`).

```bash
git checkout develop && git pull
git checkout -b release/1.0
# modificar la versión en pom.xml
git commit -am "Release 1.0.0"
git push -u origin release/1.0
```

### 7e) ¿Cómo llevamos la Release 1 a producción siguiendo Gitflow?

La rama de release se mergea a **`main`** (producción) y se marca con un **tag** de versión. Además
se mergea de vuelta a **`develop`**, para que los ajustes hechos en la release no se pierdan. Por
último se borra la rama de release.

```bash
git checkout main && git pull
git merge --no-ff release/1.0
git tag -a v1.0 -m "Release 1.0"
git push origin main --tags

git checkout develop && git pull
git merge --no-ff release/1.0
git push

git branch -d release/1.0
git push origin --delete release/1.0
```

### 7f) Se encontró un error en producción, ¿cómo lo corregimos?

Con una rama **`hotfix/*` creada desde `main`**, no desde `develop`. El error está en lo que está en
producción, que es `main`. Si partiéramos de `develop` arrastraríamos cambios que todavía no fueron
liberados ni probados. El hotfix lleva solo la corrección.

El error: al despachar un producto que no existía en el depósito, el sistema informaba
"Stock insuficiente ... disponible 0" en lugar de indicar que el producto no existe. Se corrigió
para que informe "Producto inexistente" y se agregó un test que cubre el caso.

```bash
git checkout main && git pull
git checkout -b hotfix/1.0.1
# corrección + test + versión 1.0.1 en pom.xml
git commit -am "Hotfix: despacho de producto inexistente"
git push -u origin hotfix/1.0.1
```

### 7g) Llevar la corrección a producción

Igual que una release: el hotfix se mergea a **`main`** con un nuevo tag (`v1.0.1`) y también a
**`develop`**, para que la corrección esté en las próximas versiones (si no, la release 1.1
volvería a tener el error). El merge a `main` lo hicimos con un Pull Request desde GitHub y el
merge a `develop` por comandos.

```bash
# luego de mergear el PR hotfix/1.0.1 -> main en GitHub:
git checkout main && git pull
git tag -a v1.0.1 -m "Hotfix 1.0.1"
git push origin v1.0.1

git checkout develop && git pull
git merge --no-ff hotfix/1.0.1
git push
```

### 7h) Nueva funcionalidad

Creamos `feature/devoluciones` desde `develop` para agregar el registro de devoluciones, una de las
funciones del WMS según el caso de estudio.

```bash
git checkout develop && git pull
git checkout -b feature/devoluciones
```

### 7i) Modificación A, modificación B y volver al estado A

- **A:** se agrega `registrarDevolucion()` con sus tests. Commit y push.
- **B:** se agrega el total de unidades al inventario. Commit y push.
- **Deshacer B:** con `git revert`.

```bash
git commit -am "A: agrega registro de devoluciones" && git push -u origin feature/devoluciones
git commit -am "B: muestra total de unidades"        && git push
git revert HEAD --no-edit                            && git push
```

`git revert` crea un **commit nuevo que deshace los cambios de B**, sin borrar B del historial.
El código vuelve a quedar como en A y en el historial queda registrado que B existió y se deshizo.

No usamos `git reset --hard` porque B **ya estaba publicado** en el remoto. `reset` reescribe la
historia: obliga a hacer `git push --force` y rompe las copias locales de los integrantes que ya
habían bajado B. `reset` solo es aceptable para commits que todavía no se pushearon.

### 7j) Llevar la nueva funcionalidad a producción

Siguiendo Gitflow, una feature **nunca va directo a `main`**:

1. La feature se integra a `develop` con un Pull Request.
2. Se crea `release/1.1` desde `develop` y se ajusta la versión (`1.1.0`).
3. La release se mergea a `main` con el tag `v1.1`, y de vuelta a `develop`.
4. Se borran las ramas `feature/devoluciones` y `release/1.1`.

```bash
git checkout develop && git pull
git checkout -b release/1.1
# versión 1.1.0 en pom.xml
git commit -am "Release 1.1.0" && git push -u origin release/1.1

git checkout main && git pull
git merge --no-ff release/1.1
git tag -a v1.1 -m "Release 1.1"
git push origin main --tags

git checkout develop && git pull
git merge --no-ff release/1.1 && git push
```

---

## 8) ¿Cómo podemos documentar con Git?

Git y GitHub permiten documentar en varios niveles:

- **README.md**: documentación general del proyecto, versionada junto al código.
- **Mensajes de commit**: qué cambió y por qué, en cada cambio.
- **Pull Requests**: descripción, discusión y revisión de cada cambio.
- **Tags y Releases**: qué versión está en producción y qué incluye.
- **Issues**: pedidos de funcionalidad y reportes de errores.

### 8a) ¿Qué documentaríamos en el README?

El README contiene la información necesaria para que cualquier integrante del equipo o persona
externa pueda comprender y utilizar el proyecto. Documentaríamos:

- **Descripción y objetivo del proyecto.**
- **Tecnologías y herramientas utilizadas.**
- **Requisitos e instalación.**
- **Estructura y funcionamiento del proyecto.**
- **Configuración y forma de uso.**
- **Flujo de trabajo** (modelo de ramas, cómo contribuir, cómo se libera una versión).
- **Integrantes y autores.**
- **Información relevante sobre las versiones y cambios importantes.**

El README se versiona junto con el código, ya que la documentación debe mantenerse actualizada
con el estado del proyecto: cada cambio en la documentación queda en el historial y se revisa en
los Pull Requests igual que el código.

### 8b) Modificaciones externas y Pull Requests

Si una persona externa realiza una modificación, le pediríamos que cree un **Pull Request** con:

- **Título descriptivo** del cambio.
- **Descripción**: qué se modificó y, sobre todo, **por qué**.
- **Issue relacionado**, si corresponde (ej. `Closes #12`), para tener trazabilidad entre el pedido
  y el cambio.
- **Tipo de cambio** (feature, corrección, refactor, documentación), que define hacia qué rama va.
- **Cómo probarlo**: pasos, pruebas realizadas y sus resultados.
- **Capturas o evidencia**, cuando sea necesario.
- **Posibles dependencias o efectos** en otras partes del proyecto (compatibilidad, configuración).

Estos datos permiten entender el propósito del cambio, verificar su funcionamiento y evaluar
posibles impactos antes de incorporarlo, sin depender de que el autor esté disponible para
explicarlo.

#### ¿Qué ofrece GitHub?

- **Pull Request Templates**: el archivo `.github/pull_request_template.md` hace que la descripción
  de cada PR nuevo arranque precargada con la estructura, las preguntas y el checklist que definimos.
  Esto estandariza la comunicación y evita que falte información crítica. Es la plantilla que usa
  este repositorio.
- **Issue Templates**: lo mismo para reportes de errores y pedidos de funcionalidad.
- **CODEOWNERS**: asigna automáticamente como revisores a los responsables del código modificado.
- **GitHub Actions (CI)**: verifica automáticamente que el cambio compile y pase los tests.
- **Revisiones**: comentarios por línea, sugerencias aplicables con un clic y estados
  *Approve* / *Request changes*.
- **Branch protection**: exige que los cambios entren por PR, con aprobación y CI en verde.
- **CONTRIBUTING.md**: guía de contribución que GitHub muestra al abrir un PR o issue.
