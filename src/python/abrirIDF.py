from eppy.modeleditor import IDF
import os
from shutil import copy2
import sys

pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = "C:\\EnergyPlusV8-8-0\\Energy+.idd"
archivoIDF = sys.argv[1]
rutaMateriales = sys.argv[2]

rutaRespaldo = os.path.dirname(archivoIDF) + "/respaldo_" + os.path.basename(archivoIDF)
if not os.path.exists(rutaRespaldo):    
    copy2(archivoIDF, rutaRespaldo)

IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF)
materiales = idf.idfobjects['MATERIAL']

archivoMateriales = open(rutaMateriales, "r")
listaMateriales = archivoMateriales.read().splitlines()
for linea in listaMateriales:
    for material in materiales:
        if(str(material.Name) == linea):
            #print("Material encontrado " + str(material.Name) + " " + linea)
            #al imprimir mandar valor rangomin rangomax
            print(material.Thickness)
            #print(material.getrange("Thickness"))
            break
archivoMateriales.close()       
        
                    
            
#indicar los variables a optimizar
#java captura la salida estandar para saber cuantas variables son
#dada una lista de materiales, optimizar thickness