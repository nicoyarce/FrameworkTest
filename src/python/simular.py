from eppy import modeleditor
from eppy.modeleditor import IDF
import sys
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = "C:\\EnergyPlusV8-3-0\\Energy+.idd"
archivoIDF = sys.argv[1]
archivoEPW = "C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\CHL_Concepcion.856820_IWEC.epw"
IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF, archivoEPW)
idf.run(output_directory="C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\simulacion\\")

