#!/bin/bash
cd output || exit
list_files=$(find . -name 'tree_*.dot')
for file in $list_files; do
        svg_file="${file//.dot/.svg}"
        dot -Tsvg "$file" > "$svg_file"
        inkscape --export-type="pdf" "$svg_file"
        rm "$file"
        rm "$svg_file"
done
# shellcheck disable=SC2046
qpdf --empty --pages $(find . -name 'tree_*.pdf' -print | sort -V | tr '\n' ' ' | sed 's/\.\///g') -- trees.pdf
rm tree_*.pdf

