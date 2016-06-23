#!/bin/bash



for pc in boursouf     boursouflet     cocatrix     infini1     infini2     jolicoeur     mcgonagall     tesla     trelawney     xeonphi     botero     buffet     cassatt     escher     goya     kandinsky     lautrec     legreco     marquet     morissot     opalka     redon     rembrandt     seurat     signac     tissot     velasquez     vlaminck     watteau     whistler
do
	#mkdir $pc
	currentUser="sabfraj@signac"
	serverUser="sabfraj@$pc"
	currentFolder="$currentUser:./"
	serverFolder="$serverUser:/espace/sabfraj/$pc/*trace*.txt"

	scp -r $serverFolder $currentFolder
	#echo $serverFolder



done




