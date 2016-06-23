reset 
set xlabel "X axis" 
set ylabel "Y axis" 
set zlabel "Value" 

set terminal png size 1000,800
set output 'DetectRate.png'
set xrange [0:500]
set yrange [0:500]
set dgrid3d 20,20
set hidden3d
splot  'ALGO.dat' u 1:2:6 with lines
