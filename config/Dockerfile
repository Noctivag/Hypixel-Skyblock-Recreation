# =============================================================================
# Hypixel Skyblock Recreation - Docker Configuration
# =============================================================================

FROM openjdk:21-jdk-slim

# Metadaten
LABEL maintainer="Hypixel Skyblock Recreation Team"
LABEL description="Minecraft Server für das Hypixel Skyblock Recreation Plugin"
LABEL version="1.0.0"

# Umgebungsvariablen
ENV MINECRAFT_VERSION=1.21.1
ENV PAPER_BUILD=latest
ENV SERVER_TYPE=HUB
ENV SERVER_PORT=25565
ENV JAVA_OPTS="-Xmx4G -Xms2G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+UseStringDeduplication"

# Arbeitsverzeichnis
WORKDIR /opt/minecraft

# Installiere notwendige Pakete
RUN apt-get update && \
    apt-get install -y \
    wget \
    curl \
    unzip \
    netcat-openbsd \
    && rm -rf /var/lib/apt/lists/*

# Erstelle Minecraft-Benutzer
RUN groupadd -r minecraft && useradd -r -g minecraft minecraft

# Lade Paper herunter
RUN wget -O paper.jar "https://api.papermc.io/v2/projects/paper/versions/${MINECRAFT_VERSION}/builds/${PAPER_BUILD}/downloads/paper-${MINECRAFT_VERSION}-${PAPER_BUILD}.jar"

# Erstelle Verzeichnisse
RUN mkdir -p /opt/minecraft/plugins \
    && mkdir -p /opt/minecraft/logs \
    && mkdir -p /opt/minecraft/world \
    && mkdir -p /opt/minecraft/world_nether \
    && mkdir -p /opt/minecraft/world_the_end \
    && mkdir -p /opt/minecraft/config

# Erstelle server.properties
RUN cat > server.properties << EOF
server-port=${SERVER_PORT}
server-name=Hypixel Skyblock Recreation - ${SERVER_TYPE}
motd=§6§lHypixel Skyblock Recreation §7- §e${SERVER_TYPE} Server
online-mode=false
white-list=false
max-players=100
view-distance=10
simulation-distance=10
difficulty=easy
gamemode=survival
hardcore=false
pvp=true
allow-nether=true
allow-flight=false
enable-command-block=false
enable-query=false
enable-rcon=false
rcon.port=25575
rcon.password=
level-name=world
level-seed=
level-type=minecraft:normal
generator-settings={}
generate-structures=true
spawn-protection=16
max-world-size=29999984
function-permission-level=2
network-compression-threshold=256
max-tick-time=60000
use-native-transport=true
enable-jmx-monitoring=false
enable-status=true
broadcast-rcon-to-ops=true
broadcast-console-to-ops=true
EOF

# Erstelle eula.txt
RUN echo "eula=true" > eula.txt

# Erstelle Start-Skript
RUN cat > start.sh << 'EOF'
#!/bin/bash

set -e

echo "🎮 Starting Hypixel Skyblock Recreation Server..."

# Warte auf Abhängigkeiten
if [ -n "$REDIS_HOST" ] && [ -n "$REDIS_PORT" ]; then
    echo "⏳ Waiting for Redis at $REDIS_HOST:$REDIS_PORT..."
    while ! nc -z $REDIS_HOST $REDIS_PORT; do
        echo "   Redis not ready, waiting..."
        sleep 2
    done
    echo "✅ Redis is ready!"
fi

if [ -n "$MYSQL_HOST" ] && [ -n "$MYSQL_PORT" ]; then
    echo "⏳ Waiting for MySQL at $MYSQL_HOST:$MYSQL_PORT..."
    while ! nc -z $MYSQL_HOST $MYSQL_PORT; do
        echo "   MySQL not ready, waiting..."
        sleep 2
    done
    echo "✅ MySQL is ready!"
fi

# Kopiere Plugin falls vorhanden
if [ -f "/opt/minecraft/plugin/BasicsPlugin-1.0-SNAPSHOT.jar" ]; then
    echo "📦 Installing plugin..."
    cp /opt/minecraft/plugin/BasicsPlugin-1.0-SNAPSHOT.jar /opt/minecraft/plugins/
fi

# Starte Server
echo "🚀 Starting Minecraft Server..."
exec java $JAVA_OPTS -jar paper.jar nogui
EOF

RUN chmod +x start.sh

# Ändere Besitzer
RUN chown -R minecraft:minecraft /opt/minecraft

# Wechsle zu Minecraft-Benutzer
USER minecraft

# Exponiere Port
EXPOSE ${SERVER_PORT}

# Health Check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${SERVER_PORT} || exit 1

# Starte Server
CMD ["./start.sh"]