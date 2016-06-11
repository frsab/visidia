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
#gnuplotFile_idA=_posAx0_posAy0_5000_LSM_W.txt

for size in 100  1000 5000 
do
#	for id in `seq 1 6`
	for id in   posAx0_posAy0  posAx200_posAy200 posAx250_posAy250 posAx100_posAy0  posAx250_posAy0  posAx50_posAy50   

	do 	
		nameRED="gnuplotFile_idA=_$id";
		nameRED+="_";
		nameRED+="$size"
		nameRED+="_RED_p.txt"
		nameLSM="gnuplotFile_idA=_$id";
		nameLSM+="_"
		nameLSM+="$size"
		nameLSM+="_LSM_p.txt"
		nameWALK="gnuplotFile_idA=_$id";
		nameWALK+="_"
		nameWALK+="$size"
		nameWALK+="_LSM_W.txt"
		nameSDC="gnuplotFile_idA=_$id";
		nameSDC+="_"
		nameSDC+="$size"
		nameSDC+="_SDC_p.txt"
		echo "$nameRED \n$nameLSM\n$nameWALK\n$nameSDC\n\n"	#gnuplotFile_idA=3_100_RED_p.txt
			for zzFILES_RED in $nameRED
			do
				for zzFILES_LSM in $nameLSM
				do
					for zzFILES_WALK in $nameWALK
					do
						for zzFILES_SDC in $nameSDC
						do

							# Rename output file to zombie.dat
							echo " mv $zzFILES_RED RED.dat"

							cp "${zzFILES_RED}" RED.dat
							cp "${zzFILES_LSM}" LSM.dat
							cp "${zzFILES_WALK}" WALK.dat
							cp "${zzFILES_SDC}" SDC.dat

							gnuplot out_generaleDetectRate.plt         
							gnuplot out_generaleNbMsg.plt
							gnuplot out_generaleMemoryConsumption.plt
							outPNGfileDetectRate="DetectRateRED_LSM_SDC_WALK_$id"
							outPNGfileDetectRate+="_$size"

							outPNGfileNbMsg="NbMsgRED_LSM_SDC_WALK_$id"
							outPNGfileNbMsg+="_$size"

							outPNGfileMemoryConsumption="MemoryConsumptionRED_LSM_SDC_WALK_$id"
							outPNGfileMemoryConsumption+="_$size"



							mv -f DetectRate.png "./detectRATE/${outPNGfileDetectRate}.png"

							mv -f NbMsg.png "./nbMSG/${outPNGfileNbMsg}.png"
							mv -f MemoryConsumption.png "./memoryCons/${outPNGfileMemoryConsumption}.png"
							done
							#echo "------------------------->${zzFILES}"



						done
					done
					
				done
			done
	done
done


