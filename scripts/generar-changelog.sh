#!/bin/bash

SALIDA="${1:-CHANGELOG.md}"
VERSION="$2"

titulo_tipo() {
    case "$1" in
        feat) echo "Nuevas funcionalidades" ;;
        fix) echo "Correcciones" ;;
        refactor) echo "Refactorizaciones" ;;
        test) echo "Pruebas" ;;
        docs) echo "Documentación" ;;
        build|ci) echo "Construcción" ;;
        *) echo "Mantenimiento" ;;
    esac
}

escribir_rango() {
    local rango="$1"
    for tipo in feat fix refactor test docs build chore; do
        local patron="^$tipo(\(.*\))?!?: "
        [ "$tipo" = "build" ] && patron="^(build|ci)(\(.*\))?!?: "
        [ "$tipo" = "chore" ] && patron="^(chore|perf|style|revert)(\(.*\))?!?: "
        local commits
        commits=$(git log --no-merges --format='%s' $rango | grep -E "$patron")
        if [ -n "$commits" ]; then
            echo "### $(titulo_tipo "$tipo")"
            echo
            echo "$commits" | sed -E 's/^[a-z]+(\(([^)]*)\))?!?: (.*)$/- \3/'
            echo
        fi
    done
}

{
    echo "# Changelog"
    echo
    echo "Generado automáticamente desde los commits con \`scripts/generar-changelog.sh\`."
    echo
    etiquetas=$(git tag --sort=-creatordate)
    pendientes=$(git log --no-merges --format='%s' HEAD --not --tags)
    if [ -n "$pendientes" ]; then
        if [ -n "$VERSION" ]; then
            echo "## $VERSION ($(date +%Y-%m-%d))"
        else
            echo "## Sin publicar"
        fi
        echo
        escribir_rango "HEAD --not --tags"
    fi
    for etiqueta in $etiquetas; do
        fecha=$(git log -1 --format='%ad' --date=short "$etiqueta")
        echo "## $etiqueta ($fecha)"
        echo
        previa=$(git describe --tags --abbrev=0 "$etiqueta^" 2>/dev/null)
        if [ -n "$previa" ]; then
            escribir_rango "$previa..$etiqueta"
        else
            escribir_rango "$etiqueta"
        fi
    done
} > "$SALIDA"

echo "Changelog generado en $SALIDA"
