reset 
#set zrange [0:300000]
#set xrange [0:10] 
set terminal png size 1000,800
set output 'MemoryConsumption.png'

set dgrid3d 100,100
set hidden3d

splot  'gnuplotFile_idA=_posAx50_posAy50_5000_LSM_p' u 1:2:5 with lines 

