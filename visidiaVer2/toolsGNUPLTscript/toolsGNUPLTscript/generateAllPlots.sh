#!/bin/bash
function l
{
  for i in `ls $1`
  do
    if [ -d "$i" ]
    then
      l $1/$i ;
    else
      echo $1/$i
    fi
  done
}


for size in 100 200 300 500 1000 5000 
do
	for id in `seq 1 2`
	do 	
		nameRED="gnuplotFile_idA=$id";
		nameRED+="_"
		nameRED+="$size"
		nameRED+="_RED_p.txt"
		nameLSM="gnuplotFile_idA=$id";
		nameLSM+="_"
		nameLSM+="$size"
		nameLSM+="_LSM_p.txt"
		echo "$nameRED $nameLSM"	#gnuplotFile_idA=3_100_RED_p.txt
			for zzFILES_RED in $nameRED
			do
				for zzFILES_LSM in $nameLSM
				do
					echo "------------------------->${zzFILES_RED}"
					echo "------------------------->${zzFILES_LSM}"
				# Rename output file to zombie.dat
				echo " mv $zzFILES_RED RED.dat"

				cp "${zzFILES_RED}" RED.dat
				cp "${zzFILES_LSM}" LSM.dat
#l .
#ls *.dat
				# Run gnuplot
				#gnuplot out_generale.plt



gnuplot out_generaleDetectRate.plt         
gnuplot out_generaleNbMsg.plt
gnuplot out_generaleMemoryConsumption.plt








				# Rename zombie.dat to original name
				#mv RED.dat  "${zzFILES_RED}" 
				#mv LSM.dat "${zzFILES_LSM}" 
			#	mv zombie.dat "${zzFILES}"
				# Rename zombie.png
				outPNGfileDetectRate="DetectRateRED_LSM_$id"
				outPNGfileDetectRate+="_$size"

				outPNGfileNbMsg="NbMsgRED_LSM_$id"
				outPNGfileNbMsg+="_$size"

				outPNGfileMemoryConsumption="MemoryConsumptionRED_LSM_$id"
				outPNGfileMemoryConsumption+="_$size"



				mv DetectRate.png "${outPNGfileDetectRate}.png"

				mv NbMsg.png "${outPNGfileNbMsg}.png"
				mv MemoryConsumption.png "${outPNGfileMemoryConsumption}.png"
				done
				#echo "------------------------->${zzFILES}"
				done
			done
	done
done

# {100,200,300,500,1000,5000}



