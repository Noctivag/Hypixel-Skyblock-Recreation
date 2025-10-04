# 🔨 Build-Anleitung - Hypixel Skyblock Recreation

## 📋 **Übersicht**

Diese Anleitung führt Sie durch den Build-Prozess des Hypixel Skyblock Recreation Plugins.

## ⚡ **Schnellstart**

### **Voraussetzungen**
- **Java 21** oder höher
- **Maven 3.9+** oder höher
- **Git** für Versionskontrolle

### **Build ausführen**
```bash
# Projekt klonen
git clone <repository-url>
cd Hypixel-Skyblock-Recreation

# Dependencies installieren
mvn clean install

# Plugin bauen
mvn clean package
```

## 🔧 **Detaillierte Build-Anleitung**

### **1. Environment Setup**

#### **Java Installation**
```bash
# Java Version prüfen
java -version
javac -version

# Sollte Java 21 oder höher anzeigen
```

#### **Maven Installation**
```bash
# Maven Version prüfen
mvn -version

# Sollte Maven 3.9+ anzeigen
```

### **2. Build-Optionen**

#### **Standard Build**
```bash
# Clean build
mvn clean package

# Build mit Tests
mvn clean package -DskipTests=false

# Build ohne Tests (schneller)
mvn clean package -DskipTests=true
```

#### **Advanced Build**
```bash
# Build mit Debug-Informationen
mvn clean package -X

# Build mit spezifischem Profil
mvn clean package -Pproduction

# Build mit optimierten Einstellungen
mvn clean package -T 4 -Dmaven.compiler.fork=true
```

### **3. Build-Output**

#### **Output-Dateien**
```
target/
├── BasicsPlugin-1.0-SNAPSHOT.jar          # Haupt-Plugin
├── BasicsPlugin-1.0-SNAPSHOT-sources.jar  # Quellcode
├── BasicsPlugin-1.0-SNAPSHOT-javadoc.jar  # Dokumentation
└── dependency-reduced-pom.xml             # Reduzierte POM
```

#### **Plugin installieren**
```bash
# Plugin in Server kopieren
cp target/BasicsPlugin-1.0-SNAPSHOT.jar /path/to/server/plugins/

# Server neustarten
```

## 🐳 **Docker Build**

### **Docker Image bauen**
```bash
# Docker Image erstellen
docker build -t hypixel-skyblock-plugin .

# Container starten
docker run -d --name skyblock-server hypixel-skyblock-plugin
```

### **Docker Compose**
```bash
# Mit Docker Compose starten
docker-compose up -d

# Logs anzeigen
docker-compose logs -f
```

## 🧪 **Testing**

### **Unit Tests ausführen**
```bash
# Alle Tests ausführen
mvn test

# Spezifische Tests ausführen
mvn test -Dtest=SkyblockServiceTest

# Tests mit Coverage
mvn test jacoco:report
```

### **Integration Tests**
```bash
# Integration Tests ausführen
mvn verify

# Mit Docker-Integration
mvn verify -Pintegration-tests
```

## 📊 **Performance Build**

### **Optimierte Builds**
```bash
# Performance-optimierter Build
mvn clean package -Pperformance

# Mit Profiling
mvn clean package -Pprofiling
```

### **Memory-Optimierung**
```bash
# Mit Memory-Optimierung
mvn clean package -Xmx4g -XX:+UseG1GC
```

## 🔍 **Troubleshooting**

### **Häufige Probleme**

#### **Java Version Fehler**
```bash
# Problem: Java Version zu alt
# Lösung: Java 21+ installieren
```

#### **Maven Dependency Fehler**
```bash
# Problem: Dependencies können nicht geladen werden
# Lösung: Maven Repository prüfen
mvn dependency:resolve
```

#### **Build Fehler**
```bash
# Problem: Compilation Fehler
# Lösung: Clean build ausführen
mvn clean
mvn compile
```

### **Debug-Modus**
```bash
# Mit Debug-Informationen
mvn clean package -X -Dmaven.compiler.verbose=true
```

## 📈 **Build-Performance**

### **Parallel Builds**
```bash
# Multi-threaded Build
mvn clean package -T 4

# Mit Thread-Per-Core
mvn clean package -T 1C
```

### **Build-Caching**
```bash
# Maven Local Repository optimieren
# ~/.m2/settings.xml konfigurieren
```

## 🚀 **Continuous Integration**

### **GitHub Actions**
```yaml
# .github/workflows/build.yml
name: Build Plugin
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
      - run: mvn clean package
```

### **Jenkins Pipeline**
```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
```

## 📋 **Build-Checkliste**

### **Vor dem Build**
- [ ] Java 21+ installiert
- [ ] Maven 3.9+ installiert
- [ ] Git Repository aktualisiert
- [ ] Dependencies verfügbar

### **Nach dem Build**
- [ ] JAR-Datei erstellt
- [ ] Tests erfolgreich
- [ ] Keine Warnings
- [ ] Dokumentation generiert

### **Deployment-Checkliste**
- [ ] Plugin in Server kopiert
- [ ] Konfiguration angepasst
- [ ] Dependencies installiert
- [ ] Server neugestartet
- [ ] Logs überprüft

## 🎯 **Build-Optimierungen**

### **Maven Optimierungen**
```xml
<!-- pom.xml Optimierungen -->
<properties>
    <maven.compiler.fork>true</maven.compiler.fork>
    <maven.compiler.maxmem>2048m</maven.compiler.maxmem>
</properties>
```

### **JVM Optimierungen**
```bash
# Optimierte JVM-Parameter
export MAVEN_OPTS="-Xmx4g -XX:+UseG1GC -XX:+UseStringDeduplication"
```

## 📚 **Weitere Ressourcen**

- **Maven Documentation**: https://maven.apache.org/guides/
- **Java Documentation**: https://docs.oracle.com/en/java/
- **Docker Documentation**: https://docs.docker.com/
- **GitHub Actions**: https://docs.github.com/en/actions
