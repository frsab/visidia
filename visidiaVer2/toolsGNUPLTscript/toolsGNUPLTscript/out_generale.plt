reset 
#set zrange [0:300000]
#set xrange [0:10] 
set terminal png size 1000,800
set output '3dsample.png'

set dgrid3d 100,100
set hidden3d

splot  'RED.dat' u 1:2:3 with lines,'LSM.dat' u 1:2:3 with lines 
splot  'gnuplotFile_idA=0_100_RED_p.txt' u 1:2:3 with lines,'gnuplotFile_idA=0_100_LSM_p.txt' u 1:2:3 with lines 



splot  'gnuplotFile_idA=0_100_RED_p.txt' u 1:2:5 with lines,'gnuplotFile_idA=0_100_LSM_p.txt' u 1:2:5 with lines 


#, 'gnuplotFile_idA=2_100_RED_p.txt' u 1:2:3 , 'gnuplotFile_idA=0_100_RED_p.txt' u 1:2:3  , 'gnuplotFile_idA=3_100_LSM_p.txt' u 1:2:3 , 'gnuplotFile_idA=1_100_LSM_p.txt' u 1:2:3 with #pm3d , 'gnuplotFile_idA=3_100_RED_p.txt' u 1:2:3 , 'gnuplotFile_idA=1_100_RED_p.txt' u 1:2:3  , 'gnuplotFile_idA=4_100_LSM_p.txt' u 1:2:3 , 'gnuplotFile_idA=2_100_LSM_p.txt' u 1:2:3 uplot


				

