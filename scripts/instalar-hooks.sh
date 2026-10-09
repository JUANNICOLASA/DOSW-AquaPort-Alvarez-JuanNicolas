#!/bin/sh

git config core.hooksPath .githooks
chmod +x .githooks/commit-msg
echo "Hooks de AquaPort instalados en .githooks"
