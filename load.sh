awk '{ if (length($0) > 40) print }' ricardo/* | tr '\n' ' ' > index.txt
