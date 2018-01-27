from eppy.modeleditor import IDF
import os
from shutil import copy2
import sys

pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = "C:\\EnergyPlusV8-3-0\\Energy+.idd"
archivoIDF = sys.argv[1]

rutaRespaldo = os.path.dirname(archivoIDF) + "/respaldo_" + os.path.basename(archivoIDF)

if not os.path.exists(rutaRespaldo):    
    copy2(archivoIDF, rutaRespaldo)

IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF)

materiales = idf.idfobjects['MATERIAL']

#indicar los variables a optimizar
#java captura la salida estandar para saber cuantas variables son

#for material in materiales:
print (materiales[0].Thickness)

#dada una lista de materiales, optimizar thickness