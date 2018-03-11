from eppy import modeleditor
from eppy.modeleditor import IDF
import sys
import os
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = sys.argv[1]
archivoIDF = sys.argv[2]
archivoEPW = sys.argv[3]

IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF, archivoEPW)
nuevaCarpeta = "optimizacion_" + \
    os.path.splitext(os.path.basename(archivoIDF))[0]
idf.run(output_directory=os.path.join(
    os.path.dirname(archivoIDF), nuevaCarpeta))
