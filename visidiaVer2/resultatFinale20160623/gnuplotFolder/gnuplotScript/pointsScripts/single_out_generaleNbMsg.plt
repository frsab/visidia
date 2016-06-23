reset 
set terminal postscript enhanced color
set terminal png size 1000,800
set output 'NbMsg.png'
set border linewidth 1.5
# Line styles
set style line 1 linecolor rgb 'blue' linetype 1 linewidth 5
set style line 2 linecolor rgb 'black' linetype 1 linewidth 5
set style line 3 linecolor rgb 'green' linetype 1 linewidth 5
set style line 4 linecolor rgb 'red' linetype 1 linewidth 5
set style line 5 linecolor rgb 'yellow' linetype 1 linewidth 5




# Axes label
set xlabel 'X Position' font "Bold, 14"
set ylabel 'Y Position' font "Bold, 14"
set zlabel 'VALUE' rotate by 90
# Axes ranges
set xrange [0:500]
set yrange [0:500]
#set zrange [0:100]
#set cbrange [0:100]

#unset surf
set style line 1 lt 4 lw .5
set ytics out font "Bold, 10"
set ztics out font "Bold, 10"
set xtics font "Bold, 10"


set key off
splot  'ALGO.dat' u 1:2:4 with points palette pointsize 3 pointtype 7
