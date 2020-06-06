#!/bin/bash

set -euxo pipefail

cd "$(git rev-parse --show-toplevel)/src/main/resources/awele/view"

for f in scenebuilder-*.fxml; do
  edit_me="${f/scenebuilder-}"

  cp "$f" "$edit_me"
  sed -i 's|<\!--x||g' "$edit_me"
  sed -i 's|x-->||g' "$edit_me"
  sed -i 's|scenebuilder-||g' "$edit_me"
done
