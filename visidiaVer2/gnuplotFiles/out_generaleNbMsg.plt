reset 
#set zrange [0:300000]
#set xrange [0:10] 
set terminal png size 1000,800
set output 'NbMsg.png'

set dgrid3d 15,15
set hidden3d

splot  'RED.dat' u 1:2:4 with lines,'LSM.dat' u 1:2:4 with lines ,'WALK.dat' u 1:2:4 with lines ,'SDC.dat' u 1:2:4 with lines