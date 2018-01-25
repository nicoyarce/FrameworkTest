from eppy.modeleditor import IDF
import os
from shutil import copy2
import sys
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = "C:\\EnergyPlusV8-3-0\\Energy+.idd"
archivoIDF = sys.argv[1]

respaldo = os.path.dirname(archivoIDF) + "/respaldo_" + os.path.basename(archivoIDF)
copy2(archivoIDF, respaldo)

IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF)

materiales = idf.idfobjects['MATERIAL']

#for material in materiales:
print (materiales[0].Thickness)