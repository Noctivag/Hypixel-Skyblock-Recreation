#!/bin/bash

# Multi-Server Start Script für das Basics Plugin
# Dieses Skript startet alle Server des Multi-Server-Systems

# Farben für Output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Konfiguration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="$(dirname "$SCRIPT_DIR")"
SERVERS_DIR="$BASE_DIR/servers"
JAVA_OPTS="-Xmx4G -Xms2G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200"
PAPER_JAR="paper-1.20.4.jar"

# Server-Konfiguration
declare -A SERVERS=(
    ["hub"]="25565"
    ["island"]="25566"
    ["dungeon"]="25567"
    ["event"]="25568"
    ["auction"]="25569"
    ["bank"]="25570"
    ["minigame"]="25571"
    ["pvp"]="25572"
)

# Funktionen
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_java() {
    if ! command -v java &> /dev/null; then
        log_error "Java ist nicht installiert oder nicht im PATH"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        log_error "Java 17 oder höher ist erforderlich. Aktuelle Version: $JAVA_VERSION"
        exit 1
    fi
    
    log_success "Java $JAVA_VERSION gefunden"
}

check_directories() {
    if [ ! -d "$SERVERS_DIR" ]; then
        log_info "Erstelle Server-Verzeichnisse..."
        mkdir -p "$SERVERS_DIR"
    fi
    
    for server in "${!SERVERS[@]}"; do
        SERVER_DIR="$SERVERS_DIR/$server"
        if [ ! -d "$SERVER_DIR" ]; then
            log_info "Erstelle Verzeichnis für $server Server..."
            mkdir -p "$SERVER_DIR"
        fi
        
        if [ ! -f "$SERVER_DIR/$PAPER_JAR" ]; then
            log_warning "Paper JAR nicht gefunden in $SERVER_DIR"
            log_info "Bitte laden Sie Paper 1.20.4 herunter und platzieren Sie es als $PAPER_JAR"
        fi
        
        if [ ! -d "$SERVER_DIR/plugins" ]; then
            mkdir -p "$SERVER_DIR/plugins"
        fi
        
        if [ ! -f "$SERVER_DIR/plugins/basics-plugin.jar" ]; then
            log_warning "Basics Plugin nicht gefunden in $SERVER_DIR/plugins/"
            log_info "Bitte kopieren Sie das Basics Plugin in das plugins-Verzeichnis"
        fi
    done
}

create_server_properties() {
    for server in "${!SERVERS[@]}"; do
        SERVER_DIR="$SERVERS_DIR/$server"
        PROPERTIES_FILE="$SERVER_DIR/server.properties"
        
        if [ ! -f "$PROPERTIES_FILE" ]; then
            log_info "Erstelle server.properties für $server Server..."
            cat > "$PROPERTIES_FILE" << EOF
# Minecraft server properties
server-port=${SERVERS[$server]}
server-name=${server^} Server
motd=§6§lBasics Plugin §7- §e${server^} Server
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
level-type=minecraft\:normal
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
        fi
    done
}

create_eula() {
    for server in "${!SERVERS[@]}"; do
        SERVER_DIR="$SERVERS_DIR/$server"
        EULA_FILE="$SERVER_DIR/eula.txt"
        
        if [ ! -f "$EULA_FILE" ]; then
            log_info "Erstelle eula.txt für $server Server..."
            echo "eula=true" > "$EULA_FILE"
        fi
    done
}

start_server() {
    local server=$1
    local port=${SERVERS[$server]}
    local server_dir="$SERVERS_DIR/$server"
    
    log_info "Starte $server Server auf Port $port..."
    
    cd "$server_dir" || {
        log_error "Kann nicht in $server_dir wechseln"
        return 1
    }
    
    # Prüfe ob Server bereits läuft
    if pgrep -f "paper.*$port" > /dev/null; then
        log_warning "$server Server läuft bereits auf Port $port"
        return 0
    fi
    
    # Starte Server im Hintergrund
    nohup java $JAVA_OPTS -jar "$PAPER_JAR" nogui > "logs/server.log" 2>&1 &
    local pid=$!
    
    # Warte kurz und prüfe ob Server gestartet ist
    sleep 5
    if kill -0 $pid 2>/dev/null; then
        log_success "$server Server gestartet (PID: $pid)"
        echo $pid > "$server_dir/server.pid"
    else
        log_error "$server Server konnte nicht gestartet werden"
        return 1
    fi
}

stop_server() {
    local server=$1
    local server_dir="$SERVERS_DIR/$server"
    local pid_file="$server_dir/server.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if kill -0 $pid 2>/dev/null; then
            log_info "Stoppe $server Server (PID: $pid)..."
            kill $pid
            sleep 5
            
            # Force kill falls nötig
            if kill -0 $pid 2>/dev/null; then
                log_warning "Force kill für $server Server..."
                kill -9 $pid
            fi
            
            rm -f "$pid_file"
            log_success "$server Server gestoppt"
        else
            log_warning "$server Server läuft nicht"
            rm -f "$pid_file"
        fi
    else
        log_warning "Keine PID-Datei für $server Server gefunden"
    fi
}

start_all_servers() {
    log_info "Starte alle Server..."
    
    # Starte Hub zuerst
    start_server "hub"
    sleep 10
    
    # Starte andere Server
    for server in "${!SERVERS[@]}"; do
        if [ "$server" != "hub" ]; then
            start_server "$server"
            sleep 5
        fi
    done
    
    log_success "Alle Server gestartet!"
}

stop_all_servers() {
    log_info "Stoppe alle Server..."
    
    for server in "${!SERVERS[@]}"; do
        stop_server "$server"
    done
    
    log_success "Alle Server gestoppt!"
}

restart_server() {
    local server=$1
    log_info "Starte $server Server neu..."
    stop_server "$server"
    sleep 5
    start_server "$server"
}

restart_all_servers() {
    log_info "Starte alle Server neu..."
    stop_all_servers
    sleep 10
    start_all_servers
}

show_status() {
    log_info "Server-Status:"
    echo "----------------------------------------"
    
    for server in "${!SERVERS[@]}"; do
        local port=${SERVERS[$server]}
        local server_dir="$SERVERS_DIR/$server"
        local pid_file="$server_dir/server.pid"
        
        if [ -f "$pid_file" ]; then
            local pid=$(cat "$pid_file")
            if kill -0 $pid 2>/dev/null; then
                echo -e "${GREEN}✓${NC} $server Server (Port: $port, PID: $pid)"
            else
                echo -e "${RED}✗${NC} $server Server (Port: $port, PID-Datei vorhanden aber Prozess nicht aktiv)"
                rm -f "$pid_file"
            fi
        else
            echo -e "${RED}✗${NC} $server Server (Port: $port, nicht gestartet)"
        fi
    done
    
    echo "----------------------------------------"
}

show_logs() {
    local server=$1
    local server_dir="$SERVERS_DIR/$server"
    local log_file="$server_dir/logs/latest.log"
    
    if [ -f "$log_file" ]; then
        log_info "Zeige Logs für $server Server..."
        tail -f "$log_file"
    else
        log_error "Log-Datei nicht gefunden: $log_file"
    fi
}

show_help() {
    echo "Multi-Server Start Script für das Basics Plugin"
    echo ""
    echo "Verwendung: $0 [COMMAND] [SERVER]"
    echo ""
    echo "Commands:"
    echo "  start [server]     - Starte alle Server oder einen spezifischen Server"
    echo "  stop [server]      - Stoppe alle Server oder einen spezifischen Server"
    echo "  restart [server]   - Starte alle Server oder einen spezifischen Server neu"
    echo "  status             - Zeige Status aller Server"
    echo "  logs <server>      - Zeige Logs für einen Server"
    echo "  setup              - Richte Server-Verzeichnisse ein"
    echo "  help               - Zeige diese Hilfe"
    echo ""
    echo "Server:"
    for server in "${!SERVERS[@]}"; do
        echo "  $server (Port: ${SERVERS[$server]})"
    done
    echo ""
    echo "Beispiele:"
    echo "  $0 start           - Starte alle Server"
    echo "  $0 start hub       - Starte nur den Hub-Server"
    echo "  $0 stop island     - Stoppe nur den Island-Server"
    echo "  $0 restart         - Starte alle Server neu"
    echo "  $0 status          - Zeige Status aller Server"
    echo "  $0 logs hub        - Zeige Logs für den Hub-Server"
}

setup_servers() {
    log_info "Richte Multi-Server-System ein..."
    
    check_java
    check_directories
    create_server_properties
    create_eula
    
    log_success "Setup abgeschlossen!"
    log_info "Nächste Schritte:"
    log_info "1. Laden Sie Paper 1.20.4 herunter und platzieren Sie es in jedem Server-Verzeichnis"
    log_info "2. Kopieren Sie das Basics Plugin in das plugins-Verzeichnis jedes Servers"
    log_info "3. Starten Sie die Server mit: $0 start"
}

# Hauptlogik
case "${1:-help}" in
    "start")
        if [ -n "$2" ]; then
            if [[ -n "${SERVERS[$2]}" ]]; then
                start_server "$2"
            else
                log_error "Unbekannter Server: $2"
                exit 1
            fi
        else
            start_all_servers
        fi
        ;;
    "stop")
        if [ -n "$2" ]; then
            if [[ -n "${SERVERS[$2]}" ]]; then
                stop_server "$2"
            else
                log_error "Unbekannter Server: $2"
                exit 1
            fi
        else
            stop_all_servers
        fi
        ;;
    "restart")
        if [ -n "$2" ]; then
            if [[ -n "${SERVERS[$2]}" ]]; then
                restart_server "$2"
            else
                log_error "Unbekannter Server: $2"
                exit 1
            fi
        else
            restart_all_servers
        fi
        ;;
    "status")
        show_status
        ;;
    "logs")
        if [ -n "$2" ]; then
            if [[ -n "${SERVERS[$2]}" ]]; then
                show_logs "$2"
            else
                log_error "Unbekannter Server: $2"
                exit 1
            fi
        else
            log_error "Bitte geben Sie einen Server an"
            exit 1
        fi
        ;;
    "setup")
        setup_servers
        ;;
    "help"|*)
        show_help
        ;;
esac
