#!/bin/bash

list_files=$(find output -name '*.dot')
for file in $list_files; do
        dot -Tsvg "$file" > "${file//.dot/.svg}"; rm "$file"
done

