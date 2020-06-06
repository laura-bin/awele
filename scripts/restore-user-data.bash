#!/bin/bash

set -euxo pipefail

for f in *.fxml; do
  < "$f" tr -d '\n' | sed -i.original 's|<userData>.*</userData>|<!-- & -->|'
done
