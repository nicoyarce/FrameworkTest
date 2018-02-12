from eppy.modeleditor import IDF
import sys
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = "C:\\EnergyPlusV8-8-0\\Energy+.idd"
archivoIDF = sys.argv[1]
rutaMateriales = sys.argv[2]
IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF)
materiales = idf.idfobjects['MATERIAL']

archivoMateriales = open(rutaMateriales, "r")
listaMateriales = archivoMateriales.read().splitlines()
i = 0;
for linea in listaMateriales:
    for material in materiales:
        if(str(material.Name) == linea):            
            material.Thickness = sys.argv[i + 3]            
            break
    i += 1
archivoMateriales.close() 

idf.save()