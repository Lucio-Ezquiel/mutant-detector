# 🧬 Mutant Detector API

API REST que detecta si un humano es mutante analizando su secuencia de ADN. Un humano es mutante si tiene **más de una secuencia de cuatro letras iguales** (horizontal, vertical o diagonal).

---

## 🚀 Cómo Ejecutar Localmente

### Requisitos
- Java 17+
- Gradle (incluido en el wrapper)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/mutant-detector.git
cd mutant-detector

# 2. Compilar y ejecutar
./gradlew bootRun
```

**Listo!** La app estará en **http://localhost:8080**

### Probar la API

**Swagger UI:** http://localhost:8080/swagger-ui.html

**Ejemplo con cURL:**
```bash
# Verificar mutante (retorna 200)
curl -X POST http://localhost:8080/mutant \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'

# Ver estadísticas
curl http://localhost:8080/stats
```

### Ejecutar Tests

```bash
./gradlew test
./gradlew jacocoTestReport  # Ver cobertura en build/reports/jacoco/test/html/index.html
```

---

## 🌐 Desplegar en Render

### 1. Preparar el proyecto

Crea un archivo `Dockerfile` en la raíz:

```dockerfile
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/mutant-detector-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 2. Subir a GitHub

```bash
git add .
git commit -m "Proyecto listo para deploy"
git push origin main
```

### 3. Desplegar en Render

1. Ve a **https://render.com** y crea una cuenta (gratis)
2. Click en **"New +"** → **"Web Service"**
3. Conecta tu repositorio de GitHub
4. Configura:
    - **Name:** mutant-detector-api (o el que prefieras)
    - **Environment:** Docker
    - **Instance Type:** Free
5. Click en **"Create Web Service"**

**Listo!** En 5-10 minutos tendrás una URL como:
```
https://mutant-detector-api.onrender.com
```

---

## 📡 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/mutant` | Verifica si un ADN es mutante. Retorna 200 (mutante) o 403 (humano) |
| GET | `/stats` | Retorna estadísticas: `{"count_mutant_dna": 40, "count_human_dna": 100, "ratio": 0.4}` |

---

## 🛠️ Tecnologías

- Java 17
- Spring Boot 3.2.0
- H2 Database
- JUnit 5
- Swagger/OpenAPI
- Gradle

---

## 📊 Testing

- **36 tests** unitarios e integración
- **Cobertura:** >90% en capa de servicio
- Ejecutar: `./gradlew test`
