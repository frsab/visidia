reset 
#set zrange [0:300000]
#set xrange [0:10] 
set terminal png size 1000,800
set output 'DetectRate.png'

set dgrid3d 100,100
set hidden3d

splot  'RED.dat' u 1:2:3 with lines,'LSM.dat' u 1:2:3 with lines 
