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
		for algo in _RED_p _LSM_p _LSM_W _SDC_p
		do
		nameALGO="./gnuplotFileResult/gnuplotFile_idA=_$id";
		nameALGO+="_";
		nameALGO+="$size"
		nameALGO+="$algo.txt"
			for zzFILES_ALGO in $nameALGO
			do
				cp "${zzFILES_ALGO}" ALGO.dat

				gnuplot ./gnuplotScript/linesScripts/single_out_generaleDetectRate.plt         
				gnuplot ./gnuplotScript/linesScripts/single_out_generaleNbMsg.plt
				gnuplot ./gnuplotScript/linesScripts/single_out_generaleMemoryConsumption.plt

				outPNGfileDetectRate="DetectRate$algo"
				outPNGfileDetectRate+="_$id"
				outPNGfileDetectRate+="_$size"
				outPNGfileNbMsg="NbMsg_$algo"
				outPNGfileNbMsg+="_$id"
				outPNGfileNbMsg+="_$size"
				outPNGfileMemoryConsumption="MemoryConsumption_$algo"
				outPNGfileMemoryConsumption+="_$id"
				outPNGfileMemoryConsumption+="_$size"

				mv -f DetectRate.png "./graphique/linesVersions/detectRATE-separee/${outPNGfileDetectRate}.png"
				mv -f NbMsg.png "./graphique/linesVersions/nbMSG-separee/${outPNGfileNbMsg}.png"
				mv -f MemoryConsumption.png "./graphique/linesVersions/memoryCons-separee/${outPNGfileMemoryConsumption}.png"
				done
				#echo "------------------------->${zzFILES}"

			done
		done
		

	done
done


